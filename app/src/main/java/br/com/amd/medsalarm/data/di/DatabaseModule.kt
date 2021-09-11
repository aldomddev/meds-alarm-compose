package br.com.amd.medsalarm.data.di

import android.content.Context
import androidx.room.Room
import br.com.amd.medsalarm.data.dao.MedsAlarmDao
import br.com.amd.medsalarm.data.database.MedsAlarmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMedsAlarmDatabase(@ApplicationContext context: Context): MedsAlarmDatabase {
        return Room.databaseBuilder(
            context,
            MedsAlarmDatabase::class.java,
            "meds_alarm.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMedsAlarmDao(database: MedsAlarmDatabase): MedsAlarmDao {
        return database.medsAlarmDao()
    }
}