def call(Map config = [:]) {
    def type = config.get('type', 'android')

    if (type == 'python') {
        echo '=== Running Code Quality & Syntax Check (Python) ==='
        sh 'flake8 app.py || python3 -m py_compile app.py'
    } else {
        // הקוד הקיים שלך עבור Android / Flutter / SonarQube
        def projectKey = config.get('sonarProjectKey', env.APP_NAME ?: 'android-app')

        container('android-builder') {
            echo "--- הרצת בדיקת איכות קוד (Flutter & Android Lint & SonarQube) ---"
            sh 'flutter pub get'

            dir('android') {
                sh 'chmod +x gradlew'
                sh './gradlew lint'

                withSonarQubeEnv('SonarQubeScanner') {
                    sh """
                        curl -s -u \$SONAR_AUTH_TOKEN: \
                        -X POST "\$SONAR_HOST_URL/api/projects/create" \
                        -d "project=${projectKey}&name=${projectKey}" || true
                    """
                    sh "./gradlew sonar -Dsonar.projectKey=${projectKey} -Dsonar.projectName=${projectKey}"
                }
            }
        }
    }
}