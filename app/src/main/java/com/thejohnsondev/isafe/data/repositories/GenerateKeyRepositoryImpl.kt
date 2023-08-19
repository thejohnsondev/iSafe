package com.thejohnsondev.isafe.data.repositories

import android.content.Context
import android.net.Uri
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.domain.models.KeyGenerateResult
import com.thejohnsondev.isafe.domain.repositories.GenerateKeyRepository
import com.thejohnsondev.isafe.utils.asFile
import com.thejohnsondev.isafe.utils.awaitChannelFlow
import com.thejohnsondev.isafe.utils.generateSecretKey
import com.thejohnsondev.isafe.utils.sendOrNothing
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
                                applicationContext.getString(R.string.uploaded_file_is_empty)
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
                                applicationContext.getString(R.string.unexpected_error_when_generating_the_key)
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