package com.example.lenovo.mylocationkt.fragments.currentLocation

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.lenovo.mylocationkt.R
import com.example.lenovo.mylocationkt.fragments.base.BasePresenter
import com.example.lenovo.mylocationkt.fragments.base.BaseView
import kotlinx.android.synthetic.main.fragment_currentlocation.*
import org.jetbrains.anko.support.v4.longToast

/**
 * CurrentLocationFragment
 */
class CurrentLocationFragment : BaseView(), CurrentLocationView {

    private var presenter: CurrentLocationPresenter? = null

    override fun getLayoutRes(): Int = R.layout.fragment_currentlocation

    override fun setPresenter(currentLocationPresenter: CurrentLocationPresenter) {
        this.presenter = currentLocationPresenter
    }

    override fun providePresenter(): BasePresenter? = presenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_imageview.setOnClickListener { presenter?.openInMaps() }
        cardView.visibility = View.GONE
    }

    /**
     * Updates the displayed location.
     * @param latitude latitude
     * @param longitude longitude
     * @param addressLine addressLine
     */
    override fun showLocationUpdate(latitude: String, longitude: String, addressLine: String) {
        cardView.visibility = View.VISIBLE
        currentlocation_latitude_textview.text = latitude
        currentlocation_longitude_textview.text = longitude
        currentlocation_address_textview.text = addressLine
    }

    /**
     * Hides the location view and shows an error message.
     * @param message error message
     */
    override fun showError(message: String) {
        cardView.visibility = View.GONE
        longToast(message).show()
    }

    /**
     * Starts the given intent.
     * @param intent intent
     */
    override fun startIntent(intent: Intent) {
        startActivity(intent)
    }
}