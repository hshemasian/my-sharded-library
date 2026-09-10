def call(Map config = [:]) {
    container('android-builder') {
        echo "--- הרצת בדיקות יחידה (Unit Tests) ---"
        dir('android') {
            sh 'chmod +x gradlew'
            sh './gradlew test'
        }
    }
    
    // איסוף תוצאות הטסטים
    junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
}
