package com.thejohnsondev.data

import android.content.Context
import android.net.Uri
import com.thejohnsondev.common.asFile
import com.thejohnsondev.common.awaitChannelFlow
import com.thejohnsondev.common.generateSecretKey
import com.thejohnsondev.common.sendOrNothing
import com.thejohnsondev.model.GenerateKeyError
import com.thejohnsondev.model.KeyGenerateResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GenerateKeyRepositoryImpl @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val applicationContext: Context
) : GenerateKeyRepository {
    override fun generateKey(fileUri: Uri?): Flow<KeyGenerateResult> = awaitChannelFlow {
        withContext(Dispatchers.Default) {
            if (fileUri == null) {
                coroutineScope.launch {
                    sendOrNothing(
                        KeyGenerateResult.Failure(
                            IllegalArgumentException(
                                GenerateKeyError.ERROR_FILE_EMPTY.name
                            )
                        )
                    )
                }
            }
            val keyFile = fileUri?.asFile(applicationContext)
            val generatedSecretKey = keyFile?.generateSecretKey()
            coroutineScope.launch {
                if (generatedSecretKey == null) {
                    sendOrNothing(
                        KeyGenerateResult.Failure(
                            IllegalArgumentException(
                                GenerateKeyError.ERROR_UNEXPECTED.name
                            )
                        )
                    )
                } else {
                    sendOrNothing(KeyGenerateResult.Success(generatedSecretKey))
                }
            }
        }
    }
}