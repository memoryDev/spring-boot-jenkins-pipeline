pipeline {
    agent any

    tools {
        jdk 'jdk17' // Jenkins Global Tool Configuration에서 설정한 JDK 이름
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean assemble'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                // 실제 배포 시에는 빌드된 jar를 특정 서버로 복사하거나 Docker 이미지를 빌드합니다.
                sh 'ls -l build/libs/'
            }
        }
    }

    post {
        success {
            echo 'CI/CD Pipeline succeeded!'
        }
        failure {
            echo 'CI/CD Pipeline failed. Please check the logs.'
        }
    }
}
