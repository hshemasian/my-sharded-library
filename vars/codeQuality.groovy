def call(Map config = [:]) {
    def projectKey = config.get('sonarProjectKey', env.APP_NAME ?: 'android-app')

    container('android-builder') {
        echo "--- הרצת בדיקת איכות קוד (Flutter & Android Lint & SonarQube) ---"
        
        // 0. סנכרון תלויות Flutter משורש הפרויקט (חובה כדי ליצור את קובצי ה-plugin loader ש-Gradle דורש)
        sh 'flutter pub get'

        dir('android') {
            sh 'chmod +x gradlew'
            
            // 1. הרצת Android Lint
            sh './gradlew lint'

            // 2. הרצת סריקת SonarQube דרך Gradle עם הגדרות הסביבה מ-Jenkins
            withSonarQubeEnv('SonarQubeScanner') {
                // במידה ויש צורך ביצירת הפרויקט מראש דרך API:
                sh """
                    curl -s -u \$SONAR_AUTH_TOKEN: \
                    -X POST "\$SONAR_HOST_URL/api/projects/create" \
                    -d "project=${projectKey}&name=${projectKey}" || true
                """

                // הרצת הסריקה בפועל באמצעות ה-Gradle Wrapper
                sh "./gradlew sonar -Dsonar.projectKey=${projectKey} -Dsonar.projectName=${projectKey}"
            }
        }
    }
}
