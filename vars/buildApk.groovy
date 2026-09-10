def call(Map config = [:]) {
    container('android-builder') {
        echo "--- קימפול האפליקציה ויצירת קובץ APK ---"
        dir('android') {
            sh 'chmod +x gradlew'
            sh './gradlew assembleDebug'
        }
    }
    
    // שמירת קובץ ה-APK
    archiveArtifacts artifacts: '**/build/outputs/apk/debug/*.apk', allowEmptyArchive: false
}
