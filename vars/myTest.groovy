podTemplate(
    label: 'my-multi-pod',
    containers: [
        containerTemplate(
            name: 'ubuntu',
            image: 'ubuntu:latest',
            ttyEnabled: true,
            command: 'cat'
        ),
        containerTemplate(
            name: 'alpine',
            image: 'alpine:latest',
            ttyEnabled: true,
            command: 'cat'
        )
    ]
) {
    node('my-multi-pod') {

        stage('Hello Ubuntu') {
            container('ubuntu') {
                sh 'echo Hello World from Ubuntu'
            }
        }

        stage('Hello Alpine') {
            container('alpine') {
                sh 'echo Hello World from Alpine'
            }
        }

    }
}
