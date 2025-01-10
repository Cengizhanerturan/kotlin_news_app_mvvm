package com.cengizhanerturan.kotlinnewsapplication.presentation.view.settings.update_email_address

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentUpdateEmailAddressBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.State
import com.cengizhanerturan.kotlinnewsapplication.presentation.view.auth.AuthViewModel
import com.cengizhanerturan.kotlinnewsapplication.presentation.view.auth.VerifyMailFragmentDirections
import com.cengizhanerturan.kotlinnewsapplication.presentation.view.splash.SplashFragmentDirections
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_EMAIL
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_NAME
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_PASSWORD
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_SURNAME
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.SUCCESS_UPDATE_EMAIL
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.SUCCESS_UPDATE_USER_INFO
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showDialog
import com.cengizhanerturan.kotlinnewsapplication.core.util.clearFocusAndHideKeyboard
import com.cengizhanerturan.kotlinnewsapplication.core.util.isEmailValid
import com.cengizhanerturan.kotlinnewsapplication.core.util.isPasswordValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateEmailAddressFragment : Fragment() {
    private var _binding: FragmentUpdateEmailAddressBinding? = null

    private val viewModel: UpdateEmailAddressViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

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
        _binding = FragmentUpdateEmailAddressBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocusAndHideKeyboard()
        backButtonListener()
        setDefaultUserInfo()
        updateButtonClickListener()
        observeData(view)
    }

    private fun updateButtonClickListener() {
        binding.updateButton.setOnClickListener {
            it.clearFocusAndHideKeyboard()
            errorTextClear()
            val newEmail = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (newEmail.isEmpty() || !newEmail.isEmailValid()) {
                binding.emailInputLayout.error = ERROR_INVALID_EMAIL
            } else if (!isPasswordValid(password)) {
                return@setOnClickListener
            } else {
                viewModel.updateEmailAddress(password = password, newEmail = newEmail)
            }
        }
    }


    private fun observeData(view: View) = viewModel.state.observe(viewLifecycleOwner) { state ->
        when (state) {
            is State.Idle -> {
                binding.loading.visibility = View.GONE
                binding.updateButton.isEnabled = true
                binding.updateButton.text = getString(R.string.update)
            }

            is State.Success -> {
                clearInputs()
                setDefaultUserInfo()
                errorTextClear()

                binding.loading.visibility = View.GONE
                binding.updateButton.isEnabled = true
                binding.updateButton.text = getString(R.string.update)

                viewModel.clearState()

                showDialog(
                    context = requireContext(),
                    layoutInflater = layoutInflater,
                    message = SUCCESS_UPDATE_EMAIL
                ) {
                    authViewModel.isUnverifiedEmailAfterUpdate = true
                    navigateToVerifyMail(view)
                }
            }

            is State.Loading -> {
                binding.updateButton.text = ""
                binding.loading.visibility = View.VISIBLE
                binding.updateButton.isEnabled = false
            }

            is State.Error -> {
                binding.loading.visibility = View.GONE
                binding.updateButton.isEnabled = true
                binding.updateButton.text = getString(R.string.update)
                showDialog(
                    context = requireContext(),
                    layoutInflater = layoutInflater,
                    message = state.message
                )

                viewModel.clearState()
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return if (password.isEmpty() || !password.isPasswordValid()) {
            binding.passwordInputLayout.error = ERROR_INVALID_PASSWORD
            false
        } else {
            true
        }
    }

    private fun navigateToVerifyMail(view: View) {
        val action =
            UpdateEmailAddressFragmentDirections.actionUpdateEmailAddressFragmentToVerifyMailFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun errorTextClear() {
        binding.emailInputLayout.error = ""
        binding.passwordInputLayout.error = ""
    }

    private fun clearInputs() {
        binding.emailEditText.setText("")
        binding.passwordEditText.setText("")
    }

    private fun setDefaultUserInfo() {
        binding.user = viewModel.getDefaultUser()
    }

    private fun backButtonListener() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}