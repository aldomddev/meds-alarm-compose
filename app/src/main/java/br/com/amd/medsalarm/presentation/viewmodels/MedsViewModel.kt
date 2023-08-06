package br.com.amd.medsalarm.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amd.medsalarm.domain.interactors.DeleteAlarmUseCase
import br.com.amd.medsalarm.domain.interactors.GetAllAlarmsUseCase
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.presentation.extensions.toTimeUntilNextAlarmFrom
import br.com.amd.medsalarm.presentation.mappers.toPresenter
import br.com.amd.medsalarm.presentation.model.MedsAlarmListState
import br.com.amd.medsalarm.presentation.model.MedsAlarmVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class MedsViewModel @Inject constructor(
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase
) : ViewModel() {

    val viewState = flow {
        getAllAlarmsUseCase()
            .catch { exception ->
                println(exception)
                emit(MedsAlarmListState.Error)
            }
            .collect { alarms ->
                if (alarms.isEmpty()) {
                    emit(MedsAlarmListState.Empty)
                } else {
                    val now = LocalDateTime.now()
                    val newAlarmsList = mutableListOf<MedsAlarmVO>()
                    alarms.forEach { alarm ->
                        newAlarmsList.add(alarm.toPresenter().copy(timeToNextAlarmVO = alarm.next.toTimeUntilNextAlarmFrom(fromDateTime = now)))
                    }
                    println("Stop!")
                    emit(MedsAlarmListState.Loaded(newAlarmsList))
                }
            }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        MedsAlarmListState.Loading
    )

    fun removeAlarm(alarm: MedsAlarm) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAlarmUseCase(alarm)
        }
    }

    private fun dummyAlarms(): List<MedsAlarm> {
        return listOf(
            MedsAlarm(
                id = 1,
                medication = "Tilenol",
                startsOn = LocalDateTime.of(2021, Month.AUGUST, 20, 10, 10)
            ),
            MedsAlarm(
                id = 2,
                medication = "Bromoprida",
                startsOn = LocalDateTime.of(2021, Month.AUGUST, 20, 8, 40)
            ),
            MedsAlarm(
                id = 2,
                medication = "Loratadina",
                startsOn = LocalDateTime.of(2021, Month.AUGUST, 20, 21, 45)
            )
        )
    }
}