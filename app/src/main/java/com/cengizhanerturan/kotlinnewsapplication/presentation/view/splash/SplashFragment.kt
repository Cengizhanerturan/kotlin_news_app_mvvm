package com.cengizhanerturan.kotlinnewsapplication.presentation.view.splash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.cengizhanerturan.kotlinnewsapplication.R
import com.cengizhanerturan.kotlinnewsapplication.presentation.view.auth.AuthViewModel
import com.cengizhanerturan.kotlinnewsapplication.core.util.SplashType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private val viewModel: SplashViewModel by viewModels()
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData(view)
    }

    private fun observeData(view: View) = viewModel.state.observe(viewLifecycleOwner) { state ->
        when (state) {
            is SplashState.Success -> {
                when (state.type) {
                    SplashType.LOGGED_IN -> {
                        navigateHome(view)
                    }

                    SplashType.LOGGED_OUT -> {
                        navigateAuth(view)
                    }

                    SplashType.UNVERIFIED_EMAIL -> {
                        authViewModel.isUnverifiedEmailAfterUpdate = true
                        navigateToVerifyMail(view)
                    }

                    else -> {
                        //! First Entry
                    }
                }
            }

            is SplashState.Loading -> {}
            is SplashState.Error -> {}
        }
    }

    private fun navigateToVerifyMail(view: View) {
        val action = SplashFragmentDirections.actionSplashFragmentToVerifyMailFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun navigateHome(view: View) {
        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun navigateAuth(view: View) {
        val action = SplashFragmentDirections.actionSplashFragmentToMailFragment()
        Navigation.findNavController(view).navigate(action)
    }
}