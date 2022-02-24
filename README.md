# TODO APP

# 개발 환경 구성

- 환경 구성
    - java8 openjdk
    - eclipse (2020-09)
    - SpringBoot (2.5.6)
    - React.js
    - AWS

## 이클립스 설치 및 BootRun

### 이클립스 설치

아래 URL로 접속하여 최신 버전 다운로드

- 다른 버전 다운로드시에는 Download Packages 선택하여 다운로드

[https://www.eclipse.org/downloads/](https://www.eclipse.org/downloads/)

![Untitled](TODO%20APP%204032e/Untitled.png)

---

### 프로젝트 세팅

1. spring initializr에서 다음과 같이 입력한 후 Generate 하면 세팅된 프로젝트가 다운로드된다.

![Untitled](TODO%20APP%204032e/Untitled%201.png)

1. 이클립스의 workspace에 다운로드된 프로젝트를 압축 해제
    
    ls /c/TODO
    |-- eclipse
    |   |-- ...
    |-- tools
    |   |-- Java
    `-- workspace
        `-- todoapp
    
2. 이클립스의 file → import → import Gradle Project → Project Root direct → (2)의 폴더 지정 → Gradle wrapper 지정 → finish
    1. 그럼 프로젝트가 import되고 .gradle과 .settings 파일이 생성된 것을 확인할 수 있다.
3. org.eclipse.buildship.core.prefs 파일의 gradle.user.home 키 경로를 다음과 같이 바꿔 하나의 workspace에서만 공유할 수 있도록 지정한다.

![Untitled](TODO%20APP%204032e/Untitled%202.png)

# BACK-END

## Basic 인증

- HTTP요청에 ID/PW를 함께 보내 인증하는 것
- HTTP 요청 헤더의 Authorization: ‘Basic <ID>:<Password> 콜론을 붙인후 Base64로 인코딩후 문자열을 함께 전송한다.
- HTTP 요청을 받으면 문자열을 디코딩해 +아이디와 비밀번호를 조회한 후 인증 서버의 레코드와 비교후 일치하면 요청받은 일을 수행한다.
- 하지만 HTTP 요청을 가로채 디코딩하면 ID와 PW를 알아낼 수 있으므로 HTTPS와 사용해야 한다.
- 매번 로그인 요청을 하는 매커니즘이므로 한꺼번에 로그인 하거나 디바이스 별로 로그아웃 할 수 있는 기능을 제공하지 못한다.
- 인증 서버 단일 장애점의 단점을 가짐

### 토큰 기반 인증

- Token안 사용자를 구별할 수 있는 문자열
`ex ) Authorization: Bearer Nn4dldmmfsldkf....`
- 로그인 → 토큰 생성 및 저장 → Token반환 → 토큰 포함 요청 → 검증 → 리소스반환
- Basic 인증의 스케일 문제와 동일

### JWT : JSON 웹 토큰

- 서버에서 전자 서명된 토큰을 이용하면 인증에 따른 스케일 문제를 해결
- JWT 토큰은 `{header}.{playload}.{signature}`로 구성
- Header
    - typ : Type의 약어로 토큰의 타입
    - alg : 알고리즘의 약어로 토큰의 서명을 발행하는데 사용된 해시 알고리즘의 종류를 의미
- Payload
    - sub : Subject의 약어로 토큰의 주인을 의미하며 sub은 ID처럼 유일한 식별자여야한다.
    - iss : Issuer의 약어로 토큰을 발행한 주체를 의미한다
    ex) TODO app
    - iat : issued at의 약어로 토큰이 발행된 날짜와 시간을 의미한다.
    - exp : expiration을 줄인 말로 토큰이 만료되는 시간을 의미
- Signature
    - 토큰의 유효성 검사에 사용되는 서명으로 Issuer가 발행한다.

### JWT 토큰 생성과 인증 절차

1. 로그인 요청시 서버에서 {header},{playload}를 secret를 이용해 해시 함수에 돌린 암호화한 값인 전자서명을 만들고 {header},{playload},{signature}으로 이어 붙인 후 Base64로 인코딩후 반환한다.
2. 리소스 접근 요청시 Base64로 디코딩후 secret을 이용해 전자 서명을 만든 후 요청 전자 서명과 비교해 토큰의 유효성을 검사한다.
3. 따라서 인증 서버에 토큰의 유효성을 확인하는 요청을 할 필요 없다.
4. 인증 서버가 단일 장애점을 극복
5. HTTPS를 이용해 토큰 탈취를 방지

