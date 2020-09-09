package com.travels.searchtravels

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.vision.v1.model.LatLng
import com.travels.searchtravels.api.OnVisionApiListener
import com.travels.searchtravels.api.VisionApi
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.IOException
import java.lang.NullPointerException

@RunWith(AndroidJUnit4::class)
class GoogleVisionInstrumentedTest {
    // TODO: Можно просто пройтись по списку asset'ов и на осное названия файлов провести тест категорий

    @Test
    fun test_sea() {
        test("sea.jpg", "sea")
    }

    @Test
    fun test_beach() {
        test("beach.jpg", "beach")
    }

    @Test
    fun test_mountain() {
        test("mountain.jpg", "mountain")
    }

    @Test
    fun test_snow() {
        test("snow.jpg", "snow")
    }

    @Test
    fun test_ocean() {
        test("ocean.jpg", "ocean")
    }

    fun test(imagePathName: String, categoryToTest: String) {
        val googleCredential: GoogleCredential?
        // Пытаемся авторизоваться
        try {
            googleCredential = GoogleCloudPlatformHelper.authExplicit()
        }
        catch (e: NullPointerException) {
            // Тест провален, тк невозможно соединиться с GCP
            Assert.fail("GCP not authenticated")
            return
        }
        VisionApi.findLocation(getBitmapFromAsset(
            InstrumentationRegistry.getInstrumentation().context, // ApplicationProvider.getApplicationContext(),
            imagePathName // Путь-название файла изображения
        ),
            googleCredential?.accessToken, object : OnVisionApiListener {
                override fun onSuccess(latLng: LatLng?) {
                    Assert.assertTrue(true)
                }
                override fun onErrorPlace(category: String?) {
                    // Тестируем полученную категорию с ожидаемой
                    assertEquals(category, categoryToTest)
                }
                override fun onError() {
                    Assert.fail()
                }
            })
    }

    // Попытка реализовать подгрузку ресурсов из app/src/test/resources/*
    internal fun getBitmapFromResource(filePath: String?): Bitmap? {
        val file: File = File(this::class.java.classLoader?.getResource(filePath)?.getFile())
        // val assetManager: AssetManager = context.getAssets()
        var bitmap: Bitmap? = null
        // val istr: InputStream?
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        // options.inSampleSize = 500

        val data: ByteArray = file.readBytes()
        // bitmap = BitmapFactory.decodeStream(FileInputStream(file), null, options)
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
        // println(bitmap)
        return bitmap
    }

    // Подгрузка изображений из assets
    internal fun getBitmapFromAsset(context: Context, filePath: String?): Bitmap? {
        val assetManager: AssetManager = context.assets
        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        try {
            val data: ByteArray = assetManager.open(filePath!!).readBytes()
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
            // println(bitmap)
        } catch (e: IOException) {
            Assert.fail("Image not found: " + filePath)
        }
        return bitmap
    }
}
