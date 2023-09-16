pipeline {
    environment {
        QODANA_TOKEN=credentials('spellbook-qodana-token')
    }
    agent {
        docker {
            args '''
              -v "${WORKSPACE}":/data/project
              --entrypoint=""
              '''
            image 'jetbrains/qodana-jvm-community'
        }
    }
    stages {
        stage('Qodana') {
            steps {
                sh '''
                qodana \
                --fail-threshold <number>
                '''
            }
        }
    }
}