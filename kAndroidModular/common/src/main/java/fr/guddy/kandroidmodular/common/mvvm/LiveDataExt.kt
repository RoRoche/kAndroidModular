package fr.guddy.kandroidmodular.common.mvvm

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer


fun <T> LiveData<T>.observe(lifecycleOwer: LifecycleOwner, onChanged: (T) -> Unit) {
    observe(
            lifecycleOwer,
            Observer {
                it?.let { onChanged(it) }
            }
    )
}