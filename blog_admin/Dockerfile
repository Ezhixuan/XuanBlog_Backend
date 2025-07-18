# 使用 JRE 基础镜像
FROM eclipse-temurin:21-jre-alpine

RUN mkdir -p /app/logs

# 设置工作目录
WORKDIR /app/server

# 复制构建好的JAR文件
COPY ./target/blog_server-0.0.1-beat.jar app.jar

# 暴露端口
EXPOSE 8901 8902

# 设置默认的JVM参数和Spring Profile
# 这些值现在可以在 `docker run -e` 或 docker-compose.yml 中被轻松覆盖
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:+UseStringDeduplication"
ENV SPRING_PROFILES_ACTIVE="prod"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8902"

# 容器启动时执行的命令
# 使用 `sh -c` 来执行命令，这样 shell 才能正确解析和扩展 $JAVA_OPTS 等环境变量
# exec "$@" 确保 java 进程成为 PID 1，能够正确接收和处理 SIGTERM 等信号
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS $DEBUG_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar"]
