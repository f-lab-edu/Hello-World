pipeline {
    agent any

    tools {
        maven 'Maven 3.6.3'
    }

    stages{
        stage('Poll') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }

        stage('Unit Tests') {
            steps {
                script {
                    sh 'mvn surefire:test'
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }
        
        stage('Integration Tests') {
            steps {
                script {
                    sh 'mvn failsafe:integration-test'
                    junit testResults: '**/target/failsafe-reports/*.xml', allowEmptyResults: true
                }
            }
        }
        
        stage('Deploy') {
            steps([$class: 'BapSshPromotionPublisherPlugin']) {
                sshPublisher(
                    continueOnError: false, failOnError: true,
                    publishers: [
                        sshPublisherDesc(
                            configName: "naver-soo-hw",
                            verbose: true,
                            transfers: [
                                sshTransfer(
                                    sourceFiles: "target/*.jar",
                                    removePrefix: "target",
                                    remoteDirectory: "/hw", 
                                    execCommand: ""
                                )
                            ]
                        )
                    ]
                )
            }
        }
    }
}
