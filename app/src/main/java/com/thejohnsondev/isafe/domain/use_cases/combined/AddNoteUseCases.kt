package com.thejohnsondev.isafe.domain.use_cases.combined

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import javax.inject.Inject

data class AddNoteUseCases @Inject constructor(
    val dataStore: DataStore
)