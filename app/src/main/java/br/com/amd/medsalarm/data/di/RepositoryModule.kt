package br.com.amd.medsalarm.data.di

import br.com.amd.medsalarm.data.repository.MedsAlarmDataRepository
import br.com.amd.medsalarm.domain.repository.MedsAlarmRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMedsAlarmRepository(repositoryImpl: MedsAlarmDataRepository): MedsAlarmRepository
}