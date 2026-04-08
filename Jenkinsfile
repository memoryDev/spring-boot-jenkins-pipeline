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
                 sh '''
                     # 1. 기존에 실행 중인 8081 포트 프로세스 종료 (있을 경우만)
                     echo "Stopping old process if exists..."
                     CURRENT_PID=$(lsof -t -i:8081) || true
                     if [ ! -z "$CURRENT_PID" ]; then
                         kill -9 $CURRENT_PID || true
                     fi

                     # 2. 새 빌드 파일을 배포 폴더로 복사 (이름을 app.jar로 통일)
                     echo "Copying jar file to /opt/deploy..."
		     cp build/libs/jenkinstest-0.0.1-SNAPSHOT.jar /opt/deploy/app.jar

                     # 3. 백그라운드에서 실행 (중요: BUILD_ID=dontKillMe)
                     echo "Starting new process..."
                     BUILD_ID=dontKillMe nohup java -jar /opt/deploy/app.jar > /opt/deploy/app.log 2>&1 &

                     echo "Deployment completed successfully!"
                 '''
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
