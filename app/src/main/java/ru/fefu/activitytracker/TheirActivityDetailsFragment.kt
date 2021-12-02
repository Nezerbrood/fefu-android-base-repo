package ru.fefu.activitytracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.fefu.activitytracker.databinding.TheirActivityDetailsBinding

class TheirActivityDetailsFragment: Fragment() {
    private var _binding: TheirActivityDetailsBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): MyActivityDetailsFragment {
            return MyActivityDetailsFragment()
        }
        const val tag = "their_details"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TheirActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            //activity?.onBackPressed()
            findNavController().navigate(R.id.action_theirActivityDetailsFragment_to_workoutFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}