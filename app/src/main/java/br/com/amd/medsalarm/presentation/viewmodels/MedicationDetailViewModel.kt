package br.com.amd.medsalarm.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amd.medsalarm.common.extentions.toState
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import br.com.amd.medsalarm.domain.device.PermissionChecker
import br.com.amd.medsalarm.domain.interactors.GetAlarmByIdUseCase
import br.com.amd.medsalarm.domain.interactors.SaveAlarmUseCase
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.domain.model.RepeatingInterval
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
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@HiltViewModel
class MedicationDetailViewModel @Inject constructor(
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    private val alarmManager: MedsAlarmManager,
    private val permissionChecker: PermissionChecker,
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

    private val _medicationText = mutableStateOf(value = "")
    val medicationText = _medicationText.toState()

    private val _descriptionText = mutableStateOf(value = "")
    val descriptionText = _descriptionText.toState()

    private val _showDatePickerDialog = mutableStateOf(value = false)
    val showDatePickerDialog = _showDatePickerDialog.toState()

    private val _startsOnDateTimeStr = mutableStateOf(value = "")
    val startsOnDateTimeStr = _startsOnDateTimeStr.toState()

    private val _showTimePickerDialog = mutableStateOf(value = false)
    val showTimePickerDialog = _showTimePickerDialog.toState()

    private val _endsOnDateTimeStr = mutableStateOf(value = "")
    val endsOnDateTimeStr = _endsOnDateTimeStr.toState()

    private val _endsOnDateTimeEnabled = mutableStateOf(value = false)
    val endsOnDateTimeEnabled = _endsOnDateTimeEnabled.toState()

    private val _repeatingInterval = mutableStateOf(value = RepeatingIntervalVO.EIGHT)
    val repeatingInterval = _repeatingInterval.toState()

    private val _customRepeatingInterval = mutableStateOf(value = "")
    val customRepeatingInterval = _customRepeatingInterval.toState()

    private val _alarmSaved = mutableStateOf(false)
    val alarmSaved = _alarmSaved.toState()

    var showExactAlarmDialog by mutableStateOf(false)

    var showNotificationDialog by mutableStateOf(false)

    init {
        hasPermissionsGranted()
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

    private fun hasPermissionsGranted(): Boolean {
        val exactAlarmPermissionGranted = permissionChecker.hasExactAlarmPermission()
        val notificationPermissionGranted = permissionChecker.hasNotificationPermission()
        showExactAlarmDialog = !exactAlarmPermissionGranted
        showNotificationDialog = exactAlarmPermissionGranted && !notificationPermissionGranted
        return exactAlarmPermissionGranted && notificationPermissionGranted
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
            LocalDateTime.of(startsOnDate, startsOnTime?.truncatedTo(ChronoUnit.MINUTES))
        } else {
            null
        }
    }

    private fun getEndsOnDateTime(): LocalDateTime? {
        return if (endsOnDate != null && endsOnTime != null) {
            LocalDateTime.of(endsOnDate, endsOnTime?.truncatedTo(ChronoUnit.MINUTES))
        } else {
            null
        }
    }

    fun onRepeatingIntervalChanged(value: RepeatingIntervalVO) {
        interval = value
        _repeatingInterval.value = value

        if (value == RepeatingIntervalVO.CUSTOM) {
            onCustomRepeatingIntervalChange(value = "24")
        }
    }

    fun onPermanentCheckBoxClick() {
        _endsOnDateTimeEnabled.value = !_endsOnDateTimeEnabled.value
    }

    fun onCustomRepeatingIntervalChange(value: String) {
        val filteredValue = value.filter { it.isDigit() }
        if (filteredValue.length <= 2) {
            _customRepeatingInterval.value = filteredValue
        }
    }

    fun onSaveButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            val alarm = MedsAlarm(
                id = alarmInEditionId,
                medication = medication.trim(),
                description = description.trim(),
                startsOn = getStartsOnDateTime(),
                endsOn = getEndsOnDateTime(),
                repeatingInterval = interval.toInt(),
                enabled = true
            )
            val nextAlarm = alarmManager.getNextAlarm(alarm)
            val result = saveAlarmUseCase(alarm.copy(next = nextAlarm))

            // TODO: show some message
            when {
                result.isSuccess -> {
                    alarmManager.set(alarm)
                    _alarmSaved.value = true
                }
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
                getAlarmByIdUseCase(medsAlarmId)
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

        val interval = RepeatingInterval.values().filter { it.interval == alarm.repeatingInterval }
        _repeatingInterval.value = if(interval.isNotEmpty()) {
            interval.first().toPresenter()
        } else {
            _customRepeatingInterval.value = alarm.repeatingInterval.toString()
            RepeatingIntervalVO.CUSTOM
        }
    }

    private fun RepeatingIntervalVO.toInt(): Int {
        return if (_repeatingInterval.value == RepeatingIntervalVO.CUSTOM) {
            _customRepeatingInterval.value.toInt()
        } else {
            _repeatingInterval.value.interval
        }
    }
}