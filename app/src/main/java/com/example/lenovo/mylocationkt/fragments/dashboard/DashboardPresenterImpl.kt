package com.example.lenovo.mylocationkt.fragments.dashboard

import com.example.lenovo.mylocationkt.fragments.base.BasePresenterImpl

/**
 * Presenter for DashboardFragment
 */
class DashboardPresenterImpl : BasePresenterImpl(), DashboardPresenter {

    private lateinit var dashboardView: DashboardView

    override fun setView(dashboardView: DashboardView) {
        this.dashboardView = dashboardView
    }
}