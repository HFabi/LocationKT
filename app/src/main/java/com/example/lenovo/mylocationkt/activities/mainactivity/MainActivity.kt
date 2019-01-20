package com.example.lenovo.mylocationkt.activities.mainactivity

import android.os.Bundle
import com.example.lenovo.mylocationkt.R
import com.example.lenovo.mylocationkt.activities.base.BaseActivity
import com.example.lenovo.mylocationkt.controllers.LocationController
import com.example.lenovo.mylocationkt.controllers.LocationControllerImpl
import com.example.lenovo.mylocationkt.controllers.PermissionController
import com.example.lenovo.mylocationkt.controllers.PermissionControllerImpl
import com.example.lenovo.mylocationkt.navigation.Router
import com.example.lenovo.mylocationkt.navigation.RouterImpl
import kotlinx.android.synthetic.main.activity_main.*

/**
 * MainActivity
 */
class MainActivity : BaseActivity() {

    /** controller for permission requests */
    lateinit var permissionController: PermissionController
    /** controller for location requests */
    lateinit var locationController: LocationController
    /** router */
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpDependencies()
        setUpClickListener()
        router.goToDashboard()
    }

    override fun getLayoutRes(): Int = R.layout.activity_main

    /**
     * Create dependencies.
     */
    private fun setUpDependencies() {
        router = RouterImpl(this@MainActivity, supportFragmentManager, R.id.main_framelayout)
        permissionController = PermissionControllerImpl(this@MainActivity)
        locationController = LocationControllerImpl(this, permissionController)
    }

    /**
     * Create click listener for navigation.
     */
    private fun setUpClickListener() {
        main_floatingactionbutton.setOnClickListener({ router?.goToCurrentLocation() })
        main_dashboard_imageview.setOnClickListener({ router?.goToDashboard() })
        main_settings_imageview.setOnClickListener({ router?.goToSettings() })
    }
}
