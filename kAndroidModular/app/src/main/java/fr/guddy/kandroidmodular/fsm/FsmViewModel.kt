package fr.guddy.kandroidmodular.fsm

import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting


class FsmViewModel : ViewModel() {
    private var model: Model = Model()

    @VisibleForTesting
    fun setModel(model: Model) {
        this.model = model
    }
}