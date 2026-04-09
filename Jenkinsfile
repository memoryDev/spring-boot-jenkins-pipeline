pipeline {
    agent any

    tools {
        jdk 'jdk17'
    }

    stages {
        stage('Checkout') {
            steps {
                deleteDir()
                checkout scm
                echo "--- Current Source Commit Information ---"
                sh 'git log -1'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean bootJar'
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
            when {
                anyOf {
                    branch 'main'
                    expression { env.BRANCH_NAME == 'main' }
                    expression { env.GIT_BRANCH == 'main' }
                    expression { env.GIT_BRANCH == 'origin/main' }
                }
            }
            steps {
                echo '운영 서버 배포를 시작합니다 (Port: 9000)...'
                sh '''
                     # 1. 기존 9000 포트 프로세스 종료
                     fuser -k 9000/tcp || true
                     sleep 3

                     # 2. 기존 로그 파일 삭제 (권한 꼬임 방지)
                     # 사용자님이 수동으로 실행해서 생긴 파일이 젠킨스 실행을 방해할 수 있으므로 삭제합니다.
                     sudo rm -f /opt/deploy/app.log || true

                     # 3. 새 빌드 파일 복사
                     TARGET_JAR=$(find build/libs -name "*-SNAPSHOT.jar" ! -name "*-plain.jar")
                     cp $TARGET_JAR /opt/deploy/app.jar

                     # 4. 백그라운드 실행 (프로세스 유지 설정)
                     echo "Starting new process with JENKINS_NODE_COOKIE=dontKillMe..."
                     export JENKINS_NODE_COOKIE=dontKillMe
                     
                     # 5. 실행 (환경변수와 포트 지정)
                     nohup java -jar /opt/deploy/app.jar --server.port=9000 > /opt/deploy/app.log 2>&1 &
                     
                     # 6. 기동 확인 (최대 30초 대기)
                     echo "서버 구동 대기 중..."
                     for i in {1..15}; do
                         if grep -q "Started JenkinstestApplication" /opt/deploy/app.log; then
                             echo "서버가 성공적으로 시작되었습니다! (Port: 9000)"
                             break
                         fi
                         sleep 2
                         if [ $i -eq 15 ]; then
                             echo "오류: 서버 구동 확인 실패. 로그 하단을 출력합니다:"
                             tail -n 50 /opt/deploy/app.log
                             exit 1
                         fi
                     done
                '''
            }
        }
    }

    post {
        success {
            echo 'CI/CD 파이프라인 성공! http://localhost:9000 에서 확인하세요.'
        }
        failure {
            echo 'CI/CD 파이프라인 실패! 로그를 확인해 주세요.'
        }
    }
}
