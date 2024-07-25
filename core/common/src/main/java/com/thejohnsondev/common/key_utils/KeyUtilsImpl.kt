package com.thejohnsondev.common.key_utils

import com.thejohnsondev.common.PBKDFUtils
import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.PasswordModel
import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class KeyUtilsImpl : KeyUtils {

    override fun generateKeyFBKDF(file: File): ByteArray {
        val bytes = file.readBytes()
        val step = bytes.size / 64
        val firstSelection = mutableListOf<Byte>()
        for (i in 128..bytes.size step step) {
            firstSelection.add(bytes[i])
        }
        val result = mutableListOf<Byte>()
        val toNormalize = firstSelection.windowed(4, 4)
        toNormalize.forEach {
            result.add((it.sumOf { it.toInt() } / 4).toByte())
        }
        return result.toByteArray()
    }

    override fun generateKeyPBKDF(input: String): ByteArray {
        return PBKDFUtils.pbkdf2("HmacSHA256", input.toByteArray(), input.toByteArray(), 1000, 16)
    }

    override fun encrypt(
        input: String,
        key: ByteArray,
        iv: ByteArray?
    ): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(iv ?: key.sliceArray(0 until cipher.blockSize))
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
        val encrypted = cipher.doFinal(input.toByteArray())
        return java.util.Base64.getEncoder().encodeToString(encrypted)
    }

    override fun decrypt(
        input: String,
        key: ByteArray,
        iv: ByteArray?
    ): String {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val secretKey = SecretKeySpec(key, "AES")
        val ivParameterSpec = IvParameterSpec(iv ?: key.sliceArray(0 until cipher.blockSize))
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec)
        val plainText = cipher.doFinal(java.util.Base64.getDecoder().decode(input))
        return String(plainText)
    }

    override fun encryptAdditionalFieldModel(
        additionalField: AdditionalField,
        key: ByteArray
    ): AdditionalField {
        return additionalField.copy(
            title = encrypt(additionalField.title.trim(), key),
            value = encrypt(additionalField.value.trim(), key)
        )
    }

    override fun decryptAdditionalFieldModel(
        additionalField: AdditionalField,
        key: ByteArray
    ): AdditionalField {
        return additionalField.copy(
            title = decrypt(additionalField.title, key),
            value = decrypt(additionalField.value, key)
        )
    }

    override fun encryptPasswordModel(passwordModel: PasswordModel, key: ByteArray): PasswordModel {
        return passwordModel.copy(
            organization = encrypt(passwordModel.organization.trim(), key),
            organizationLogo = encrypt(passwordModel.organizationLogo?.trim().orEmpty(), key),
            title = encrypt(passwordModel.title.trim(), key),
            password = encrypt(passwordModel.password.trim(), key),
            additionalFields = passwordModel.additionalFields.map {
                encryptAdditionalFieldModel(
                    it,
                    key
                )
            }
        )
    }

    override fun decryptPasswordModel(passwordModel: PasswordModel, key: ByteArray): PasswordModel {
        return passwordModel.copy(
            organization = decrypt(passwordModel.organization, key),
            organizationLogo = decrypt(passwordModel.organizationLogo.orEmpty(), key),
            title = decrypt(passwordModel.title, key),
            password = decrypt(passwordModel.password, key),
            additionalFields = passwordModel.additionalFields.map {
                decryptAdditionalFieldModel(
                    it,
                    key
                )
            }
        )
    }

    override fun encryptNoteModel(noteModel: NoteModel, key: ByteArray): NoteModel {
        return noteModel.copy(
            title = encrypt(noteModel.title.trim(), key),
            description = encrypt(noteModel.description.trim(), key)
        )
    }

    override fun decryptNoteModel(noteModel: NoteModel, key: ByteArray): NoteModel {
        return noteModel.copy(
            title = decrypt(noteModel.title, key),
            description = decrypt(noteModel.description, key)
        )
    }

}