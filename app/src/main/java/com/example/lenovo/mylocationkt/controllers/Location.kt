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
import java.lang.Exception
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private val MIN_UPDATE_INTERVAL: Long = 1000
private val INTERVAL_FASTEST: Long = 5000

interface LocationController {
    suspend fun startLocationUpdates(): LocationUpdate
    fun cleanUp()
}

class NoPermissionsException : Exception()
class PlayservicesNotAvailableException : Exception()
class LocatingNotEnabledException : Exception()
class AddressDetailNotFoundException : Exception()

class LocationControllerImpl(
    var baseActivity: BaseActivity,
    private var permissionController: PermissionController
) : LocationController {

    private var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(baseActivity);
    private var geocoder = Geocoder(baseActivity, Locale.getDefault());
    private var locationCallback: LocationCallback? = null
    private var isChecking = false


    override suspend fun startLocationUpdates(): LocationUpdate {
        checkHasPermission()
        checkPlayServicesAvailable()
        checkLocatingEnabled()
        return getDetailsFromLocation(getLocation())
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLocation(): Location =
        suspendCoroutine<Location> { continuation ->
            if (!isChecking) {
                isChecking = true;
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult?) {
                        super.onLocationResult(locationResult)
                        Timber.d("Location Request Success " + locationResult.toString())
                        Log.d("TAGLOC","Location Request Success ..." + locationResult.toString())
                        locationResult?.lastLocation?.let {
                            isChecking = false
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

    override fun cleanUp() {
        if(locationCallback != null && fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            isChecking = false
        }
    }

    private fun createLocationRequest(): LocationRequest = LocationRequest()
        .setInterval(MIN_UPDATE_INTERVAL)
        .setFastestInterval(INTERVAL_FASTEST)
        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)

    private suspend fun checkHasPermission() {
        val hasPermission: Boolean = permissionController.requestPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        if (!hasPermission) throw NoPermissionsException()
    }

    private fun checkPlayServicesAvailable() {
        val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(baseActivity)
        if (resultCode != ConnectionResult.SUCCESS) throw PlayservicesNotAvailableException()
    }

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