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
        stage('Build') {
            steps {
                withGradle {
                    sh './gradlew clean build build'
                }
                archiveArtifacts artifacts: 'spellbook/build/libs/*.jar', followSymlinks: false
            }
        }
        stage('Qodana') {
            steps {
                sh '''
                qodana
                '''
            }
        }
    }
}