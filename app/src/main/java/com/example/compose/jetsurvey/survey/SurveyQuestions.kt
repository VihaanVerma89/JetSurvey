package com.example.compose.jetsurvey.survey

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.jetsurvey.R
import com.example.compose.jetsurvey.theme.JetSurveyTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import java.text.SimpleDateFormat
import java.util.*


@Preview()
@Composable
fun PreviewSurveyQuestion(

) {
    JetSurveyTheme(darkTheme = true) {
        QuestionTitle(title = R.string.pick_date)
    }
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
private fun MultipleChoiceIconQuestion(
    possibleAnswer: PossibleAnswer.MultipleChoiceIcon,
    answer: Answer.MultipleChoice?,
    onAnswerSelected: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = possibleAnswer.optionsStringIconRes.associateBy { stringResource(id = it.second) }
    Column(modifier = modifier) {
        for (option in options) {
            var checkedState by remember(answer) {
                val selectedOption = answer?.answersStringRes?.contains(option.value.second)
                mutableStateOf(selectedOption ?: false)
            }
            val answerBorderColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }
            val answerBackgroundColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                checkedState = !checkedState
                                onAnswerSelected(option.value.second, checkedState)
                            }
                        )
                        .background(answerBackgroundColor)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = option.value.first),
                        contentDescription = null,
                        modifier = Modifier
                            .width(56.dp)
                            .height(56.dp)
                            .clip(MaterialTheme.shapes.medium)
                    )
                    Text(text = option.key)

                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { selected ->
                            checkedState = selected
                            onAnswerSelected(option.value.second, selected)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary
                        ),
                    )
                }
            }
        }
    }
}

@Composable
private fun MultipleChoiceQuestion(
    possibleAnswer: PossibleAnswer.MultipleChoice,
    answer: Answer.MultipleChoice?,
    onAnswerSelected: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = possibleAnswer.optionsStringRes.associateBy { stringResource(id = it) }
    Column(modifier = modifier) {
        for (option in options) {
            var checkedState by remember(answer) {
                val selectedOption = answer?.answersStringRes?.contains(option.value)
                mutableStateOf(selectedOption ?: false)
            }
            val answerBorderColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }
            val answerBackgroundColor = if (checkedState) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }
            Surface(
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = answerBorderColor
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(answerBackgroundColor)
                        .clickable(
                            onClick = {
                                checkedState = !checkedState
                                onAnswerSelected(option.value, checkedState)
                            }
                        )
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = option.key)

                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { selected ->
                            checkedState = selected
                            onAnswerSelected(option.value, selected)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colors.primary
                        ),
                    )
                }
            }
        }
    }
}


