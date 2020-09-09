package com.travels.searchtravels

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.services.AbstractGoogleClient
import com.google.common.collect.Lists
import java.io.IOException
import java.io.InputStream

// Помощник по GCP
class GoogleCloudPlatformHelper {
    companion object {
        // Для аутентификации/авторизации в GCP через сервисный аккаунт
        @Throws(IOException::class)
        fun authExplicit(): GoogleCredential {
            val googleCredential: GoogleCredential
            // Пытаемся авторизоваться через переменную окружения GOOGLE_APPLICATION_CREDENTIALS
            googleCredential = try {
                GoogleCredential.getApplicationDefault()
            } catch (ex: IOException) {
                // Делаем fallback на локальный файл из ресурсов
                val jsonFile: InputStream? = this::class.java.classLoader?.getResourceAsStream("./gcp-serviceaccount.json")
                GoogleCredential.fromStream(jsonFile).createScoped(
                    Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform")
                )
            }
            // Обновляем (получаем) токен при помощи которого дальше будем коммуницировать с GCP
            googleCredential.refreshToken()
            println("GCP.serviceAccountId: " + googleCredential.serviceAccountId)
            println("GCP.accessToken: " + googleCredential.accessToken)
            // Возвращаем объект с данными для коммуникации с GCP
            return googleCredential
        }
    }
}
