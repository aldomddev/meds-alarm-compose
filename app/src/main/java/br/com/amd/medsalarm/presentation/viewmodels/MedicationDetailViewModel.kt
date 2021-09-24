package br.com.amd.medsalarm.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amd.medsalarm.core.extentions.toLiveData
import br.com.amd.medsalarm.domain.interactors.GetAlarmByIdUseCase
import br.com.amd.medsalarm.domain.interactors.SaveAlarmUseCase
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.presentation.mappers.toDomain
import br.com.amd.medsalarm.presentation.mappers.toPresenter
import br.com.amd.medsalarm.presentation.model.RepeatingIntervalVO
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
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var alarmInEditionId = 0
    private var medication: String = ""
    private var description: String = ""
    private var startsOnDate: LocalDate? = null
    private var startsOnTime: LocalTime? = null
    private var endsOnDate: LocalDate? = null
    private var endsOnTime: LocalTime? = null
    private var interval = RepeatingIntervalVO.EIGHT

    private var isChoosingStartsOnDateTime = false
    private var isChoosingEndsOnDateTime = false

    private val _medicationText = MutableLiveData<String>()
    val medicationText = _medicationText.toLiveData()

    private val _descriptionText = MutableLiveData<String>()
    val descriptionText = _descriptionText.toLiveData()

    private val _showDatePickerDialog = MutableLiveData<Boolean>()
    val showDatePickerDialog = _showDatePickerDialog.toLiveData()

    private val _startsOnDateTimeStr = MutableLiveData<String>()
    val startsOnDateTimeStr = _startsOnDateTimeStr.toLiveData()

    private val _showTimePickerDialog = MutableLiveData<Boolean>()
    val showTimePickerDialog = _showTimePickerDialog.toLiveData()

    private val _endsOnDateTimeStr = MutableLiveData<String>()
    val endsOnDateTimeStr = _endsOnDateTimeStr.toLiveData()

    private val _endsOnDateTimeEnabled = MutableLiveData<Boolean>()
    val endsOnDateTimeEnabled = _endsOnDateTimeEnabled.toLiveData()

    private val _repeatingInterval = MutableLiveData<RepeatingIntervalVO>()
    val repeatingInterval = _repeatingInterval.toLiveData()

    init {
        loadAlarmDataForEdition(medsAlarmId = savedStateHandle.get<Int>("id") ?: 0)
    }

    fun onMedicationTextChange(text: String) {
        medication = text
        _medicationText.value = medication
    }

    fun onDescriptionTextChange(text: String) {
        description = text
        _descriptionText.value = description
    }

    fun onStartsOnFocusChange(focused: Boolean) {
        resetEndsDateTimeControl()
        isChoosingStartsOnDateTime = focused
        _showDatePickerDialog.value = focused
    }

    private fun resetStartsOnDateTimeControl() {
        isChoosingStartsOnDateTime = false
    }

    fun onEndsOnFocusChange(focused: Boolean) {
        resetStartsOnDateTimeControl()
        isChoosingEndsOnDateTime = focused
        _showDatePickerDialog.value = focused
    }

    private fun resetEndsDateTimeControl() {
        isChoosingEndsOnDateTime = false
    }

    fun onDateChange(date: LocalDate?) {
        when {
            isChoosingStartsOnDateTime -> startsOnDate = date
            isChoosingEndsOnDateTime -> endsOnDate = date
        }
        _showDatePickerDialog.value = false
        _showTimePickerDialog.value = date != null

        println("AMD - onDateChange: $date, start=$isChoosingStartsOnDateTime, end=$isChoosingEndsOnDateTime")
    }

    fun onTimeChange(time: LocalTime?) {
        _showTimePickerDialog.value = false

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

        resetStartsOnDateTimeControl()
        resetEndsDateTimeControl()
    }

    private fun formatAndShowStartsOnDateTime() {
        if (startsOnDate != null && startsOnTime != null) {
            val startDate = startsOnDate!!
            val startTime = startsOnTime!!

            val localDateStr = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val localTimeStr = startTime.format(DateTimeFormatter.ISO_LOCAL_TIME)

            _startsOnDateTimeStr.value = "$localDateStr - $localTimeStr"
        } else {
            _startsOnDateTimeStr.value = ""
        }
    }

    private fun formatAndShowEndsOnDateTime() {
        if (endsOnDate != null && endsOnTime != null) {
            val endDate = endsOnDate!!
            val endTime = endsOnTime!!

            val localDateStr = endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            val localTimeStr = endTime.format(DateTimeFormatter.ISO_LOCAL_TIME)

            _endsOnDateTimeStr.value = "$localDateStr - $localTimeStr"
        } else {
            _endsOnDateTimeStr.value = ""
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

    fun onRepeatingIntervalChanged(value: RepeatingIntervalVO) {
        interval = value
        _repeatingInterval.value = value
    }

    fun onSaveButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm = MedsAlarm(
                id = alarmInEditionId,
                medication = medication.trim(),
                description = description.trim(),
                startsOn = getStartsOnDateTime(),
                endsOn = getEndsOnDateTime(),
                repeatingInterval = interval.toDomain()
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
        // TODO
    }

    private fun loadAlarmDataForEdition(medsAlarmId: Int) {
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
            loadAlarmDataForEdition(alarm = MedsAlarm(medication = ""))
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

        _repeatingInterval.value = alarm.repeatingInterval.toPresenter()
    }
}