# Build stage
FROM eclipse-temurin:17-jdk AS server-build
WORKDIR /app

# Gradle Wrapper 복사 및 실행 권한 부여
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

RUN chmod +x gradlew

# 의존성 캐싱을 위해 먼저 다운로드
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사 및 빌드
COPY src src
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app

# 타임존 설정 (Asia/Seoul)
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 보안을 위한 non-root 유저 생성 (Debian/Ubuntu 형식)
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

COPY --from=server-build /app/build/libs/*.jar app.jar

RUN chown appuser:appgroup app.jar
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
