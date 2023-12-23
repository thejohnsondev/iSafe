package com.thejohnsondev.domain.di

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.domain.DeletePasswordUseCase
import com.thejohnsondev.domain.DeletePasswordUseCaseImpl
import com.thejohnsondev.domain.GetAllPasswordsUseCase
import com.thejohnsondev.domain.GetAllPasswordsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object VaultDomainModule {

    @Singleton
    @Provides
    fun provideDeletePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): DeletePasswordUseCase = DeletePasswordUseCaseImpl(passwordsRepository)


    @Singleton
    @Provides
    fun provideGetAllPasswordsUseCase(
        passwordsRepository: PasswordsRepository
    ): GetAllPasswordsUseCase = GetAllPasswordsUseCaseImpl(passwordsRepository)

}