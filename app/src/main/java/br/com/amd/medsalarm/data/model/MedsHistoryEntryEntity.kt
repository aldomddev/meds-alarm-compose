package br.com.amd.medsalarm.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "meds_history")
data class MedsHistoryEntryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @NonNull @ColumnInfo(name = "fired_on")
    val firedOn: LocalDateTime,
    @NonNull @ColumnInfo(name = "acknowledged")
    val acknowledged: Boolean,
    @NonNull @ColumnInfo(name = "acknowledged_on")
    val acknowledgedOn: LocalDateTime
)
