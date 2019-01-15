package com.example.lenovo.mylocationkt.fragments.dashboard

import com.example.lenovo.mylocationkt.fragments.base.BasePresenterImpl

class DashboardPresenterImpl : BasePresenterImpl(), DashboardPresenter {

    private lateinit var dashboardView: DashboardView

    override fun setView(dashboardView: DashboardView) {
        this.dashboardView = dashboardView
    }
}