package ru.fefu.activitytracker.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.databinding.NewActivityFragmentBinding
import ru.fefu.activitytracker.adapters.NewActivityTypeListAdapter
import ru.fefu.activitytracker.room.Activity
import java.time.LocalDateTime
import kotlin.random.Random

class NewActivityFragment:Fragment() {
    private var _binding: NewActivityFragmentBinding? = null
    private val binding get() = _binding!!
    private val adapter = NewActivityTypeListAdapter()
    companion object {
        fun newInstance(): NewActivityFragment {
            return NewActivityFragment()
        }
        const val tag = "new_activity"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NewActivityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycleView = binding.newActivityList
        recycleView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recycleView.adapter = adapter
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.buttonContinue.setOnClickListener {
            if(adapter.selected!=-1)
            {

                App.INSTANCE.db.activityDao().insert(Activity(0, adapter.selected,
                    System.currentTimeMillis(),System.currentTimeMillis()+1000000,
                    listOf(Pair(
                        Random.nextDouble(43.566651, 43.626951),
                        Random.nextDouble(131.987517, 131.998417))
                        ,Pair(Random.nextDouble(43.566651, 43.626951),
                            Random.nextDouble(131.987517, 131.998417))),
                    ""))
                    findNavController().popBackStack()
                //findNavController().navigate(R.id.action_newActivityFragment_to_startedActivityFragment)
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}