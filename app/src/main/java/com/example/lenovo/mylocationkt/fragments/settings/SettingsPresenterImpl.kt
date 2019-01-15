package com.example.lenovo.mylocationkt.fragments.settings

import com.example.lenovo.mylocationkt.fragments.base.BasePresenterImpl

class SettingsPresenterImpl : SettingsPresenter, BasePresenterImpl() {

    private lateinit var settingsView: SettingsView

    override fun setView(settingsView: SettingsView) {
        this.settingsView = settingsView
    }
}