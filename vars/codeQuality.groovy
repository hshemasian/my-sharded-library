def sonarCreateProject(String projectKey) {
    withSonarQubeEnv('SonarQube') { 
        sh "pwd"
    }
}

def sonarLocalScan() {
    def scannerHome = tool 'SonarQubeScanner' 
    
    withSonarQubeEnv('SonarQube') {
        sh "pwd"
    }
}
