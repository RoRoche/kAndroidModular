package fr.guddy.kandroidmodular.usecases.userinput.fsm

import paperparcel.PaperParcel
import paperparcel.PaperParcelable


@PaperParcel
data class UserInputResult(val user: String) : PaperParcelable {
    companion object {
        @JvmField
        val CREATOR = PaperParcelUserInputResult.CREATOR
    }
}