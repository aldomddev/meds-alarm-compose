package br.com.amd.medsalarm.device.di

import br.com.amd.medsalarm.device.MedsAlarmManagerImpl
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MedsAlarmManagerModule {

    @Binds
    @Singleton
    abstract fun bindMedsAlarmManager(impl: MedsAlarmManagerImpl): MedsAlarmManager
}