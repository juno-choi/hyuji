# 📗 Normal API

일반적인 사이트에서 사용되는 API

👉 [깃허브 주소](https://github.com/juno-choi/normal-api)

👉 [서버 실행 후 문서 url](http://localhost:8000/docs.html)

👉 `port` 8000

---

# 📙 skill

`language` java 11

`framework` Spring Boot 2.7.7

`Tool` Intellij, git, gradle, jenkins, docker

`DB` JPA, Redis, H2

`docs` restdocs 

---

# 📕 기능

## 📄 로그인

### ✅ JWT token 방식 로그인 구현

1. email 회원가입
2. redis jwt access, refresh token 등록하여 expire 시간에 따라 토큰 유효 체크
3. token 재발급 기능
4. 회원 상세보기 (회원 ID 기준)

### ✅ Oauth2 로그인 구현

1. kakao 간편 로그인

## 📄 게시판

### ✅ 게시판

1. 게시판 글 등록
2. 게시판 불러오기 (리스트)
3. 게시판 불러오기 (상세)

### ✅ 댓글 기능   

1. 댓글 등록
2. 댓글 불러오기 (리스트)

---