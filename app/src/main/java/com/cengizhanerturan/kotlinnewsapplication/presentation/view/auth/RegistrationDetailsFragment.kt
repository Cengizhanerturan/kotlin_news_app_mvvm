package com.cengizhanerturan.kotlinnewsapplication.presentation.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentRegistrationDetailsBinding
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_NAME
import com.cengizhanerturan.kotlinnewsapplication.core.util.Constants.ERROR_INVALID_SURNAME
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showDialog
import com.cengizhanerturan.kotlinnewsapplication.core.util.clearFocusAndHideKeyboard

class RegistrationDetailsFragment : Fragment() {
    private var _binding: FragmentRegistrationDetailsBinding? = null

    private val viewModel: AuthViewModel by activityViewModels()

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
        _binding = FragmentRegistrationDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.clearFocusAndHideKeyboard()
        viewModel.clearState()
        continueButtonClicked()
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
                val action =
                    RegistrationDetailsFragmentDirections.actionRegistrationDetailsFragmentToHomeFragment()
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
                    context = requireContext(),
                    layoutInflater = layoutInflater,
                    message = state.message
                )
            }
        }
    }

    private fun continueButtonClicked() {
        binding.continueButton.setOnClickListener {
            it.clearFocusAndHideKeyboard()
            val name = binding.nameEditText.text.toString().trim()
            val surname = binding.surnameEditText.text.toString().trim()

            viewModel.name = name
            viewModel.surname = surname

            if (name.isEmpty()) {
                binding.nameInputLayout.error = ERROR_INVALID_NAME
            } else if (surname.isEmpty()) {
                binding.nameInputLayout.error = ERROR_INVALID_SURNAME
            } else {
                viewModel.registrationDetailsProcess()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}