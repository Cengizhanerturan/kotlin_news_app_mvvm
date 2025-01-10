package com.cengizhanerturan.kotlinnewsapplication.presentation.view.settings.update_password

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentUpdatePasswordBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.State
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_DO_NOT_MATCH_PASSWORD
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_PASSWORD
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.SUCCESS_UPDATE_PASSWORD
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showDialog
import com.cengizhanerturan.kotlinnewsapplication.core.util.clearFocusAndHideKeyboard
import com.cengizhanerturan.kotlinnewsapplication.core.util.isPasswordValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdatePasswordFragment : Fragment() {
    private var _binding: FragmentUpdatePasswordBinding? = null

    private val viewModel: UpdatePasswordViewModel by viewModels()

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
        _binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocusAndHideKeyboard()
        backButtonListener()
        updateButtonClicked()
        observeData()
    }

    private fun observeData() = viewModel.state.observe(viewLifecycleOwner) { state ->
        when (state) {
            is State.Idle -> {
                binding.loading.visibility = View.GONE
                binding.updateButton.isEnabled = true
                binding.updateButton.text = getString(R.string.update)
            }

            is State.Success -> {
                clearInputs()
                errorTextClear()

                binding.loading.visibility = View.GONE
                binding.updateButton.isEnabled = true
                binding.updateButton.text = getString(R.string.update)

                viewModel.clearState()

                showDialog(
                    context = requireContext(),
                    layoutInflater = layoutInflater,
                    message = SUCCESS_UPDATE_PASSWORD
                )
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

    private fun updateButtonClicked() {
        binding.updateButton.setOnClickListener {
            it.clearFocusAndHideKeyboard()
            binding.passwordInputLayout.error = null
            binding.newPasswordInputLayout.error = null
            binding.rePasswordInputLayout.error = null

            val currentPassword = binding.passwordEditText.text.toString().trim()
            val newPassword = binding.newPasswordEditText.text.toString().trim()
            val rePassword = binding.rePasswordEditText.text.toString().trim()

            if (currentPassword.isEmpty() || !currentPassword.isPasswordValid()) {
                binding.passwordInputLayout.error = ERROR_INVALID_PASSWORD
                return@setOnClickListener
            }

            if (newPassword.isEmpty() || !newPassword.isPasswordValid()) {
                binding.newPasswordInputLayout.error = ERROR_INVALID_PASSWORD
                return@setOnClickListener
            }

            if (newPassword != rePassword) {
                binding.rePasswordInputLayout.error = ERROR_DO_NOT_MATCH_PASSWORD
            } else {
                viewModel.updatePassword(currentPassword, newPassword)
            }
        }
    }

    private fun errorTextClear() {
        binding.passwordInputLayout.error = ""
        binding.newPasswordInputLayout.error = ""
        binding.rePasswordInputLayout.error = ""
    }

    private fun clearInputs() {
        binding.passwordEditText.setText("")
        binding.newPasswordEditText.setText("")
        binding.rePasswordEditText.setText("")
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