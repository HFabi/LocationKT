package com.example.lenovo.mylocationkt.controllers

import android.Manifest
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import android.util.Log
import com.example.lenovo.mylocationkt.activities.base.BaseActivity
import com.example.lenovo.mylocationkt.models.LocationUpdate
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// Constants
private val MIN_UPDATE_INTERVAL: Long = 1000
private val INTERVAL_FASTEST: Long = 5000

interface LocationController {
    suspend fun startLocationUpdates(): LocationUpdate
    fun cleanUp()
}

// Exceptions
/**
 * Should be thrown when user has no runtime permission.
 */
class NoPermissionsException : Exception()

/**
 * Should be thrown when the google play services are not available
 * or not up-to-date.
 */
class PlayServicesNotAvailableException : Exception()

/**
 * Should be thrown when locating is disabled in settings.
 */
class LocatingNotEnabledException : Exception()

/**
 * Should be thrown when address details are not available.
 */
class AddressDetailNotFoundException : Exception()

/**
 * LocationControllerImpl
 *
 * Handles location request.
 *
 * @constructor
 * @property baseActivity baseActivity
 * @property permissionController permission controller
 */
class LocationControllerImpl(
    var baseActivity: BaseActivity,
    private var permissionController: PermissionController
) : LocationController {

    /** google play services location provider client */
    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(baseActivity);
    /** geo coder */
    private var geocoder = Geocoder(baseActivity, Locale.getDefault())
    /** callback */
    private var locationCallback: LocationCallback? = null
    /** is waiting for location response */
    private var isInRequest = false

    /**
     * Starts a new location request.
     * @return LocationUpdate
     */
    override suspend fun startLocationUpdates(): LocationUpdate {
        checkHasPermission()
        checkPlayServicesAvailable()
        checkLocatingEnabled()
        return getDetailsFromLocation(getLocation())
    }

    /**
     * Determines the location from the users position.
     * @return location
     */
    @SuppressLint("MissingPermission")
    private suspend fun getLocation(): Location =
        suspendCoroutine<Location> { continuation ->
            if (!isInRequest) {
                isInRequest = true;
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        super.onLocationResult(locationResult)
                        Timber.d("Location Request Success " + locationResult.toString())
                        Log.d("TAGLOC", "Location Request Success ..." + locationResult.toString())
                        locationResult?.lastLocation?.let {
                            isInRequest = false
                            fusedLocationProviderClient.removeLocationUpdates(this)
                            return continuation.resume(it)
                        }
                    }
                }
                fusedLocationProviderClient.requestLocationUpdates(
                    createLocationRequest(),
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                Timber.d("Location Request Denied: Is already searching location ...")
            }
        }

    /**
     * Determines address information for a given location.
     * @param location location
     * @return location update
     */
    private suspend fun getDetailsFromLocation(location: Location): LocationUpdate {
        return suspendCoroutine<LocationUpdate> { continuation ->
            try {
                geocoder
                    .getFromLocation(location.latitude, location.longitude, 1)
                    .let {
                        if (!it.isNullOrEmpty()) {
                            continuation.resume(
                                LocationUpdate(
                                    location.longitude,
                                    location.latitude,
                                    it[0].getAddressLine(0),
                                    it[0].countryName
                                )
                            )
                        }
                    }
            } catch (exception: Exception) {
                continuation.resumeWithException(AddressDetailNotFoundException())
            }
        }
    }

    /**
     * Removes callbacks and active location requests.
     */
    override fun cleanUp() {
        if (locationCallback != null && fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            isInRequest = false
        }
    }

    /**
     * Creates a location request.
     */
    private fun createLocationRequest(): LocationRequest = LocationRequest()
        .setInterval(MIN_UPDATE_INTERVAL)
        .setFastestInterval(INTERVAL_FASTEST)
        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

    /**
     * Checks if app has permissions for ACCESS_FINE_LOCATION
     * @throws NoPermissionsException when app has no permission
     */
    private suspend fun checkHasPermission() {
        val hasPermission: Boolean = permissionController.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (!hasPermission) throw NoPermissionsException()
    }

    /**
     * Checks if google play services are available.
     * @throws PlayServicesNotAvailableException when services not available
     */
    private fun checkPlayServicesAvailable() {
        val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(baseActivity)
        if (resultCode != ConnectionResult.SUCCESS) throw PlayServicesNotAvailableException()
    }

    /**
     * Checks if locating is enabled in the app settings.
     * @return true: locating is enabled
     * @throws LocatingNotEnabledException when locating is disabled
     */
    private suspend fun checkLocatingEnabled(): Boolean {
        return suspendCoroutine<Boolean> { continuation ->
            val locationSettingsRequest = LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest())
                .build()
            val client = LocationServices.getSettingsClient(baseActivity)
            val task = client.checkLocationSettings(locationSettingsRequest)
            task.addOnSuccessListener(baseActivity) { locationSettingsResponse -> continuation.resume(true) }
            task.addOnFailureListener(baseActivity) { e -> continuation.resumeWithException(LocatingNotEnabledException()) }
        }
    }
}