def call(Map config = [:]) {
    container('android-builder') {
        echo "--- קימפול ה-APK ---"
        dir('android') {
            sh 'chmod +x gradlew'
            sh './gradlew assembleDebug'
        }
    }
    archiveArtifacts artifacts: '**/build/outputs/apk/debug/*.apk', allowEmptyArchive: false
}
