FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} asena.jar
COPY application.properties application.properties
ENTRYPOINT ["java", "-jar", "asena.jar"]
