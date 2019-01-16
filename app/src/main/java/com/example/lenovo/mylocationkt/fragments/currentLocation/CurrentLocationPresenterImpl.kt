package com.example.lenovo.mylocationkt.fragments.currentLocation

import android.content.Intent
import android.net.Uri
import com.example.lenovo.mylocationkt.controllers.*
import com.example.lenovo.mylocationkt.fragments.base.BasePresenterImpl
import com.example.lenovo.mylocationkt.models.LocationUpdate
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug

class CurrentLocationPresenterImpl : BasePresenterImpl(), CurrentLocationPresenter, AnkoLogger {

    private lateinit var view: CurrentLocationView
    private lateinit var locationController: LocationController
    private var lastLocationUpdate: LocationUpdate? = null;
    private var isWorking: Boolean = false;

    override fun setView(view: CurrentLocationView) {
        this.view = view
    }

    override fun setLocationController(locationController: LocationController) {
        this.locationController = locationController
    }

    override fun onResume() {
        debug("in working+" + isWorking)
        if (!isWorking) {
            debug("in working")
            isWorking = true
            launch {
                val locationUpdate = withContext(Dispatchers.Default) { locationController.startLocationUpdates() }
                lastLocationUpdate = locationUpdate
                view.showLocationUpdate(
                    locationUpdate.latitude.toString(),
                    locationUpdate.longitude.toString(),
                    locationUpdate.addressLine.replace(", ", "\n")
                )
            }.invokeOnCompletion {
                debug("onResume onCompletion")
                isWorking = false
            }
        }
    }

    override val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is NoPermissionsException -> view.showError("Please give permission")
            is AddressDetailNotFoundException -> view.showError("Address details not found")
            is PlayservicesNotAvailableException -> view.showError("Google PlayServices not found")
            is LocatingNotEnabledException -> view.showError("Please enable locating in settings")
            else -> throwable.printStackTrace()
        }
    }

    override fun openInMaps() {
        lastLocationUpdate?.let {
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${it.latitude},${it.longitude}"))
            mapIntent.setPackage("com.google.android.apps.maps")
            view.startIntent(mapIntent)
        }
    }

    override fun onPause() {
        super.onPause()
        locationController.cleanUp()
        isWorking = false
    }
}