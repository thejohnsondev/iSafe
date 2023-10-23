package com.thejohnsondev.domain.vault.di

import com.thejohnsondev.data.vault.PasswordsRepository
import com.thejohnsondev.domain.vault.DeletePasswordUseCase
import com.thejohnsondev.domain.vault.DeletePasswordUseCaseImpl
import com.thejohnsondev.domain.vault.GetAllPasswordsUseCase
import com.thejohnsondev.domain.vault.GetAllPasswordsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

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