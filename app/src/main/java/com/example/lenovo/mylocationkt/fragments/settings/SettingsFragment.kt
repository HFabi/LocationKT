package com.example.lenovo.mylocationkt.fragments.settings

import com.example.lenovo.mylocationkt.R
import com.example.lenovo.mylocationkt.fragments.base.BasePresenter
import com.example.lenovo.mylocationkt.fragments.base.BaseView

class SettingsFragment : BaseView(), SettingsView {

    private var presenter: SettingsPresenter? = null

    override fun getLayoutRes(): Int = R.layout.fragment_settings

    override fun setPresenter(settingsPresenter: SettingsPresenter) {
        this.presenter = settingsPresenter
    }

    override fun providePresenter(): BasePresenter? = presenter

}