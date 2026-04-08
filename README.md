# Jenkins Test Project 🚀

이 프로젝트는 Spring Boot를 기반으로 구축된 단순 API 서버이며, Jenkins를 활용한 CI/CD 파이프라인 연동을 실습하고 검증하기 위해 제작되었습니다.

## 🛠 기술 스택
- **Language:** Java 17
- **Framework:** Spring Boot 3.x
- **Build Tool:** Gradle
- **Test:** JUnit 5 (MockMvc, AssertJ)
- **CI/CD:** Jenkins (Pipeline-as-Code)

## ✨ 주요 기능
- 단순 문자열을 반환하는 REST API 제공.
- Jenkins를 통한 빌드, 테스트, 배포 자동화 프로세스 구현.
- JUnit 5 기반의 단위 테스트 및 통합 테스트 구성.

## 🚀 로컬 실행 방법
```bash
# 저장소 클론
git clone <repository-url>

# 프로젝트 디렉토리 이동
cd jen-test

# 빌드 및 실행
./gradlew bootRun
```
실행 후 `http://localhost:8080/api/hello` 접속 시 `"Hello, Jenkins CI/CD!"` 응답을 확인할 수 있습니다.

## 🧪 테스트 실행
```bash
./gradlew clean test
```
- `HelloControllerTest`: MockMvc를 이용한 컨트롤러 단위 테스트.
- `HelloIntegrationTest`: 실제 어플리케이션 컨텍스트를 로드하여 검증하는 통합 테스트.

## 🎡 CI/CD 파이프라인 (Jenkins)
프로젝트 루트의 `Jenkinsfile`을 통해 다음 단계가 자동으로 수행됩니다:
1. **Build:** `./gradlew clean assemble`을 통해 실행 가능한 .jar 파일 생성.
2. **Test:** `./gradlew test`를 실행하고 결과를 JUnit XML 형태로 Jenkins에 리포팅.
3. **Deploy:** 빌드된 아티팩트의 배포를 시뮬레이션.

## 📝 API 명세
| Method | Path | Description | Response |
|---|---|---|---|
| GET | `/api/hello` | 인사말 반환 | "Hello, Jenkins CI/CD!" |
