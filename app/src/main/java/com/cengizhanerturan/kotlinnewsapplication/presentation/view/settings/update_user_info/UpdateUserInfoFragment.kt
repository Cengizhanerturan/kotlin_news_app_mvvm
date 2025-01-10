package com.cengizhanerturan.kotlinnewsapplication.presentation.view.settings.update_user_info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentUpdateUserInfoBinding
import com.cengizhanerturan.kotlinnewsapplication.domain.model.State
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_NAME
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_SURNAME
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.SUCCESS_UPDATE_USER_INFO
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showDialog
import com.cengizhanerturan.kotlinnewsapplication.core.util.clearFocusAndHideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateUserInfoFragment : Fragment() {
    private var _binding: FragmentUpdateUserInfoBinding? = null

    private val viewModel: UpdateUserInfoViewModel by viewModels()

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
        _binding = FragmentUpdateUserInfoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocusAndHideKeyboard()
        viewModel.clearState()
        backButtonListener()
        setDefaultUserInfo()
        observeData()
        updateButtonClickListener()
    }

    private fun updateButtonClickListener() {
        binding.updateButton.setOnClickListener {
            it.clearFocusAndHideKeyboard()
            errorTextClear()
            val name = binding.nameEditText.text.toString().trim()
            val surname = binding.surnameEditText.text.toString().trim()
            if (name.isEmpty()) {
                binding.nameInputLayout.error = ERROR_INVALID_NAME
            } else if (surname.isEmpty()) {
                binding.surnameInputLayout.error = ERROR_INVALID_SURNAME
            } else {
                viewModel.updateUserInfo(name = name, surname = surname)
            }
        }
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
                setDefaultUserInfo()
                errorTextClear()

                binding.loading.visibility = View.GONE
                binding.updateButton.isEnabled = true
                binding.updateButton.text = getString(R.string.update)

                showDialog(
                    context = requireContext(),
                    layoutInflater = layoutInflater,
                    message = SUCCESS_UPDATE_USER_INFO
                )

                viewModel.clearState()
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

    private fun errorTextClear() {
        binding.nameInputLayout.error = ""
        binding.surnameInputLayout.error = ""
    }

    private fun clearInputs() {
        binding.nameEditText.setText("")
        binding.surnameEditText.setText("")
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