pipeline {
    // 1. 실행 환경 설정
    agent any

    // 2. 도구 설정
    tools {
        jdk 'jdk17'
    }

    // 3. 파이프라인 단계 정의
    stages {
        // [1단계] 소스 코드 체크아웃
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        // [2단계] 애플리케이션 빌드
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean assemble'
            }
        }

        // [3단계] 테스트 실행
        stage('Test') {
            steps {
                sh './gradlew test'
            }
            // 테스트 단계 전용 사후 처리
            post {
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        // [4단계] 서버 배포 (main 브랜치일 때만 실행)
        stage('Deploy') {
            when {
                // 여러 브랜치 이름 표기 방식을 모두 체크하여 안정성을 높임
                anyOf {
                    branch 'main'
                    expression { env.BRANCH_NAME == 'main' }
                    expression { env.GIT_BRANCH == 'main' }
                    expression { env.GIT_BRANCH == 'origin/main' }
                }
            }
            steps {
                echo '운영 서버(main)에 배포를 시작합니다...'
                sh '''
                     # 1. 기존에 실행 중인 8888 포트 프로세스 종료 (있을 경우만)
                     echo "기존 프로세스 확인 중..."
                     CURRENT_PID=$(lsof -t -i:8888) || true
                     if [ ! -z "$CURRENT_PID" ]; then
                         kill -9 $CURRENT_PID || true g
                     fi

                     # 2. 새 빌드 파일을 배포 폴더로 복사
                     echo "파일 복사 중..."
                     cp build/libs/jenkinstest-0.0.1-SNAPSHOT.jar /opt/deploy/app.jar

                     # 3. 백그라운드에서 애플리케이션 실행
                     echo "애플리케이션 실행 중..."
                     BUILD_ID=dontKillMe nohup java -jar /opt/deploy/app.jar > /opt/deploy/app.log 2>&1 &

                     echo "배포가 성공적으로 완료되었습니다!"
                 '''
            }
        }
    }

    // 4. 전체 파이프라인 최종 결과 처리
    post {
        success {
            echo 'CI/CD 파이프라인이 성공적으로 완료되었습니다!'
        }
        failure {
            echo 'CI/CD 파이프라인이 실패했습니다. 젠킨스 로그를 확인해 주세요.'
        }
    }
}
