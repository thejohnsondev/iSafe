package com.thejohnsondev.domain.di

import com.thejohnsondev.data.vault.PasswordsRepository
import com.thejohnsondev.domain.add_edit_password.CreatePasswordUseCase
import com.thejohnsondev.domain.add_edit_password.CreatePasswordUseCaseImpl
import com.thejohnsondev.domain.add_edit_password.UpdatePasswordUseCase
import com.thejohnsondev.domain.add_edit_password.UpdatePasswordUseCaseImpl
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

    @Singleton
    @Provides
    fun provideCreatePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): CreatePasswordUseCase = CreatePasswordUseCaseImpl(passwordsRepository)

    @Singleton
    @Provides
    fun provideUpdatePasswordUseCase(
        passwordsRepository: PasswordsRepository
    ): UpdatePasswordUseCase = UpdatePasswordUseCaseImpl(passwordsRepository)

}