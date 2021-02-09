FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} asena.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=container", "-jar", "asena.jar"]
