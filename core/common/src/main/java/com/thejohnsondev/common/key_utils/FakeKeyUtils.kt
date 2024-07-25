package com.thejohnsondev.common.key_utils

import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import java.io.File

class FakeKeyUtils: KeyUtils {
    override fun generateKeyFBKDF(file: File): ByteArray {
        return ByteArray(0)
    }

    override fun generateKeyPBKDF(input: String): ByteArray {
        return ByteArray(0)
    }

    override fun encrypt(input: String, key: ByteArray, iv: ByteArray?): String {
        return input
    }

    override fun decrypt(input: String, key: ByteArray, iv: ByteArray?): String {
        return input
    }

    override fun encryptAdditionalFieldModel(
        additionalField: AdditionalField,
        key: ByteArray
    ): AdditionalField {
        return additionalField
    }

    override fun decryptAdditionalFieldModel(
        additionalField: AdditionalField,
        key: ByteArray
    ): AdditionalField {
        return additionalField
    }

    override fun encryptPasswordModel(passwordModel: PasswordModel, key: ByteArray): PasswordModel {
        return passwordModel
    }

    override fun decryptPasswordModel(passwordModel: PasswordModel, key: ByteArray): PasswordModel {
        return passwordModel
    }

    override fun encryptNoteModel(noteModel: NoteModel, key: ByteArray): NoteModel {
        return noteModel
    }

    override fun decryptNoteModel(noteModel: NoteModel, key: ByteArray): NoteModel {
        return noteModel
    }
}