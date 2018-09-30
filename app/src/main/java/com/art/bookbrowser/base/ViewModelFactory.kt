package com.art.bookbrowser.base

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.art.bookbrowser.di.AppContext
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
@AppContext
class ViewModelFactory @Inject constructor(
        private val app: Application,
        private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}