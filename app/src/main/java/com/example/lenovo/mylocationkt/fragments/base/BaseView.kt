package com.example.lenovo.mylocationkt.fragments.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseView : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), null);
    }

    abstract fun getLayoutRes(): Int

    abstract fun providePresenter(): BasePresenter?

    override fun onResume() {
        super.onResume()
        providePresenter()?.onResume()
    }

    override fun onPause() {
        super.onPause()
        providePresenter()?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        providePresenter()?.onDestroy()
    }
}