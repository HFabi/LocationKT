package com.example.lenovo.mylocationkt.fragments.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * BaseView
 */
abstract class BaseView : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutRes(), null)
    }

    /**
     * Provides layout resource id
     * @return resource id
     */
    abstract fun getLayoutRes(): Int

    /**
     * Provides the presenter to parent
     * @return presenter
     */
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