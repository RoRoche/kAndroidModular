package fr.guddy.kandroidmodular.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStores
import android.support.v4.app.Fragment
import org.koin.android.architecture.ext.KoinFactory


inline fun <reified T : ViewModel> Fragment.getViewModelFromActivity(): T {
    val vm = ViewModelProvider(ViewModelStores.of(activity!!), KoinFactory)
    return vm.get(T::class.java)
}