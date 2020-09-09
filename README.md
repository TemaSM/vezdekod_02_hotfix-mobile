### Основные фичи
* Тесты публичных методов самого приложения, без изменений в исходниках и логике поведения  
* Авторизация в GCP по переменной окружения или из файл-ключа сервисного аккаунта - [GoogleCloudPlatformHelper](app/src/test/java/com/travels/searchtravels/GoogleCloudPlatformHelper.kt)  
<small>(используется GitHub секрет `GOOGLE_APPLICATION_CREDENTIALS` или файл-ключ `app/src/test/resources/gcp-serviceaccount.json`)</small>
* Выполнение тестов (unit + instrumented, в виртуальной Android машине через QEMU) в облаке благодаря GitHub Actions  
* Автоматическая сборка apk файла в облаке CI GitHub Actions, с выходным артефактом и релизом на страницу репо  