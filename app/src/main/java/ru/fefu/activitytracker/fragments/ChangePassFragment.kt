package ru.fefu.activitytracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.fefu.activitytracker.databinding.FragmentChangePassBinding

class ChangePassFragment: Fragment() {
    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance(): ChangePassFragment {
            return ChangePassFragment()
        }
        const val tag = "profile_change"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener{
            findNavController().popBackStack()
        }
        binding.buttonContinue.setOnClickListener{
            // TODO: 1/28/2022 Нет api для смены пароля 
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}