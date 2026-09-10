def call(Map config = [:]) {
    container('trivy') {
        echo "--- סריקת אבטחה עם Trivy ---"
        sh 'trivy fs --format json --output trivy-android-report.json .'
        sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL .'
    }
    archiveArtifacts artifacts: 'trivy-android-report.json', allowEmptyArchive: true
}
