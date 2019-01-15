package com.example.lenovo.mylocationkt.fragments.currentLocation

import android.content.Intent
import com.example.lenovo.mylocationkt.controllers.LocationController
import com.example.lenovo.mylocationkt.fragments.base.BasePresenter
import com.example.lenovo.mylocationkt.models.LocationUpdate

interface CurrentLocationPresenter : BasePresenter{
    fun setView(view: CurrentLocationView)
    fun setLocationController(locationController: LocationController)
    fun openInMaps()
}

interface CurrentLocationView {
    fun setPresenter(currentLocationPresenter: CurrentLocationPresenter)
    fun showLocationUpdate(latitude: String, longitude: String, addressLine: String)
    fun showError(message: String)
    fun startIntent(intent: Intent)
}

