package com.example.lenovo.mylocationkt.navigation

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.example.lenovo.mylocationkt.activities.mainactivity.MainActivity
import com.example.lenovo.mylocationkt.fragments.currentLocation.CurrentLocationFragment
import com.example.lenovo.mylocationkt.fragments.currentLocation.CurrentLocationPresenterImpl
import com.example.lenovo.mylocationkt.fragments.dashboard.DashboardFragment
import com.example.lenovo.mylocationkt.fragments.dashboard.DashboardPresenterImpl
import com.example.lenovo.mylocationkt.fragments.settings.SettingsFragment
import com.example.lenovo.mylocationkt.fragments.settings.SettingsPresenterImpl

interface Router {
    fun goBack()
    fun goToSettings()
    fun goToDashboard()
    fun goToCurrentLocation()
}

class RouterImpl(val mainActivity: MainActivity, val fragmentManager: FragmentManager, val container: Int) : Router {

    override fun goBack() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToSettings() {
        val settingsView = SettingsFragment()
        val settingsPresenter = SettingsPresenterImpl()
        settingsView.setPresenter(settingsPresenter)
        settingsPresenter.setView(settingsView)

        transit(settingsView as SettingsFragment, false)
    }

    override fun goToDashboard() {
        val dashboardView = DashboardFragment()
        val dashboardPresenter = DashboardPresenterImpl()
        dashboardView.setPresenter(dashboardPresenter)
        dashboardPresenter.setView(dashboardView)

        transit(dashboardView as DashboardFragment, false)
    }

    override fun goToCurrentLocation() {
        val currentLocationView = CurrentLocationFragment()
        val currentLocationPresenter = CurrentLocationPresenterImpl()
        currentLocationView.setPresenter(currentLocationPresenter)
        currentLocationPresenter.setView(currentLocationView)
        currentLocationPresenter.setLocationController(mainActivity.locationController)

        transit(currentLocationView as CurrentLocationFragment, false)
    }

    private fun transit(fragment: Fragment, addToBackStack:Boolean) {
        val tag: String = fragment.javaClass.name
        if(addToBackStack) {
            fragmentManager.beginTransaction()
                .replace(container, fragment, tag)
                .addToBackStack(tag)
                .commit()
        } else {
            fragmentManager.beginTransaction()
                .replace(container, fragment, tag)
                .commit()
        }
    }
}