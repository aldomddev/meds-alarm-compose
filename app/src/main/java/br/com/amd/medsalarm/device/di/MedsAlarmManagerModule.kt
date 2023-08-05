package br.com.amd.medsalarm.device.di

import android.content.Context
import br.com.amd.medsalarm.device.PermissionCheckerImpl
import br.com.amd.medsalarm.device.MedsAlarmManagerImpl
import br.com.amd.medsalarm.domain.device.PermissionChecker
import br.com.amd.medsalarm.domain.device.MedsAlarmManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MedsAlarmManagerModule {

    @Binds
    @Singleton
    abstract fun bindMedsAlarmManager(impl: MedsAlarmManagerImpl): MedsAlarmManager

    @Binds
    @Singleton
    abstract fun bindAlarmPermission(impl: PermissionCheckerImpl): PermissionChecker
}