@Composable
private fun SingleChoiceIconQuestion(
    possibleAnswer: PossibleAnswer.SingleChoiceIcon,
    answer: Answer.SingleChoice?,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val options = possibleAnswer.optionsStringIconRes.associateBy { stringResource(id = it.second) }

    val radioOptions = options.keys.toList()

    val selected = if (answer != null) {
        stringResource(id = answer.answer)
    } else {
        null
    }

    val (selectedOption, onOptionSelected) = remember(answer) { mutableStateOf(selected) }

    Column(modifier = modifier) {
        radioOptions.forEach { text ->
            val onClickHandle = {
                onOptionSelected(text)
                options[text]?.let { onAnswerSelected(it.second) }
                Unit
            }
            val optionSelected = text == selectedOption
            val answerBorderColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.5f)
            } else {
                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            }
            val answerBackgroundColor = if (optionSelected) {
                MaterialTheme.colors.primary.copy(alpha = 0.12f)
            } else {
                MaterialTheme.colors.background
            }
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    options[text]?.let {
                        Image(
                            painter = painterResource(
                                id = it.first
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .width(56.dp)
                                .height(56.dp)
                                .clip(MaterialTheme.shapes.medium)
                        )
                    }
                    Text(
                        text = text
                    )

                    RadioButton(
                        selected = optionSelected,
                        onClick = onClickHandle,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.primary
                        )
                    )
                }
            }
        }
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
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp)
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


            when (question.answer) {
                is PossibleAnswer.SingleChoice -> SingleChoiceQuestion(
                    possibleAnswer = question.answer,
                    answer = answer as Answer.SingleChoice?,
                    onAnswerSelected = { answer -> onAnswer(Answer.SingleChoice(answer)) },
                    modifier = Modifier.fillParentMaxWidth()
                )
                is PossibleAnswer.SingleChoiceIcon -> SingleChoiceIconQuestion(
                    possibleAnswer = question.answer,
                    answer = answer as Answer.SingleChoice?,
                    onAnswerSelected = { answer -> onAnswer(Answer.SingleChoice(answer)) },
                    modifier = Modifier.fillMaxWidth()
                )
                is PossibleAnswer.MultipleChoice -> MultipleChoiceQuestion(
                    possibleAnswer = question.answer,
                    answer = answer as Answer.MultipleChoice?,
                    onAnswerSelected = { newAnswer, selected ->
                        // create the answer if it doesn't exist or
                        // update it based on the user's selection
                        if (answer == null) {
                            onAnswer(Answer.MultipleChoice(setOf(newAnswer)))
                        } else {
                            onAnswer(answer.withAnswerSelected(newAnswer, selected))
                        }
                    },
                    modifier = Modifier.fillParentMaxWidth()
                )
                is PossibleAnswer.MultipleChoiceIcon -> MultipleChoiceIconQuestion(
                    possibleAnswer = question.answer,
                    answer = answer as Answer.MultipleChoice?,
                    onAnswerSelected = { newAnswer, selected ->
                        // create the answer if it doesn't exist or
                        // update it based on the user's selection
                        if (answer == null) {
                            onAnswer(Answer.MultipleChoice(setOf(newAnswer)))
                        } else {
                            onAnswer(answer.withAnswerSelected(newAnswer, selected))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                is PossibleAnswer.Action -> ActionQuestion(
                    questionId = question.id,
                    possibleAnswer = question.answer,
                    answer = answer as Answer.Action?,
                    onAction = onAction,
                    modifier = Modifier.fillParentMaxWidth()
                )
                is PossibleAnswer.Slider -> SliderQuestion(
                    possibleAnswer = question.answer,
                    answer = answer as Answer.Slider?,
                    onAnswerSelected = { onAnswer(Answer.Slider(it)) },
                    modifier = Modifier.fillParentMaxWidth()
                )
                else -> {
                    Text(text = "unsupported")
                }
            }
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


@Composable
private fun ActionQuestion(
    questionId: Int,
    possibleAnswer: PossibleAnswer.Action,
    answer: Answer.Action?,
    onAction: (Int, SurveyActionType) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (possibleAnswer.actionType) {
        SurveyActionType.PICK_DATE -> {
            DateQuestion(
                questionId = questionId,
                answer = answer,
                onAction = onAction,
                modifier = modifier
            )
        }
        SurveyActionType.TAKE_PHOTO -> {
//            PhotoQuestion(
//                questionId = questionId,
//                answer = answer,
//                onAction = onAction,
//                modifier = modifier
//            )
        }
        SurveyActionType.SELECT_CONTACT -> TODO()
    }
}

@Composable
fun DateQuestion(
    questionId: Int,
    answer: Answer.Action?,
    onAction: (Int, SurveyActionType) -> Unit,
    modifier: Modifier = Modifier,
) {

    val date = if (answer != null && answer.result is SurveyActionResult.Date) {
        answer.result.date
    } else {
        SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault()).format(Date())
    }

    Button(
        onClick = { onAction(questionId, SurveyActionType.PICK_DATE) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.onPrimary,
            contentColor = MaterialTheme.colors.onSecondary
        ),
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(vertical = 20.dp)
            .height(54.dp),
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
    ) {
        Text(
            text = date,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Icon(
            imageVector = Icons.Filled.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
        )
    }
}

@Preview
@Composable
fun PreviewDateQuestion(

) {

    JetSurveyTheme {
    }
}
@Composable
fun SliderQuestion(
    possibleAnswer: PossibleAnswer.Slider,
    answer: Answer.Slider?,
    onAnswerSelected: (Float) -> Unit,
    modifier: Modifier = Modifier
) {

    var sliderPosition by remember {
        mutableStateOf(answer?.answerValue ?: possibleAnswer.defaultValue)
    }


    Row(modifier = modifier) {

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onAnswerSelected(it)
            },
            valueRange = possibleAnswer.range,
            steps = possibleAnswer.steps,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
        )
    }

    Row {
        Text(
            text = stringResource(id = possibleAnswer.startText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Text(
            text = stringResource(id = possibleAnswer.neutralText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
        Text(
            text = stringResource(id = possibleAnswer.endText),
            style = MaterialTheme.typography.caption,
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.8f)
        )
    }
}