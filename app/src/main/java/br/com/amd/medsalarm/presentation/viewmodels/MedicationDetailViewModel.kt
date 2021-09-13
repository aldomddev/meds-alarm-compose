package br.com.amd.medsalarm.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amd.medsalarm.core.extentions.toLiveData
import br.com.amd.medsalarm.domain.interactors.GetAlarmByIdUseCase
import br.com.amd.medsalarm.domain.interactors.SaveAlarmUseCase
import br.com.amd.medsalarm.domain.model.MedsAlarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase
) : ViewModel() {

    private var alarmInEditionId = 0
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

    private val _endsOnDateTimeEnabled = MutableLiveData<Boolean>()
    val endsOnDateTimeEnabled = _endsOnDateTimeEnabled.toLiveData()

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

    private fun getStartsOnDateTime(): LocalDateTime? {
        return if (startsOnDate != null && startsOnTime != null) {
            LocalDateTime.of(startsOnDate, startsOnTime)
        } else {
            null
        }
    }

    private fun getEndsOnDateTime(): LocalDateTime? {
        return if (endsOnDate != null && endsOnTime != null) {
            LocalDateTime.of(endsOnDate, endsOnTime)
        } else {
            null
        }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm = MedsAlarm(
                id = alarmInEditionId,
                medication = medication,
                description = description,
                startsOn = getStartsOnDateTime(),
                endsOn = getEndsOnDateTime()
            )
            val params = SaveAlarmUseCase.Params(alarm = alarm)
            val result = saveAlarmUseCase(params)

            // TODO: show some message
            when {
                result.isSuccess -> {}
                else -> {
                    println("${result.exceptionOrNull()}")
                }
            }
        }
    }

    fun onCancelButtonClick() {

    }

    fun loadAlarmDataForEdition(medsAlarmId: Int) {
        if (medsAlarmId > 0) {
            alarmInEditionId = medsAlarmId
            viewModelScope.launch(Dispatchers.IO) {
                val params = GetAlarmByIdUseCase.Params(medsAlarmId)
                getAlarmByIdUseCase(params)
                    .onSuccess { alarm ->
                        withContext(Dispatchers.Main) {
                            loadAlarmDataForEdition(alarm)
                        }
                    }
                    .onFailure {
                        println("error: $it")
                    }
            }
        } else {
            alarmInEditionId = 0
        }
    }

    private fun loadAlarmDataForEdition(alarm: MedsAlarm) {
        onMedicationTextChange(text = alarm.medication)
        onDescriptionTextChange(text = alarm.description)

        isChoosingStartsOnDateTime = true
        isChoosingEndsOnDateTime = false
        onDateChange(date = alarm.startsOn?.toLocalDate())
        onTimeChange(time = alarm.startsOn?.toLocalTime())

        isChoosingStartsOnDateTime = false
        val endDate = alarm.endsOn?.toLocalDate()
        val endTime = alarm.endsOn?.toLocalTime()
        _endsOnDateTimeEnabled.value = endDate == null && endTime == null
        isChoosingEndsOnDateTime = true
        onDateChange(date = endDate)
        onTimeChange(time = endTime)
        isChoosingEndsOnDateTime = false
    }
}