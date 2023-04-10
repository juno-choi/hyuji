FROM openjdk:17.0.2-slim

# jar파일 복사
COPY build/libs/normal-api-1.0.jar normal.jar
ENTRYPOINT ["java","-jar","normal.jar"]