package br.com.amd.medsalarm.device.di

import android.content.Context
import br.com.amd.medsalarm.common.extentions.getAlarmManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DeviceModule {

    @Provides
    fun provideAlarmManager(@ApplicationContext context: Context) = context.getAlarmManager()
}