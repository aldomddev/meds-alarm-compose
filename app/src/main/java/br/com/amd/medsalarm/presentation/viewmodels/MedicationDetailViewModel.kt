package br.com.amd.medsalarm.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amd.medsalarm.core.extentions.toLiveData
import br.com.amd.medsalarm.domain.interactors.SaveAlarmUseCase
import br.com.amd.medsalarm.domain.model.MedsAlarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    private val saveAlarmUseCase: SaveAlarmUseCase
) : ViewModel() {

    private var medication: String = ""
    private var description: String = ""
    private var startsOnDate: LocalDate? = null
    private var startsOnTime: LocalTime? = null
    private var endsOnDate: LocalDate? = null
    private var endsOnTime: LocalTime? = null

    private var isChoosingStartsOnDateTime = false
    private var isChoosingEndsOnDateTime = false

    private val _medicationText = MutableLiveData<String>()
    val medicationText = _medicationText.toLiveData()

    private val _descriptionText = MutableLiveData<String>()
    val descriptionText = _descriptionText.toLiveData()

    private val _showStartsOnDateTime = MutableLiveData<Boolean>()
    val showStartsOnDateTime = _showStartsOnDateTime.toLiveData()

    private val _startsOnDateTime = MutableLiveData<String>()
    val startsOnDateTime = _startsOnDateTime.toLiveData()

    private val _showEndsOnDateTime = MutableLiveData<Boolean>()
    val showEndsOnDateTime = _showEndsOnDateTime.toLiveData()

    private val _endsOnDateTime = MutableLiveData<String>()
    val endsOnDateTime = _endsOnDateTime.toLiveData()

    fun onMedicationTextChange(text: String) {
        medication = text.trim()
        _medicationText.value = medication
    }

    fun onDescriptionTextChange(text: String) {
        description = text.trim()
        _descriptionText.value = description
    }

    fun onStartsOnFocusChange(focused: Boolean) {
        isChoosingStartsOnDateTime = focused
        _showStartsOnDateTime.value = focused
    }

    fun onEndsOnFocusChange(focused: Boolean) {
        isChoosingEndsOnDateTime = focused
        _showEndsOnDateTime.value = focused
    }

    fun onDateTimeDialogShow() {
        _showStartsOnDateTime.value = false
        _showEndsOnDateTime.value = false
    }

    fun onDateChange(date: LocalDate?) {
        when {
            isChoosingStartsOnDateTime -> startsOnDate = date
            isChoosingEndsOnDateTime -> endsOnDate = date
        }

        println("AMD - onDateChange: $date, start=$isChoosingStartsOnDateTime, end=$isChoosingEndsOnDateTime")
    }

    fun onTimeChange(time: LocalTime?) {
        when {
            isChoosingStartsOnDateTime -> {
                startsOnTime = time
                formatAndShow()
            }
            isChoosingEndsOnDateTime -> {
                endsOnTime = time
                formatAndShow()
            }
        }

        println("AMD - onTimeChange: $time, start=$isChoosingStartsOnDateTime, end=$isChoosingEndsOnDateTime")
    }

    private fun formatAndShow() {
        when {
            isChoosingStartsOnDateTime -> formatAndShowStartsOnDateTime()
            isChoosingEndsOnDateTime -> formatAndShowEndsOnDateTime()
        }
    }

    private fun formatAndShowStartsOnDateTime() {
        if (startsOnDate != null && startsOnTime != null) {
            val startDate = startsOnDate!!
            val startTime = startsOnTime!!

            val localDateStr = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val localTimeStr = startTime.format(DateTimeFormatter.ISO_LOCAL_TIME)

            _startsOnDateTime.value = "$localDateStr - $localTimeStr"
        } else {
            _startsOnDateTime.value = ""
        }
    }

    private fun formatAndShowEndsOnDateTime() {
        if (endsOnDate != null && endsOnTime != null) {
            val endDate = endsOnDate!!
            val endTime = endsOnTime!!

            val localDateStr = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val localTimeStr = endTime.format(DateTimeFormatter.ISO_LOCAL_TIME)

            _endsOnDateTime.value = "$localDateStr - $localTimeStr"
        } else {
            _endsOnDateTime.value = ""
        }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm = MedsAlarm(
                medication = medication,
                description = description,
                startsOn = LocalDateTime.of(startsOnDate!!, startsOnTime!!)
            )
            val params = SaveAlarmUseCase.Params(alarm = alarm)
            saveAlarmUseCase(params)
        }
    }

    fun onCancelButtonClick() {

    }
}