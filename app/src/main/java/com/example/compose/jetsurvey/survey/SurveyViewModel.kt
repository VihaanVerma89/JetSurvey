package com.example.compose.jetsurvey.survey

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

const val simpleDateFormatPattern = "EEE, MMM d"

class SurveyViewModel(
    private val surveyRepository: SurveyRepository,
    private val photoUriManager: PhotoUriManager,
) : ViewModel() {


    private val _uiState = MutableLiveData<SurveyState>()
    val uiState: LiveData<SurveyState>
        get() = _uiState


    private lateinit var surveyInitialState: SurveyState


    init {
        viewModelScope.launch {
            val survey = surveyRepository.getSurvey()

            // Create the default questions state based on the survey questions
            val questions: List<QuestionState> = survey.questions.mapIndexed { index, question ->
                val showPrevious = index > 0
                val showDone = index == survey.questions.size - 1
                QuestionState(
                    question = question,
                    questionIndex = index,
                    totalQuestionsCount = survey.questions.size,
                    showPrevious = showPrevious,
                    showDone = showDone
                )
            }
            surveyInitialState = SurveyState.Questions(survey.title, questions)
            _uiState.value = surveyInitialState
        }
    }

    fun computeResult(surveyQuestions: SurveyState.Questions) {
        val answers = surveyQuestions.questionsState.mapNotNull { it.answer }
        val result = surveyRepository.getSurveyResult(answers)
        _uiState.value = SurveyState.Result(surveyQuestions.surveyTitle, result)
    }

    private fun updateStateWithActionResult(questionId: Int, result: SurveyActionResult) {
        val latestState = _uiState.value
        if (latestState != null && latestState is SurveyState.Questions) {
            val question =
                latestState.questionsState.first { questionState ->
                    questionState.question.id == questionId
                }
            question.answer = Answer.Action(result)
            question.enableNext = true
        }
    }


    fun onDatePicked(questionId: Int, pickerSelection: Long?) {
        val selectedDate = Date().apply {
            time = pickerSelection ?: getCurrentDate(questionId)
        }
        val formattedDate =
            SimpleDateFormat(simpleDateFormatPattern, Locale.getDefault()).format(selectedDate)
        updateStateWithActionResult(questionId, SurveyActionResult.Date(formattedDate))
    }

    fun getCurrentDate(questionId: Int): Long {
        return getSelectedDate(questionId)
    }

    private fun getSelectedDate(questionId: Int): Long {
        val latestState = _uiState.value
        var ret = Date().time
        if (latestState != null && latestState is SurveyState.Questions) {
            val question =
                latestState.questionsState.first { questionState ->
                    questionState.question.id == questionId
                }
            val answer: Answer.Action? = question.answer as Answer.Action?
            if (answer != null && answer.result is SurveyActionResult.Date) {
                val formatter = SimpleDateFormat(simpleDateFormatPattern, Locale.ENGLISH)
                val formatted = formatter.parse(answer.result.date)
                if (formatted is Date)
                    ret = formatted.time
            }
        }
        return ret
    }
}

class SurveyViewModelFactory(
    private val photoUriManager: PhotoUriManager,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyViewModel::class.java)) {
            return SurveyViewModel(SurveyRepository, photoUriManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
