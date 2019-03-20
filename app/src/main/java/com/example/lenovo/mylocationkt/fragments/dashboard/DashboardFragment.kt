package com.example.lenovo.mylocationkt.fragments.dashboard

import com.example.lenovo.mylocationkt.R
import com.example.lenovo.mylocationkt.fragments.base.BasePresenter
import com.example.lenovo.mylocationkt.fragments.base.BaseView

/**
 * DashboardFragment
 * Add a useless comment v1.1
 * Add a useless comment
 */
class DashboardFragment : BaseView(), DashboardView {

    private var presenter: DashboardPresenter? = null

    override fun getLayoutRes(): Int = R.layout.fragment_dashboard

    override fun setPresenter(dashboardPresenter: DashboardPresenter) {
        this.presenter = dashboardPresenter
    }

    override fun providePresenter(): BasePresenter? = presenter
}