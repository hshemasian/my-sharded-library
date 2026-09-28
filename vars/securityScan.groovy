def call(Object args = [:]) {
    if (args instanceof String) {
        // אם מעבירים שם של Docker Image (כמו בפרויקט פייתון)
        echo "=== Running Trivy Security Scan on Docker Image: ${args} ==="
        sh "trivy image --severity HIGH,CRITICAL --exit-code 0 ${args} || true"
    } else {
        // הקוד הקיים שלך עבור סריקת פרויקט Android
        container('trivy') {
            echo "--- סריקת אבטחה עם Trivy ---"
            sh 'trivy fs --format json --output trivy-android-report.json .'
            sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL .'
        }
        archiveArtifacts artifacts: 'trivy-android-report.json', allowEmptyArchive: true
    }
}