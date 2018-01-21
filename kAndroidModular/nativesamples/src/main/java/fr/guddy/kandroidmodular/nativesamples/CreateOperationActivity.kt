package fr.guddy.kandroidmodular.nativesamples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import org.jetbrains.anko.startActivityForResult


class CreateOperationActivity : AppCompatActivity(), SelectMachineFragment.OnFragmentInteractionListener {

    private val typeOfOperation: TypeOfOperation? = null
    private var selectedMachineId: Long? = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_operation)
        findViewById<Button>(R.id.button_select_machine).setOnClickListener {
            startActivityForResult<SelectMachineActivity>(
                    SELECT_MACHINE,
                    SelectMachineActivity.CAPABILITY to typeOfOperation
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SELECT_MACHINE) {
            if (resultCode == Activity.RESULT_OK) {
                selectedMachineId = data?.getLongExtra(SelectMachineActivity.MACHINE_ID, -1)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSelectedMachine(selectedMachineId: Long) {
        this.selectedMachineId = selectedMachineId
//        val anotherFragment = supportFragmentManager.findFragmentById(R.id.another_fragment_container_id) as AnotherFragment
//        if (anotherFragment == null) {
//            anotherFragment.updateUi(selectedMachineId)
//        } else {
//            TODO("create and display AnotherFragment with selectedMachineId")
//        }
    }

    enum class TypeOfOperation

    companion object {
        val SELECT_MACHINE = 1
    }
}
