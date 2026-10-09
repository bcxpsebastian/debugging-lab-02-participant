FROM eclipse-temurin:17-jdk

WORKDIR /app
COPY src ./src

RUN javac --release 17 -d build src/*.java

EXPOSE 5005

ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-cp", "build", "ValidationRunner"]
