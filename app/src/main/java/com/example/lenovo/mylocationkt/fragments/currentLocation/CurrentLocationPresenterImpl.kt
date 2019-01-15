package com.example.lenovo.mylocationkt.fragments.currentLocation

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.lenovo.mylocationkt.controllers.AddressDetailNotFoundException
import com.example.lenovo.mylocationkt.controllers.LocationController
import com.example.lenovo.mylocationkt.controllers.NoPermissionsException
import com.example.lenovo.mylocationkt.fragments.base.BasePresenterImpl
import com.example.lenovo.mylocationkt.models.LocationUpdate
import kotlinx.coroutines.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import timber.log.Timber

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
        debug("in working+"+isWorking)
        if(!isWorking) {
            debug("in working")
            isWorking = true
            launch{
                val locationUpdate = withContext(Dispatchers.Default) {locationController.startLocationUpdates()}
                lastLocationUpdate = locationUpdate
                view.showLocationUpdate(locationUpdate.addressLine.replace(",", "\n"), locationUpdate.latitude.toString(), locationUpdate.longitude.toString())
            }.invokeOnCompletion {
                debug("onResume onCompletion")
                isWorking = false
            }
        }
    }

    override val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable){
            is NoPermissionsException ->  view.showError("Please give permission")
            is AddressDetailNotFoundException -> debug ("AddressDetailNotFoundException")
            else -> throwable.printStackTrace()
        }
    }

    override fun openInMaps() {
        val loationUpdateCopy = lastLocationUpdate;
        if(loationUpdateCopy is LocationUpdate) {
            val gmmIntentUri = Uri.parse("geo:${loationUpdateCopy.latitude},${loationUpdateCopy.longitude}");
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            view.startIntent(mapIntent)
        }
    }
}