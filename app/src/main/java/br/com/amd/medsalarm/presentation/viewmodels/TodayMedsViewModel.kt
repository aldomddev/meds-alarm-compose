package br.com.amd.medsalarm.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amd.medsalarm.domain.interactors.DeleteAlarmUseCase
import br.com.amd.medsalarm.domain.interactors.GetAllAlarmsUseCase
import br.com.amd.medsalarm.domain.model.MedsAlarm
import br.com.amd.medsalarm.presentation.mappers.toPresenter
import br.com.amd.medsalarm.presentation.model.MedsAlarmVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Month
import javax.inject.Inject

@HiltViewModel
class TodayMedsViewModel @Inject constructor (
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase
) : ViewModel() {

    val viewState: Flow<ViewState> = flow {
        getAllAlarmsUseCase()
            .catch { exception ->
                println(exception)
                emit(ViewState.Error)
            }
            .collect { alarms ->
                if (alarms.isEmpty()) {
                    //emit(ViewState.Empty)
                    emit(ViewState.Loaded(dummyAlarms().toPresenter()))
                } else {
                    emit(ViewState.Loaded(alarms.toPresenter()))
                }
            }
    }

    fun removeAlarm(alarm: MedsAlarm) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteAlarmUseCase(DeleteAlarmUseCase.Params(alarm))
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

    sealed class ViewState {
        object Empty: ViewState()
        object Loading: ViewState()
        data class Loaded(val data: List<MedsAlarmVO>): ViewState()
        object Error: ViewState()
    }
}