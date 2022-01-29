package com.example.compose.jetsurvey.survey

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.theme.JetSurveyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@Preview()
@Composable
fun PreviewSurveyQuestion(

) {
    JetSurveyTheme(darkTheme = true) {
        QuestionTitle(title = R.string.pick_date)
    }
}

@Composable
fun QuestionContent(

) {

}

@Preview
@Composable
fun PreviewSingleChoiceQuestion(

) {
    JetSurveyTheme {
//        SingleChoiceQuestion()
    }
}

@Composable
private fun SingleChoiceQuestion(
    possibleAnswer: PossibleAnswer.SingleChoice,
    answer: Answer.SingleChoice?,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {

    val options = possibleAnswer.optionsStringRes.associateBy { stringResource(id = it) }

    val radioOptions = options.keys.toList()

    val selected = if (answer != null) {
        stringResource(id = answer.answer)
    } else {
        null
    }

    val (selectedOption, onOptionSelected) = remember(answer) { mutableStateOf(selected) }

    Column {

        radioOptions.forEach { text ->

            val onClickHandle = {
                onOptionSelected(text)
                options[text]?.let { onAnswerSelected(it) }
                Unit
            }

            val optionSelected = text == selectedOption

            val answerBorderColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }

            val answerBackgroundColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }

            SingleChoiceQuestionItem(answerBorderColor = answerBorderColor,
                optionSelected = optionSelected,
                onClickHandle = onClickHandle,
                answerBackgroundColor = answerBackgroundColor,
                text = text)
        }

    }
}

@Composable
fun SingleChoiceQuestionItem(
    answerBorderColor: Color,
    optionSelected: Boolean,
    onClickHandle: () -> Unit,
    answerBackgroundColor: Color,
    text: String,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            width = 1.dp,
            color = answerBorderColor
        ),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = optionSelected,
                    onClick = onClickHandle
                )
                .background(answerBackgroundColor)
                .padding(vertical = 16.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = text)
            RadioButton(selected = optionSelected,
                onClick = onClickHandle,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colors.primary
                )
            )
        }
    }
}


@Preview
@Composable
fun PreviewSingleChoiceQuestionItem(

) {

    JetSurveyTheme {

        val optionSelected = true

        val answerBorderColor = if (optionSelected) {
            MaterialTheme.colors.primary.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        }

        val answerBackgroundColor = if (optionSelected) {
            MaterialTheme.colors.primary.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
        }
        Column {


            SingleChoiceQuestionItem(answerBorderColor = answerBorderColor,
                optionSelected = optionSelected,
                onClickHandle = { /*TODO*/ },
                answerBackgroundColor = answerBackgroundColor,
                text = "testing ")

            SingleChoiceQuestionItem(answerBorderColor = answerBorderColor,
                optionSelected = false,
                onClickHandle = { /*TODO*/ },
                answerBackgroundColor = answerBackgroundColor,
                text = "testing 2 ")
        }
    }
}

@Composable
fun QuestionTitle(
    @StringRes title: Int,
) {
    val backgroundColor = if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.onSurface.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        )
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Question(
    question: Question,
    answer: Answer<*>?,
    shouldAskPermissions: Boolean,
    onAnswer: (Answer<*>) -> Unit,
    onAction: (Int, SurveyActionType) -> Unit,
    onDoNotAskForPermissions: () -> Unit,
    openSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    QuestionContent(question, answer, onAnswer, onAction, modifier)

}


@Composable
private fun QuestionContent(
    question: Question,
    answer: Answer<*>?,
    onAnswer: (Answer<*>) -> Unit,
    onAction: (Int, SurveyActionType) -> Unit,
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        contentPadding = PaddingValues(start=20.dp, end=20.dp)
    )
    {
        item {
            Spacer(modifier = Modifier.height(32.dp))
            QuestionTitle(title = question.questionText)
            Spacer(modifier = Modifier.height(24.dp))

            if (question.description != null) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(id = question.description),
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(bottom = 18.dp, start = 8.dp, end = 8.dp)
                    )
                }
            }

            SingleChoiceQuestion(
                possibleAnswer = question.answer as PossibleAnswer.SingleChoice,
                answer = answer as Answer.SingleChoice?,
                onAnswerSelected = { answer -> onAnswer(Answer.SingleChoice(answer)) },
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }


}

@Preview
@Composable
fun PreviewQuestion(

) {
    val question = Question(
        id = 2,
        questionText = R.string.pick_superhero,
        answer = PossibleAnswer.SingleChoice(
            optionsStringRes = listOf(
                R.string.spark,
                R.string.lenz,
                R.string.bugchaos,
                R.string.frag
            )
        ),
        description = R.string.select_one)

    JetSurveyTheme {
        Question(
            question = question,
            shouldAskPermissions = true,
            answer = null,
            onAnswer = {},
            onAction = { _, _ -> },
            onDoNotAskForPermissions = {},
            openSettings = {}
        )
    }
}