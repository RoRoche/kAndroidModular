package fr.guddy.kandroidmodular.fsm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting


class FsmViewModel : ViewModel() {
    val context: MutableLiveData<FsmContext> = MutableLiveData()
    private var model: Model = Model()

    init {
        context.value = FsmContext(model = model)
    }

    @VisibleForTesting
    fun setModel(model: Model) {
        this.model = model
        context.value = FsmContext(true, model)
    }
}