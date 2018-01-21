package fr.guddy.kandroidmodular.nativesamples

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class SelectMachineFragment : Fragment() {

    private var capability: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            capability = arguments.getString(CAPABILITY)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_select_machine, container, false)
        view.findViewById<Button>(R.id.select_machine_a).setOnClickListener {
            listener?.onSelectedMachine(1L)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onSelectedMachine(selectedMachineId: Long)
    }

    companion object {
        private val CAPABILITY = "SelectMachineActivity:capability"
        fun newInstance(capability: String): SelectMachineFragment {
            val fragment = SelectMachineFragment()
            val args = Bundle()
            args.putString(CAPABILITY, capability)
            fragment.arguments = args
            return fragment
        }
    }
}