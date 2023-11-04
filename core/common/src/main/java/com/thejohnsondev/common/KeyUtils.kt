package com.thejohnsondev.common

import java.io.File
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun File.generateSecretKey(): ByteArray {
    val bytes = readBytes()
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

fun String.encrypt(key: ByteArray): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encrypted = cipher.doFinal(toByteArray())
    return java.util.Base64.getEncoder().encodeToString(encrypted)
}

fun String.decrypt(key: ByteArray): String {
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val plainText = cipher.doFinal(java.util.Base64.getDecoder().decode(this))
    return String(plainText)
}