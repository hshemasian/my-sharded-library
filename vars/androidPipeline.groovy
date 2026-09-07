def call(Map config = [:]) {
    // הגדרת ערכי ברירת מחדל לפרמטרים
    def appName = config.get('appName', 'KidsAndroidApp')
    def sonarProjectKey = config.get('sonarProjectKey', 'kids-android-app')

    podTemplate(
        label: 'android-build-pod',
        containers: [
            // קונטיינר לבנייה והרצת טסטים לאנדרואיד (כולל Java, Gradle ו-Android SDK)
            containerTemplate(
                name: 'android-builder',
                image: 'mingc/android-build-box:latest',
                ttyEnabled: true,
                command: 'cat'
            ),
            // קונטיינר Trivy לסריקות אבטחה
            containerTemplate(
                name: 'trivy',
                image: 'aquasec/trivy:latest',
                ttyEnabled: true,
                command: 'cat'
            )
        ]
    ) {
        node('android-build-pod') {

            stage('Checkout') {
                container('android-builder') {
                    echo "--- מורידים את קוד המקור של האפליקציה ---"
                    checkout scm
                }
            }

            stage('Code Quality - Lint & Sonar') {
                container('android-builder') {
                    echo "--- הרצת בדיקת איכות קוד (Android Lint) ---"
                    dir('android') {
                        sh 'chmod +x gradlew'
                        sh './gradlew lint'
                    }
                }
            }

            stage('Security Scan - Trivy') {
                container('trivy') {
                    echo "--- סריקת אבטחת רכיבים ותלויות עם Trivy ---"
                    // סריקת קובצי הפרויקט, יצירת דו"ח JSON ושמירת הריצה
                    sh 'trivy fs --format json --output trivy-android-report.json .'
                    sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL .'
                }
                
                // שמירת דו"ח ה-JSON כ-Artifact בג'נקינס
                archiveArtifacts artifacts: 'trivy-android-report.json', allowEmptyArchive: true
            }

            stage('QA & Unit Tests') {
                container('android-builder') {
                    echo "--- הרצת בדיקות יחידה (Unit Tests) ---"
                    dir('android') {
                        sh 'chmod +x gradlew'
                        sh './gradlew test'
                    }
                }
                
                // איסוף והצגת תוצאות הבדיקות בממשק של Jenkins
                junit allowEmptyResults: true, testResults: '**/build/test-results/**/*.xml'
            }

            stage('Build APK') {
                container('android-builder') {
                    echo "--- קימפול האפליקציה ויצירת קובץ APK ---"
                    dir('android') {
                        sh 'chmod +x gradlew'
                        sh './gradlew assembleDebug'
                    }
                }
            }

            stage('Archive Artifacts') {
                echo "--- שמירת קובץ ה-APK להורדה ---"
                // שמירת קובץ ה-APK שנוצר כדי שתוכל להוריד אותו
                archiveArtifacts artifacts: '**/build/outputs/apk/debug/*.apk', allowEmptyArchive: false
            }
        }
    }
}
