def call(Map config = [:]) {
    container('trivy') {
        echo "--- סריקת אבטחת רכיבים ותלויות עם Trivy ---"
        sh 'trivy fs --format json --output trivy-android-report.json .'
        sh 'trivy fs --exit-code 0 --severity HIGH,CRITICAL .'
    }
    
    // שמירת הדו"ח מחוץ לקונטיינר בברמת ה-Pipeline
    archiveArtifacts artifacts: 'trivy-android-report.json', allowEmptyArchive: true
}