## 로그인 기능 개발 구현

```basic
`-- user
    |-- controller
    |   `-- UserController.java
    |-- dto
    |   `-- UserDTO.java
    |-- model
    |   `-- UserEntity.java
    |-- persistence
    |   `-- UserRepository.java
    `-- service
        `-- UserService.java
```

## 스프링 시큐리티와 JWT 구현

build.gradle에 다음을 추가

```groovy
implementation group: 'io.jsonwebtoken', name: 'jjwt', version: '0.9.1'
```

토큰을 생성하고 검증하는 클래스 구현

```basic
|-- security
|   `-- TokenProvider.java
```

- JWT라이브러리를 이용해 JWT토큰을 생성
- 토큰을 디코딩 및 파싱하고 토큰 위조 여부를 확인

UserController.java에서 token 생성로직을 추가

```java
...
final String token = tokenProvider.create(user);			
// 토큰 생성
final UserDTO responseUserDTO = UserDTO.builder()
							.token(token)
...
```

반환된 로그인 성공 JSON

```json
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4ODE1MjdmMDZhNThkMDE3ZjA2YTY2NGEzMDAwMCIsImlzcyI6InRvZG8gYXBwIiwiaWF0IjoxNjQ1MDg0MDU0LCJleHAiOjE2NDUxNzA0NTN9.4p5lTir97t4VQa3V-Yz3O_02UptKgBKZnFBiPUe_xYRggObsAFBI5QW9rEEiLeqjuzIgMxVwf9HcayCqjKCzWw",
    "email": "helloworld.com",
    "username": null,
    "password": null,
    "id": "402881527f06a58d017f06a664a30000"
}
```

## 스프링 시큐리티

- 스프링 시큐리티를 추가하면 FilterChainProxy 필터를 서블릿 필터에 넣어준다.
- web.xml대신 WebSecurityConfigurerAdaper를 상속하여 설정
- build.gradle에 아래 구현 추가후 refresh gradle proejct

```groovy
implementation 'org.springframework.boot:spring-boot-starter-security'
```

- OncePerRequestFilter는 요청당 반드시 한 번만 실행되므로 한 번만 인증하도록 이 필터를 상속하는 JwtAuthenticationFilter를 구현
- postman에 Authorization의 Type에 Bearer Token을 선택 후 return 받은 Token을 입력하여 전송하면 정상적으로 동작한다

### 패스워드 암호화

- BCryptPasswordEncoder 구현을 통한 패스워드 암호화
- Salting을 이용하여 인코딩하므로 BCryptPasswordEncoder .matches()를 사용하여 인코딩된 문자를 비교하여 두 값의 일치 여부를 확인

WebSecurityConfig.java

```java
@Bean
private BCryptPasswordEncoder passwordEncoder() {
	return new BCryptPasswordEncoder();
}
```

- WebSecurityConfig에서 스프링 컨테이너에 passwordEncoder를 BCryptPasswordEncoder로 구현하는 Bean으로 등록

---

UserService.java

```java
public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {

		final UserEntity originalUser = userRepository.findByEmail(email);
		// matches 메서드를 이용해 패스워드가 같은지 확인
		if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
			return originalUser;
		}
		
		return null;		
}
```

- UserController에서 DI로 passwordEncoder를 주입받고 getByCredentials함수의 세 번째 인자로 지정한다.
- matches함수를 사용해 파라미터로 넘어오는 암호문과 DB의 암호문을 비교하여 일치여부 확인

 

# AWS 프로덕션 배포

- AWS 일라스틱 빈스톡
- AWS 데이터베이스
- 오토 스케일링 그룹
- 로드 밸런서
- ROUTE 53
- AWS Certificate Manager

## EC2 (Elastic Compute Cloud)

- 서버 컴퓨터
- 로컬에서 App을 실행하듯 클라우드 컴퓨터를 활용해 App을 실행

