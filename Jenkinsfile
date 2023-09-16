pipeline {
    environment {
        QODANA_TOKEN=credentials('spellbook-qodana-token')
        ossrhUsername="PinkLemonadeWizard"
        ossrhPassword=credentials('ossrh-plw-password')
        signing.keyId=credentials('ossrh-plw-signing-keyid')
        signing.password=credentials('ossrh-plw-signing-keypwd')
        signing.secretKeyRingFile=credentials('ossrh-plw-signing-keyringfile')
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
                    sh './gradlew clean build'
                }
                archiveArtifacts artifacts: 'spellbook/build/libs/*.jar', fingerprint: true, followSymlinks: false, onlyIfSuccessful: true
            }
        }
        stage('Qodana') {
            steps {
                sh '''
                qodana \
                --fail-threshold 1 \
                --project-dir "${WORKSPACE}"
                '''
            }
        }
        stage('Deploy') {
            withGradle {
                sh './gradlew publish'
            }
        }
    }
}