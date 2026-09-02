def deployApp(String branch = 'main') {
    echo "Deploying application from branch: ${branch}"
}

def buildApp(String imageName) {
    echo "Building Docker image: ${imageName}"
    sh "docker build -t ${imageName} ."
}

def testApp() {
    echo 'Running tests...'
}

def deployToDockerHub() {
    echo "Deploying ${env.IMAGE_NAME} to Docker Hub..."
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'USER', passwordVariable: 'PAT')]) {
                    sh 'echo "$PAT" | docker login -u "$USER" --password-stdin'
                    sh "docker push ${env.IMAGE_NAME}"
                }
}

def cleanup() {
    echo "Cleaning up local Docker image..."
                sh "docker rmi ${env.IMAGE_NAME} || true"
}
