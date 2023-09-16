pipeline {
    environment {
        QODANA_TOKEN=credentials('spellbook-qodana-token')
        ossrhPassword=credentials('ossrh-plw-password')
        ossrhSignKeyFile=credentials('ossrh-plw-signing-keyringfile')
    }

    agent any

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
            stages {
                stage('Preparations') {
                    steps {
                        sh 'mkdir -p cache/'
                        sh 'chown jenkins:jenkins cache'
                    }
                }
                stage('Run') {
                    agent {
                        docker {
                            args '''-v "${WORKSPACE}":/data/project/ -v "${WORKSPACE}/cache":/data/cache/ --entrypoint=""'''
                            image 'jetbrains/qodana-jvm-community'
                        }
                    }
                    steps {
                        sh '''
                        qodana \
                        --fail-threshold 1 \
                        --property=project.open.type=Gradle \
                        --project-dir /data/project/ \
                        --source-dir spellbook/src/main/java/ \
                        --baseline /data/project/qodana.sarif.json
                        '''
                    }
                }
            }
        }
        stage('Deploy') {
            when { anyOf { branch 'main' } }
            steps {
                withGradle {
                    sh '''
                    ./gradlew publish \
                    -PossrhUsername=PinkLemonadeWizard \
                    -PossrhPassword=${ossrhPassword} \
                    -Psigning.keyId=BBCE4E01 \
                    -Psigning.password=${ossrhPassword} \
                    -Psigning.secretKeyRingFile=${ossrhSignKeyFile}
                    '''
                }
            }
        }
    }
}
