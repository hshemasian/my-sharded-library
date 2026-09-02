def sonarCreateProject(String projectKey) {
        withSonarQubeEnv('SonarQubeScanner') {
            sh "pwd"
        }
}

def sonarLocalScan() {
    def scannerHome = tool 'SonarQubeScanner'
    withSonarQubeEnv('SonarQubeScanner') {
        sh"pwd"
    }
}
