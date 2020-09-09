#!/usr/bin/env bash

chmod +x ./gradlew
mkdir -p ./app/src/test/resources/
printenv GOOGLE_APPLICATION_CREDENTIALS > ./app/src/test/resources/gcp-serviceaccount.json

./gradlew app:connectedAndroidTest