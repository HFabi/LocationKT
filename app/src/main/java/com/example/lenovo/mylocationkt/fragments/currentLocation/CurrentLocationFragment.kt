package com.example.lenovo.mylocationkt.fragments.currentLocation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.lenovo.mylocationkt.R
import com.example.lenovo.mylocationkt.fragments.base.BasePresenter
import com.example.lenovo.mylocationkt.fragments.base.BaseView
import kotlinx.android.synthetic.main.fragment_currentlocation.*
import android.content.Intent
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast


class CurrentLocationFragment : BaseView(), CurrentLocationView {

    private var presenter: CurrentLocationPresenter? = null

    override fun getLayoutRes(): Int = R.layout.fragment_currentlocation

    override fun setPresenter(currentLocationPresenter: CurrentLocationPresenter) {
        this.presenter = currentLocationPresenter
    }

    override fun providePresenter(): BasePresenter? = presenter

    override fun showLocationUpdate(latitude: String, longitude: String, addressLine: String) {
        cardView.visibility = View.VISIBLE
        currentlocation_latitude_textview.text = latitude
        currentlocation_longitude_textview.text = longitude
        currentlocation_address_textview.text = addressLine
    }

    override fun showError(message: String) {
        cardView.visibility = View.GONE
        longToast(message).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        map_imageview.setOnClickListener{ presenter?.openInMaps()}
        cardView.visibility = View.GONE
    }

    override fun startIntent(intent: Intent) {
        startActivity(intent)
    }
}