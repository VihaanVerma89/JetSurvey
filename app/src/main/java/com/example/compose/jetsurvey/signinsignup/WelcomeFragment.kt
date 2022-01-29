package com.example.compose.jetsurvey.signinsignup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.compose.jetsurvey.Screen
import com.example.compose.jetsurvey.navigate
import com.example.compose.jetsurvey.theme.JetSurveyTheme

class WelcomeFragment : Fragment() {
    private val viewModel: WelcomeViewModel by viewModels { WelcomeViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        viewModel.navigateTo.observe(viewLifecycleOwner, { navigateToEvent ->
            navigateToEvent.getContentIfNotHandled()?.let { navigateTo ->
                navigate(navigateTo, Screen.Welcome)
            }
        })
        val composeView = ComposeView(requireContext())
        composeView.apply {
            setContent {
                JetSurveyTheme {
                    WelcomeScreen(onEvent = { event ->
                        when (event) {
                            is WelcomeEvent.SignInAsGuest -> viewModel.signInAsGuest()
                        }
                    })
                }
            }
        }
        return composeView
    }
}