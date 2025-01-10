package com.cengizhanerturan.kotlinnewsapplication.presentation.view.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.cengizhanerturan.kotlinnewsapplication.core.theme.ThemeManager
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentSettingsBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.State
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showDialog
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showLogoutDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    private val viewModel: SettingsViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSwitchValue(view.context)

        themeSwitchChangeListener(view.context)
        userInformationButtonClickListener()
        emailAddressButtonClickListener()
        passwordButtonClickListener()
        logOutButtonClickListener()

        observeData(view)
    }

    private fun getSwitchValue(context: Context) {
        val isDarkMode = ThemeManager.isDarkMode(context)
        binding.themeSwitch.isChecked = isDarkMode
    }

    private fun themeSwitchChangeListener(context: Context) =
        binding.themeSwitch.setOnCheckedChangeListener { _, isDarkMode ->
            ThemeManager.changeTheme(context, isDarkMode)
        }

    private fun observeData(view: View) = viewModel.state.observe(viewLifecycleOwner) { state ->
        when (state) {
            is State.Idle -> {
                binding.loading.visibility = View.GONE
            }

            is State.Success -> {
                binding.loading.visibility = View.GONE
                navigateToMail(view)
            }

            is State.Loading -> {
                binding.loading.visibility = View.VISIBLE
            }

            is State.Error -> {
                binding.loading.visibility = View.GONE
                showDialog(
                    context = requireContext(),
                    layoutInflater = layoutInflater,
                    message = state.message
                )

                viewModel.clearState()
            }
        }
    }

    private fun navigateToMail(view: View) {
        val action = SettingsFragmentDirections.actionSettingsFragmentToMailFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun logOutButtonClickListener() {
        binding.logoutButton.setOnClickListener {
            showLogoutDialog(
                context = requireContext(),
                layoutInflater = layoutInflater,
            ) {
                viewModel.logout()
            }
        }
    }

    private fun passwordButtonClickListener() {
        binding.updatePasswordButton.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToUpdatePasswordFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun emailAddressButtonClickListener() {
        binding.updateEmailButton.setOnClickListener {
            val action =
                SettingsFragmentDirections.actionSettingsFragmentToUpdateEmailAddressFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun userInformationButtonClickListener() {
        binding.updateUserInformationButton.setOnClickListener {
            val action = SettingsFragmentDirections.actionSettingsFragmentToUpdateUserInfoFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}