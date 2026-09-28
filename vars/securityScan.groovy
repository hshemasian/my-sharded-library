def call(Object args = [:]) {
    if (args instanceof CharSequence) {
        def imageName = args.toString()
        echo "=== Running Trivy Security Scan on Docker Image: ${imageName} ==="
        sh "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image --severity HIGH,CRITICAL --exit-code 0 ${imageName} || true"
    } else {
        container('trivy') {
            echo "--- סריקת אבטחה עם Trivy (Filesystem) ---"
            sh 'trivy fs --format json --output trivy-android-report.json .'
            sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL .'
        }
        archiveArtifacts artifacts: 'trivy-android-report.json', allowEmptyArchive: true
    }
}
