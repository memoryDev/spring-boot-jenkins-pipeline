# 🚀 Spring Boot Jenkins CI/CD 실습 프로젝트

이 프로젝트는 Spring Boot 기반의 API 서버를 Jenkins 파이프라인으로 자동 빌드 및 배포하는 환경을 구축한 실습 결과물입니다.

## 🛠 기술 스택
- **Java:** 17
- **Framework:** Spring Boot 3.2.4
- **Build Tool:** Gradle
- **CI/CD:** Jenkins (Declarative Pipeline)
- **Deployment:** Linux Server (Local Path: `/opt/deploy`)

## 🏗 CI/CD 파이프라인 구성 (`Jenkinsfile`)
프로젝트 루트의 `Jenkinsfile`을 통해 다음 단계가 자동으로 수행됩니다:

1.  **Checkout:** `deleteDir()`를 통해 워크스페이스를 초기화하고 최신 코드를 가져옵니다.
2.  **Build:** `./gradlew clean bootJar` 명령으로 실행 가능한 최신 JAR를 생성합니다.
3.  **Test:** 모든 테스트 코드를 실행하고 JUnit 리포트를 Jenkins 대시보드에 기록합니다.
4.  **Deploy:** **`main` 브랜치에 머지될 때만** 작동하며, 아래 과정을 거칩니다:
    - 이전 프로세스 종료 (9000 포트)
    - 기존 로그 및 파일 정리 (권한 충돌 방지)
    - 새로운 JAR 파일 복사 및 백그라운드 실행 (`nohup`)
    - **Health Check:** 서버가 정상적으로 뜰 때까지 로그를 감시하여 배포 성공 여부 판정

## 🌐 서비스 접속 정보
| 환경 | 주소 | 비고 |
|---|---|---|
| **로컬 개발** | `http://localhost:8888` | IntelliJ 등에서 직접 실행 시 |
| **운영(Jenkins)** | `http://localhost:9000` | Jenkins 자동 배포 서버 |

### 주요 API 명세
- `GET /api/hello`: 기본 인사말 반환
- `GET /api/hello2`: 배포 확인용 테스트 API

## 💡 트러블슈팅 가이드 (배운 점)
배포 과정에서 발생했던 주요 문제와 해결 방법입니다:

1.  **Permission Denied:**
    - `/opt/deploy` 폴더 권한 문제 해결을 위해 `sudo chown -R jenkins:jenkins /opt/deploy`를 수행했습니다.
2.  **포트 충돌:**
    - 로컬 서버(8888)와 젠킨스 서버(9000) 포트를 분리하여 상호 간섭 없이 개발이 가능하도록 설정했습니다.
3.  **이전 버전 잔상:**
    - `fuser -k` 명령어로 기존 프로세스를 확실히 죽이고, `JENKINS_NODE_COOKIE=dontKillMe` 설정을 통해 젠킨스 종료 후에도 앱이 유지되도록 처리했습니다.
4.  **권한 꼬임:**
    - 수동 실행으로 인해 생긴 로그 파일은 젠킨스가 수정할 수 없으므로, 배포 스크립트에서 기존 로그를 삭제(`rm -f`)하는 과정을 추가했습니다.

## 🧪 테스트 실행
```bash
./gradlew clean test
```
