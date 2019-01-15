package com.example.lenovo.mylocationkt.fragments.dashboard

import com.example.lenovo.mylocationkt.fragments.base.BasePresenter


interface DashboardPresenter : BasePresenter{
    fun setView(dashboardView: DashboardView)
}

interface DashboardView {
    fun setPresenter(dashboardPresenter: DashboardPresenter)
}