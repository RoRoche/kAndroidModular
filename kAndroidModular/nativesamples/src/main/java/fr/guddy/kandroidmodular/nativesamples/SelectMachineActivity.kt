package fr.guddy.kandroidmodular.nativesamples

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class SelectMachineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_machine)
        findViewById<Button>(R.id.select_machine_a).setOnClickListener {
            val intent = Intent()
            intent.putExtra(MACHINE_ID, 1L)
            setResult(
                    Activity.RESULT_OK,
                    intent
            )
            finish()
        }
    }

    companion object Params {
        val CAPABILITY = "SelectMachineActivity:capability"
        val MACHINE_ID = "SelectMachineActivity:machineId"
    }
}
