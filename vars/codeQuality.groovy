def sonarCreateProject(String projectKey) {
        withSonarQubeEnv('SonarQubeScanner') {
            sh 
        }
}

def sonarLocalScan() {
    def scannerHome = tool 'SonarQubeScanner'
    withSonarQubeEnv('SonarQubeScanner') {
        sh
    }
}
