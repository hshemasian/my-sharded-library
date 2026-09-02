def sonarCreateProject(String projectKey) {
    withSonarQubeEnv('SonarQubeScanner') {
        sh """
            curl -s -u \$SONAR_AUTH_TOKEN: \
            -X POST "\$SONAR_HOST_URL/api/projects/create" \
            -d "project=${projectKey}&name=${projectKey}" || true
        """
    }
}

def sonarLocalScan(String projectKey = env.APP_NAME) {
    def scannerHome = tool 'SonarQubeScanner'
    withSonarQubeEnv('SonarQubeScanner') {
        sh """
            ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectKey=${projectKey} \
            -Dsonar.projectName=${projectKey} \
            -Dsonar.sources=. \
            -Dsonar.sourceEncoding=UTF-8
        """
    }
}