## ROUTE 53 - DNS

- 브라우저에 도메인주소를 입력하면 우리의 EC2 IP를 제공해 줄 수 있는 DNS 서비스

## 로드 밸런서

- 트래픽 분배
- 로드 Load = 트래픽, 밸런서 = 균형

## 오토 스케일링 그룹

- 연결된 인스턴스를 Target Group이라 한다.
- 자동으로 스케일
- 최소, 최대, 적정 인스턴수를 정할 수 있다.

## VPC와 서브넷

### VPC (Virtual Private Cloud)

- AWS 게정 전용가상 네트워크
    - 옵션을 설정하지 않는 이상, 이 네트워크 안에서 생성되는 EC2는 외부 접근 불가능
    - 독립적인 가상의 네트워크
    - VPC의 주인인 계정 본인만 접속 가능

### 서브넷

- VPC안에 여러 개로 쪼개진 네트워크를 의미
- 서브넷의 설정에 따라 EC2의 사설 IP주소가 결정

## 일라스틱 빈스톡

- 코드로 인프라를 묘사하고 서비스가 이를 반영
IaC(Infrastructure as Code)

---

## ⚙ AWS 설치 ⚙

## AWS 접근 3가지 방법

- AWS 콘솔
    - [https://console.aws.amazon.com](https://console.aws.amazon.com에서) 에서 접근 할 수 있으며 GUI 기반으로 동작
- AWS CLI (Command Line Interface)
    - 터미널이나 powerShell에서 명령어를 이용
- AWS SDK
    - 라이브러리를 추가하고 프로그래밍을 통한 리소스 접근

### 파이썬 설치

- AWS CLI와 EB CLI는 파이썬 기반으로 동작하므로 파이썬 설치
- [https://www.python.org/downloads/](https://www.python.org/downloads/) 에서 파이썬3설치

### AWS CLI 설치

- [https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/install-cliv2-windows.html](https://docs.aws.amazon.com/ko_kr/cli/latest/userguide/install-cliv2-windows.html) 에서 AWS CLI 설치

---

## IAM (identity and Access Management)

- 모든 개발자가 같은 계정을 사용하는 것은 보안에 취약하며 역할 분리가 안된다.
- 역할마다 접근 권한을 줄 수 있도록 개발자에게는 아이디와 비밀번호를,
프로그램에는 액세스키와 비밀 액세스키를 제공

### 액세스키와 비밀 액세스키 생성

- AWS CLI를 사용하기 위한 액세스키와 비밀 액세스키 생성
    1. AWS console → IAM → 액세스 관리 → 사용자 → 사용자 추가
    
    ![Untitled](TODO%20APP%204032e/Untitled%203.png)
    
    1. 기존 정책 직접 연결(탭) → AdministratorAccess 선택
    2. 키-값 (옵션)
    3. 사용자 만들기
    4. 완료 액세스키와 비밀 액세스 키 발급
    
- AWS Credential 설정
    
    ```bash
    aws configure
    # 발급받은 액세스키 입력
    AWS Access Key ID [None]:
    # 발급받은 비밀 액세스키 입력
    AWS Secret Access Key [None]:
    # 한국에 거주활 확률이 높으면 ap-northeast-2
    Default region name [None]:
    # json
    Default output format [None]:
    ```
    
- EB CLI 설치 (일라스틱 빈스톡 환경 구축)
    
    ```bash
    $ pip install awsebcli --upgrade --user
    ```
    
    - 환경 변수 설정
    C:\Users\user\AppData\Roaming\Python\Python310\Scripts
    - eb 버전 확인 `eb —version`

---

## 일라스틱 빈스톡을 이용한 배포

- 프로젝트의 ROOT 디렉토리로 이동하여 `eb init` 실행
    
    ```bash
    #eb init 명령 실행
    $ eb init todoapp
    # Select a default region
    
    ```
    
    - Select a default region
        - 일라스틱 빈스톡이 어느 리전에 App의 환경을 생성해야 하는지 선택
    - Select a platform & Select a platform branch
        - App이 사용할 플랫폼을 선택 ex) Java8, Window, Linux
    - Do you want to set up SSH for your instance?
        - 일라스틱 빈스톡을 이용한 EC2에 접근하기 위한 SSH를 설정할 것인지 선택

- 설정을 완료하면 프로젝트 ROOT 디렉터리에 .elasticbeanstalk/config.yml 폴더와 파일이 생성되며 `eb init`시 설정한 정보들이 나열돼 있다.

### spring profile을 통한 개발, 운영 설정 분리

```yaml
server:
  port : 5000
spring:
  jpa:
    database: MYSQL	#MYSQL사용
    show-sql: true	#JPA가 실행한 sql 쿼리를 로그로 출력할 지 여부
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
  datasource:
    url: jdbc:mysql://${rds.hostname}:${rds.port}/${rds.db.name}
    username: ${rds.username}
    password: ${rds.password}
```

- `database-platform` : MySQL8Dialect는 자바의 데이터형과 DB의 데이터형을 매핑
(Sring → Varchar, Interger → bigint 등)
@Id, @GenerateValue 등을 DB 키워드로 전환
- `hibernate : ddl-auto` : App runtime시 DB의 테이블의 작업을 3가지로 구분한다.
보통 운영환경에서는 validate를 사용하고 update 필요시는 사용자 지정 script를 작성
    - `create` : 모든 테이블을 지우고 새로 만듬
    - `update` : 테이블이 없다면 새로만들고, 변경된 테이블을 업데이트한다.
    - `validate` 현재 DB의 테이블 스키마가 App에 정의된 모델과 일치하는지 확인

- `datasource` :DB URL을 설정 jdbc:<db_type>://<host>:<port>/<db_name>
${rds.hostname} ... 은 일라스틱 빈스톡이 App을 실행하면서 이 값을 지정해 줄 수 있다.

mysql을 사용하기 위한 build.gradle 추가

```groovy
runtimeOnly 'mysql:mysql-connector-java'
```

## HealthCheck API

AWS 로드 벨런서는 APP의 기본 경로인 “/”에 HTTP 요청을 보내 동작하는지 확인한다.

EB는 이를 기반으로 APP이 정상작동하는지 모니터링이 필요한지 확인하며 콘솔에 표시한다.

- HealthCheckController 추가
    
    ```java
    @GetMapping("/")
    public String healthCheck() {
    	return "The service is up and running...";
    }
    ```
    

### target jar 설정

- EB에 App이 자동으로 배포 설정 되도록 다음과 같이 작성한다.
    
    ```yaml
    deploy:
      artifact: build/libs/todoapp-0.0.1-SNAPSHOT.jar
    ```
    

### eb create를 이용해 AWS에 환경 설정

```bash
eb create --database --elb-type application --instance-type t2.micro
```

- `eb create` : AWS에 EB 환경을 생성
- `--database` : RDS DB를 만들기 위핸 매개변수로 자동으로 DB가 생성
- `--elb-type <ELB 타입>` : 일라스틱 로드 밸런서 타입 매개변수로 로드 밸런서와 오토 스케일링 그룹을 이용할 경우 사용
- `--instance-type <인스턴스 타입>` : App이 동작할 인스턴스 타입으로 프리티어인 t2.micro 사용

### eb setenv를 이용한 프로파일 설정 적용

```bash
$ eb setenv SPRING_PROFILES_ACTIVE=prod
```

- git으로 프로젝트를 관리할 경우에는 eb deploy를 해야 배포됨
    
    ```bash
    $ eb deploy
    ```
    

### AWS CLI를 이용한 인스턴스 확인

```bash
# RDS 확인
$ aws rds describe-db-instances --region ap-northeast-2
# 오토 스케일링 그룹 확인
$ aws autoscaling describe-auto-scaling-groups --region ap-northeast-2
# 로드 밸런서 확인
$ aws elbv2 describe-load-balancers --region ap-northeast-2
# 로드 밸런서와 ASG를 연결하는 타깃 그룹 확인
$ aws elbv2 describe-target-groups --region ap-northeast-2
# EC2 확인
$ aws ec2 describe-instance --region ap-northeast-2
```

## 최종 구현 배포 화면

![Untitled](TODO%20APP%204032e/Untitled%204.png)

![Untitled](TODO%20APP%204032e/Untitled%205.png)

#