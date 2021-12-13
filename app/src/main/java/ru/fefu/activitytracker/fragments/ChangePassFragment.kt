package ru.fefu.activitytracker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.databinding.FragmentChangePassBinding

class ChangePassFragment : Fragment() {
    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        return binding.root
    }
    companion object{
        fun newInstance(message: String): ProfileFragment {
            val args = Bundle()
            args.putString("message", message)
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.buttonContinue.setOnClickListener{
            // TODO: 12/11/2021 implement password change
        }
    }
}