package ru.fefu.activitytracker.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.fefu.activitytracker.App
import ru.fefu.activitytracker.R
import ru.fefu.activitytracker.activities.WelcomeActivity
import ru.fefu.activitytracker.databinding.FragmentProfileBinding
import ru.fefu.activitytracker.retrofit.Result
import ru.fefu.activitytracker.retrofit.response.UserModel

class ProfileFragment: Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ProfileViewModel

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
        const val tag = "profile_view"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.change.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_changePassFragment)
        }

        viewModel.logoutUser
            .onEach {
                if (it is Result.Success<Unit>) {
                    App.INSTANCE.sharedPrefs.edit().remove("token").apply()
                    val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                else if (it is Result.Error<Unit>) {
                    Toast.makeText(requireContext(), it.e.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .launchIn(lifecycleScope)

        viewModel.profile
            .onEach {
                if (it is Result.Success<UserModel>) {
                    binding.login.editText?.setText(it.result.login)
                    binding.nickname.editText?.setText(it.result.name)
                }
                else if (it is Result.Error<UserModel>) {
                    Toast.makeText(requireContext(), it.e.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .launchIn(lifecycleScope)
        viewModel.getProfile()

        binding.buttonLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}