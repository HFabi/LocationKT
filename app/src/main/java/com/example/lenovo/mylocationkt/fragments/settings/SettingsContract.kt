package com.example.lenovo.mylocationkt.fragments.settings

import com.example.lenovo.mylocationkt.fragments.base.BasePresenter

interface SettingsPresenter : BasePresenter{
    fun setView(settingsView: SettingsView)
}

interface SettingsView {
    fun setPresenter(settingsPresenter: SettingsPresenter)
}