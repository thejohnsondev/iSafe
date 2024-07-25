package com.thejohnsondev.common.key_utils

import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import java.io.File

interface KeyUtils {
    fun generateKeyFBKDF(file: File): ByteArray
    fun generateKeyPBKDF(input: String): ByteArray
    fun encrypt(input: String, key: ByteArray, iv: ByteArray? = null): String
    fun decrypt(input: String, key: ByteArray, iv: ByteArray? = null): String
    fun encryptAdditionalFieldModel(
        additionalField: AdditionalField,
        key: ByteArray
    ): AdditionalField

    fun decryptAdditionalFieldModel(
        additionalField: AdditionalField,
        key: ByteArray
    ): AdditionalField

    fun encryptPasswordModel(passwordModel: PasswordModel, key: ByteArray): PasswordModel
    fun decryptPasswordModel(passwordModel: PasswordModel, key: ByteArray): PasswordModel
    fun encryptNoteModel(noteModel: NoteModel, key: ByteArray): NoteModel
    fun decryptNoteModel(noteModel: NoteModel, key: ByteArray): NoteModel
}