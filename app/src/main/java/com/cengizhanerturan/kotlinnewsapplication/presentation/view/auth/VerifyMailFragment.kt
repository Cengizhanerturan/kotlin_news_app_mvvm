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
import com.cengizhanerturan.kotlinnewsapplication.databinding.FragmentVerifyMailBinding
import com.cengizhanerturan.kotlinnewsapplication.core.util.DialogUtils.showDialog

class VerifyMailFragment : Fragment() {
    private var _binding: FragmentVerifyMailBinding? = null

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
        _binding = FragmentVerifyMailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButtonVisibilityControl()
        backButtonClicked()
        viewModel.verifyProcess()
        continueButtonClicked()
        observeData(view)

    }

    private fun backButtonVisibilityControl() {
        if (viewModel.isUnverifiedEmailAfterUpdate) {
            binding.backButton.visibility = View.GONE
        } else {
            binding.backButton.visibility = View.VISIBLE
        }
    }

    private fun continueButtonClicked() {
        binding.continueButton.setOnClickListener {
            val action = if (viewModel.isLogin == true) {
                VerifyMailFragmentDirections.actionVerifyMailFragmentToHomeFragment()
            } else {
                VerifyMailFragmentDirections.actionVerifyMailFragmentToRegistrationDetailsFragment()
            }
            viewModel.clearState()
            Navigation.findNavController(it).navigate(action)
        }
    }

    private fun observeData(view: View) = viewModel.authState.observe(viewLifecycleOwner) { state ->
        when (state) {
            is AuthState.Idle -> {
                binding.continueButton.text = ""
                binding.loading.visibility = View.VISIBLE
                binding.continueButton.isEnabled = false
            }

            is AuthState.Success -> {
                binding.continueButton.text = getString(R.string.continue_text)
                binding.loading.visibility = View.GONE
                binding.continueButton.isEnabled = true
            }

            is AuthState.Loading -> {
                binding.continueButton.text = ""
                binding.loading.visibility = View.VISIBLE
                binding.continueButton.isEnabled = false
            }

            is AuthState.Error -> {
                binding.continueButton.text = ""
                binding.loading.visibility = View.VISIBLE
                binding.continueButton.isEnabled = false

                showDialog(
                    context = view.context,
                    layoutInflater = layoutInflater,
                    message = state.message
                ) {
                    if (state.loginRequired == true) {
                        viewModel.clearState()
                        navigateToMail(view)
                    }
                }
            }
        }
    }

    private fun navigateToMail(view: View) {
        val action = VerifyMailFragmentDirections.actionVerifyMailFragmentToMailFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun backButtonClicked() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}