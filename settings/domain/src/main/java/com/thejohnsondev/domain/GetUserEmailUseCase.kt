package com.thejohnsondev.domain

import com.thejohnsondev.datastore.DataStore
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(
    private val dataStore: DataStore
) {

    operator fun invoke(): String = dataStore.getUserEmail()

}