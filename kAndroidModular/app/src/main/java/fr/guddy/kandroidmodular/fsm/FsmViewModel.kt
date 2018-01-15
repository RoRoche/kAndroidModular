package fr.guddy.kandroidmodular.fsm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.annotation.VisibleForTesting
import au.com.ds.ef.EventEnum


class FsmViewModel : ViewModel() {
    val fsmModel: MutableLiveData<FsmModel> = MutableLiveData()

    init {
        fsmModel.value = FsmModel()
    }

    val flowContext: FsmContext
        get() = fsmModel.value?.flowContext!!

    fun trigger(event: EventEnum) {
        flowContext.safeTrigger(event)
    }

    @VisibleForTesting
    fun updateFlowContext(fsmContext: FsmContext) {
        fsmModel.value = FsmModel(
                forceEnterInitialState = true,
                flowContext = fsmContext
        )
    }
}