FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine
RUN apk -U upgrade
ENV PORT 8081
ENV CLASSPATH /opt/lib
EXPOSE 8081 9000

COPY target/stwgs-content-api*.jar /opt/app.jar
VOLUME /content
COPY content /content
WORKDIR /opt
CMD ["java", "-jar", "app.jar"]
