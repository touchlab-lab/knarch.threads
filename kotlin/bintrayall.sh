set -e
./gradlew bintrayUpload -PdeployCommon=true
./gradlew bintrayUpload -PdeployIos=true
./gradlew bintrayUpload -PdeployAndroid=true
./gradlew bintrayUpload -PdeployLivedataIos=true
./gradlew bintrayUpload -PdeployLivedataCommon=true
./gradlew bintrayUpload -PdeployLivedataAndroid=true
./gradlew bintrayUpload -PdeployLivedataAndroidx=true
