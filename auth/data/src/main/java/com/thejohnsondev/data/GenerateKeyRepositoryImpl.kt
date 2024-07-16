package com.thejohnsondev.data

import com.thejohnsondev.common.key_utils.KeyUtils
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenerateKeyRepositoryImpl @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val keyUtils: KeyUtils
) : GenerateKeyRepository {
    override fun generateKey(password: String): Flow<KeyGenerateResult> = awaitChannelFlow {
        withContext(Dispatchers.Default) {
            coroutineScope.launch {
                sendOrNothing(
                    KeyGenerateResult.Success(
                        keyUtils.generateKeyPBKDF(password)
                    )
                )
            }
        }
    }
}