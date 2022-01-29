package com.example.compose.jetsurvey.survey

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.theme.JetSurveyTheme
import com.example.compose.jetsurvey.theme.progressIndicatorBackground


@Composable
fun SurveyQuestionScreen(
    questions: SurveyState.Questions,
    shouldAskPermissions: Boolean,
    onDoNotAskForPermissions: () -> Unit,
    onAction: (Int, SurveyActionType) -> Unit,
    onDonePressed: () -> Unit,
    onBackPressed: () -> Unit,
    openSettings: () -> Unit,
) {
    val questionState = remember(questions.currentQuestionIndex) {
        questions.questionsState[questions.currentQuestionIndex]
    }

    Surface {
        Scaffold(
            topBar = {
                SurveyTopAppBar(
                    questionIndex = questionState.questionIndex,
                    totalQuestionsCount = questionState.totalQuestionsCount,
                    onBackPressed = onBackPressed
                )
            },
            content = { innerPadding ->
                Question(
                    question = questionState.question,
                    answer = questionState.answer,
                    shouldAskPermissions = shouldAskPermissions,
                    onAnswer = {
                        if (it !is Answer.PermissionsDenied) {
                            questionState.answer = it
                        }
                        questionState.enableNext = true
                    },
                    onAction = onAction,
                    openSettings = openSettings,
                    onDoNotAskForPermissions = onDoNotAskForPermissions,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            },

            bottomBar = {
                SurveyBottomBar(
                    questionState = questionState,
                    onPreviousPressed = { questions.currentQuestionIndex-- },
                    onNextPressed = { questions.currentQuestionIndex++ },
                    onDonePressed = onDonePressed
                )
            }
        )
    }
}

@Composable
fun SurveyTopAppBar(
    questionIndex: Int,
    totalQuestionsCount: Int,
    onBackPressed: () -> Unit,
) {

    Column {
        Box(modifier = Modifier.fillMaxWidth())
        {
            TopAppBarTitle(questionIndex = questionIndex,
                totalQuestionsCount = totalQuestionsCount,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .align(Alignment.Center))


            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                IconButton(
                    onClick = onBackPressed,
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close),
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

        }
        val animatedProgress by animateFloatAsState(
            targetValue = (questionIndex + 1) / totalQuestionsCount.toFloat(),
            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
        )
        LinearProgressIndicator(
            progress = animatedProgress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            backgroundColor = MaterialTheme.colors.progressIndicatorBackground)

    }

}

@Composable
fun SurveyBottomBar(
    questionState: QuestionState,
    onPreviousPressed: () -> Unit,
    onNextPressed: () -> Unit,
    onDonePressed: () -> Unit,
) {

    Row {
        if (questionState.showPrevious) {
            OutlinedButton(onClick = onPreviousPressed) {
                Text(text = stringResource(id = R.string.previous))
            }
        }

        if (questionState.showDone) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = stringResource(id = R.string.done))
            }
        } else {
            Button(onClick = onNextPressed) {
                Text(text = stringResource(id = R.string.next))
            }
        }
    }
}

@Composable
fun TopAppBarTitle(
    questionIndex: Int,
    totalQuestionsCount: Int,
    modifier: Modifier = Modifier,
) {
    val indexStyle = MaterialTheme.typography.caption.toSpanStyle().copy(
        fontWeight = FontWeight.Bold
    )
    val totalStyle = MaterialTheme.typography.caption.toSpanStyle()
    val text = buildAnnotatedString {
        withStyle(style = indexStyle) {
            append("${questionIndex + 1}")
        }
        withStyle(style = totalStyle) {
            append(stringResource(R.string.question_count, totalQuestionsCount))
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.caption,
        modifier = modifier
    )
}

@Preview
@Composable
fun PreviewSurveyScreen(
) {

    JetSurveyTheme {

    }

}