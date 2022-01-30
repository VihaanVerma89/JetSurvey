package com.example.compose.jetsurvey.survey

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.theme.JetSurveyTheme

class SurveyFragment : Fragment() {

    private val viewModel: SurveyViewModel by viewModels {
        SurveyViewModelFactory(PhotoUriManager(requireContext().applicationContext))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
//            id = R.id.sign_in_fragment


            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            setContent {
                JetSurveyTheme {
                    viewModel.uiState.observeAsState().value?.let { surveyState ->
                        when (surveyState) {
                            is SurveyState.Questions -> SurveyQuestionScreen(
                                questions = surveyState,
                                shouldAskPermissions = false,
                                onAction = { id, action ->  },
                                onDoNotAskForPermissions = {},
                                onDonePressed = {

                                },
                                onBackPressed = {
                                    activity?.onBackPressedDispatcher?.onBackPressed()
                                },
                                openSettings = {
                                    activity?.startActivity(
                                        Intent(
                                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                            Uri.fromParts("package", context.packageName, null)
                                        )
                                    )
                                }
                            )
//                            is SurveyState.Result -> SurveyResultScreen(
//                                result = surveyState,
//                                onDonePressed = {
//                                    activity?.onBackPressedDispatcher?.onBackPressed()
//                                }
//                            )
                        }
                    }
                }
            }
        }
    }

}