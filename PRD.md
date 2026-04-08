# PRD: Gradle 기반 Spring Boot API 서버 및 Jenkins CI/CD 연동

## 1. Problem Statement (문제 정의)
Spring Boot 기반의 단일 API 서버를 구축하고, Jenkins를 활용하여 지속적 통합 및 배포(CI/CD) 파이프라인을 자동화하여 코드 변경에 대한 안정적인 빌드, 테스트 및 배포 프로세스를 확보한다.

## 2. Scope & Non-Goals (범위 및 제외 대상)
**Scope (범위):**
- Java 17, Spring Boot 3.x, Gradle을 활용한 애플리케이션 기본 구조 설정
- GET `/api/hello` 엔드포인트 구현 (단순 문자열 반환)
- JUnit 5를 사용한 단위 테스트 구현 (`/api/hello` 정상 응답 검증)
- Jenkinsfile을 작성하여 Build, Test, Deploy 스테이지 파이프라인 구성
- 테스트 결과(JUnit XML)를 Jenkins 파이프라인에 통합 및 리포팅
- Gradle Wrapper(`gradlew`)를 이용한 OS 독립적인 빌드 환경 구성

**Non-Goals (제외 대상):**
- 실제 운영 서버(Production) 환경에 대한 무중단 배포(Zero-downtime deployment) 및 롤백 전략 구현
- 데이터베이스 연동 및 복잡한 비즈니스 로직 처리
- 보안/인증(Spring Security 등) 처리
- SonarQube 등 외부 정적 분석 도구 연동 (기본 테스트 리포트만 처리)

## 3. Acceptance Criteria (인수 조건)
- **AC1. 로컬 환경 빌드 및 실행:**
  - `./gradlew build` 명령어가 에러 없이 성공적으로 실행되어야 한다.
  - 애플리케이션 실행 후 `curl http://localhost:8080/api/hello` 호출 시 HTTP 상태 코드 200과 함께 예상된 응답(예: "Hello, World!")이 반환되어야 한다.
- **AC2. 테스트 통과:**
  - `./gradlew test` 명령어 실행 시 모든 JUnit 5 테스트가 통과해야 한다.
  - `/api/hello` 엔드포인트에 대한 MockMvc 기반의 테스트 코드가 존재하고 성공해야 한다.
- **AC3. Jenkins Build 스테이지:**
  - Jenkins에서 파이프라인 실행 시 `Build` 스테이지에서 `./gradlew clean assemble` (또는 `build -x test`)가 성공하고, 실행 가능한 빌드 아티팩트(.jar)가 생성되어야 한다.
- **AC4. Jenkins Test 스테이지:**
  - `Test` 스테이지에서 `./gradlew test`가 성공적으로 실행되어야 한다.
  - `build/test-results/test/*.xml` 경로에 JUnit XML 리포트 파일이 생성되고, `junit` 플러그인을 통해 Jenkins UI의 Test Result 트렌드에 반영되어야 한다.
- **AC5. Jenkins Deploy 스테이지:**
  - `Deploy` 스테이지가 정상적으로 실행되며, 요구사항에 따른 모의 배포(echo 명령어나 단순 jar 백그라운드 실행 등)가 스크립트 상에서 오류 없이 종료되어야 한다.

## 4. Constraints & Dependencies (제약 사항 및 의존성)
**Constraints (제약 사항):**
- JDK: 17 버전 사용 필수
- Framework: Spring Boot 3.x 버전 사용 필수
- Build Tool: Gradle (Gradle Wrapper 포함) 사용 필수
- CI/CD Tool: Jenkins (Pipeline script from SCM 또는 인라인 Jenkinsfile 방식 지원)
- 모든 빌드 및 테스트는 컨테이너 또는 Jenkins 에이전트 내에서 Gradle Wrapper를 통해 수행되어 환경 의존성을 최소화해야 함.

**Dependencies (의존성):**
- Jenkins 서버 내 JDK 17 환경 (또는 Docker Pipeline을 통한 JDK 17 컨테이너 환경 제공)
- `spring-boot-starter-web`, `spring-boot-starter-test` 모듈 의존성

## 5. Handoff Checklist (핸드오프 체크리스트)
- [ ] Spring Boot 프로젝트 골격 생성 (build.gradle, settings.gradle, gradlew 등)
- [ ] `/api/hello` 컨트롤러 클래스 작성 (`HelloController.java`)
- [ ] `HelloController`에 대한 JUnit 5 WebMvcTest 작성 (`HelloControllerTest.java`)
- [ ] 프로젝트 루트에 `Jenkinsfile` 작성 (Build, Test, Deploy 스테이지 및 post action 포함)
- [ ] 로컬에서 `./gradlew build` 및 `./gradlew test` 정상 동작 확인
