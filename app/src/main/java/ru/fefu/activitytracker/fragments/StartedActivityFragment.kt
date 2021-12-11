package ru.fefu.activitytracker.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.databinding.StartedActivityFragmentBinding

class StartedActivityFragment:Fragment() {
    private var _binding: StartedActivityFragmentBinding? = null
    private val binding get() = _binding!!
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
        _binding = StartedActivityFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.finishActivityButton.setOnClickListener {
        }
        binding.pauseActivityButton.setOnClickListener {
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}