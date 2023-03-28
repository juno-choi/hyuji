# 📗 Normal API

일반적인 사이트에서 사용되는 API 프로젝트

👉 [깃허브 주소](https://github.com/juno-choi/normal-api)

## 📄 skill

`language` java 17

`framework` Spring Boot 2.7.7

`Tool` Intellij, git, gradle, jenkins, docker

`DB` MySql, JPA, Redis, H2

`docs` restdocs

## 📄 기능

### ✅ 이메일 회원가입

### ✅ JWT token 방식 로그인 구현

1. redis jwt access, refresh token 등록하여 expire 시간에 따라 토큰 유효 체크
2. refresh token 재발급 기능

### ✅ 게시판, 댓글 기능

1. 게시판 글 등록
2. 게시판 불러오기 (리스트)
3. 게시판 불러오기 (상세)
4. 댓글 등록