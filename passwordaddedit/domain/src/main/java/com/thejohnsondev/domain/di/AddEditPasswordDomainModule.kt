package com.thejohnsondev.domain.di

import com.thejohnsondev.data.PasswordsRepository
import com.thejohnsondev.domain.CreatePasswordUseCase
import com.thejohnsondev.domain.UpdatePasswordUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AddEditPasswordDomainModule {

    @Singleton
    @Provides
    fun provideCreatePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): CreatePasswordUseCase = CreatePasswordUseCase(passwordsRepository)

    @Singleton
    @Provides
    fun provideUpdatePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): UpdatePasswordUseCase = UpdatePasswordUseCase(passwordsRepository)

}