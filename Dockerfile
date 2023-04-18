FROM openjdk:17.0.2-slim

# jar파일 복사
COPY build/libs/hyugi-1.0.jar hyugi.jar
ENTRYPOINT ["java","-jar","hyugi.jar"]