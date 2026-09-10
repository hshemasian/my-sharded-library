def call(Map config = [:]) {
    container('android-builder') {
        echo "--- הרצת בדיקות יחידה ---"
        dir('android') {
            sh 'chmod +x gradlew'
            sh './gradlew test'
        }
    }
    junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
}
