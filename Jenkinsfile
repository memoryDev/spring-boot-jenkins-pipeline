pipeline {
    // 1. 실행 환경 설정: 어떤 에이전트(노드)에서 실행할지 결정합니다. 'any'는 사용 가능한 아무 곳에서나 실행한다는 의미입니다.
    agent any

    // 2. 도구 설정: 젠킨스 'Global Tool Configuration'에 등록된 도구를 사용합니다.
    tools {
        // Jenkins Global Tool Configuration에서 설정한 JDK 이름 ('jdk17')을 사용합니다.
        jdk 'jdk17'
    }

    // 3. 파이프라인의 핵심 단계들 정의
    stages {
        // [1단계] 소스 코드 체크아웃
        stage('Checkout') {
            steps {
                // Jenkins 프로젝트 설정에 입력된 Git/SCM 정보를 바탕으로 최신 코드를 내려받습니다.
                checkout scm
            }
        }

        // [2단계] 애플리케이션 빌드
        stage('Build') {
            steps {
                // Gradle 실행 파일(gradlew)에 실행 권한(+x)을 부여합니다.
                sh 'chmod +x gradlew'
                // 이전에 빌드된 결과물을 삭제(clean)하고, 실행 가능한 JAR 파일을 생성(assemble)합니다.
                sh './gradlew clean assemble'
            }
        }

        // [3단계] 테스트 실행
        stage('Test') {
            steps {
                // 프로젝트의 단위 테스트 및 통합 테스트를 실행합니다.
                sh './gradlew test'
            }
            // 단계 종료 후 실행되는 처리 (항상 실행)
            post {
                always {
                    // 테스트 결과를 JUnit 형식의 XML 리포트로 젠킨스 대시보드에 기록합니다.
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        // [4단계] 서버 배포
        stage('Deploy') {
            steps {
                echo '애플리케이션 배포를 시작합니다...'
                sh '''
                     # 1. 기존에 실행 중인 8888 포트 프로세스 종료 (있을 경우만)
                     echo "기존 프로세스가 실행 중인지 확인하고 종료합니다..."
                     CURRENT_PID=$(lsof -t -i:8888) || true
                     if [ ! -z "$CURRENT_PID" ]; then
                         kill -9 $CURRENT_PID || true
                     fi

                     # 2. 새 빌드 파일을 배포 폴더로 복사 (이름을 app.jar로 통일하여 관리 편의성 증대)
                     echo "빌드된 JAR 파일을 /opt/deploy 폴더로 복사합니다..."
                     cp build/libs/jenkinstest-0.0.1-SNAPSHOT.jar /opt/deploy/app.jar

                     # 3. 백그라운드에서 애플리케이션 실행
                     # BUILD_ID=dontKillMe: 젠킨스가 파이프라인 종료 시 실행한 자식 프로세스를 죽이지 않도록 설정하는 환경 변수입니다.
                     # nohup: 터미널이 닫혀도 프로세스가 종료되지 않게 합니다.
                     # > /opt/deploy/app.log 2>&1: 표준 출력과 에러를 로그 파일로 저장합니다.
                     # &: 프로세스를 백그라운드에서 실행합니다.
                     echo "새로운 프로세스를 시작합니다..."
                     BUILD_ID=dontKillMe nohup java -jar /opt/deploy/app.jar > /opt/deploy/app.log 2>&1 &

                     echo "배포가 성공적으로 완료되었습니다!"
                 '''
            }
        }
    }

    // 4. 전체 파이프라인 종료 후 최종 결과 처리
    post {
        // 모든 단계가 성공적으로 끝났을 때 실행
        success {
            echo 'CI/CD 파이프라인이 성공적으로 완료되었습니다!'
        }
        // 파이프라인 중 하나라도 실패했을 때 실행
        failure {
            echo 'CI/CD 파이프라인이 실패했습니다. 젠킨스 로그를 확인해 주세요.'
        }
    }
}
