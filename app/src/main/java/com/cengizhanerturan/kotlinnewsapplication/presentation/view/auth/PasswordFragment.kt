package com.cengizhanerturan.kotlinnewsapplication.presentation.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentPasswordBinding
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_DO_NOT_MATCH_PASSWORD
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_PASSWORD
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showDialog
import com.cengizhanerturan.kotlinnewsapplication.core.util.clearFocusAndHideKeyboard
import com.cengizhanerturan.kotlinnewsapplication.core.util.isPasswordValid

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordBinding? = null

    private val viewModel: AuthViewModel by activityViewModels()
    private var isLogin: Boolean = false

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
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocusAndHideKeyboard()
        init()
        continueButtonClicked()
        backButtonClicked()
        observeData(view)
    }

    private fun observeData(view: View) = viewModel.authState.observe(viewLifecycleOwner) { state ->
        when (state) {
            is AuthState.Idle -> {
                binding.loading.visibility = View.GONE
                binding.continueButton.isEnabled = true
                binding.continueButton.text = getString(R.string.continue_text)
            }

            is AuthState.Success -> {
                val action = if (isLogin) {
                    if (state.userVerified == true) {
                        PasswordFragmentDirections.actionPasswordFragmentToHomeFragment()
                    } else {
                        PasswordFragmentDirections.actionPasswordFragmentToVerifyMailFragment()
                    }
                } else {
                    PasswordFragmentDirections.actionPasswordFragmentToVerifyMailFragment()
                }
                viewModel.clearState()
                Navigation.findNavController(view).navigate(action)
            }

            is AuthState.Loading -> {
                binding.continueButton.text = ""
                binding.loading.visibility = View.VISIBLE
                binding.continueButton.isEnabled = false
            }

            is AuthState.Error -> {
                binding.loading.visibility = View.GONE
                binding.continueButton.isEnabled = true
                binding.continueButton.text = getString(R.string.continue_text)

                showDialog(
                    context = view.context,
                    layoutInflater = layoutInflater,
                    message = state.message
                )
            }
        }
    }

    private fun backButtonClicked() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun continueButtonClicked() {
        binding.continueButton.setOnClickListener {
            it.clearFocusAndHideKeyboard()
            binding.passwordInputLayout.error = null
            binding.rePasswordInputLayout.error = null

            val password = binding.passwordEditText.text.toString().trim()
            val rePassword = binding.rePasswordEditText.text.toString().trim()

            if (!isPasswordValid(password)) return@setOnClickListener

            viewModel.password = password

            if (isLogin) {
                viewModel.passwordProcessLogin()
            } else {
                handleRegisterPasswordValidation(password, rePassword)
            }
        }
    }

    private fun handleRegisterPasswordValidation(password: String, rePassword: String) {
        if (password != rePassword) {
            binding.rePasswordInputLayout.error = ERROR_DO_NOT_MATCH_PASSWORD
        } else {
            viewModel.passwordProcessRegister()
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

    private fun init() {
        isLogin = viewModel.isLogin ?: false
        if (isLogin) {
            binding.passwordTitle.text = getString(R.string.sign_in_title)
            binding.rePasswordInputLayout.visibility = View.GONE
        } else {
            binding.passwordTitle.text = getString(R.string.register_title)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}