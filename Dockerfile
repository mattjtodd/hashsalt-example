FROM delitescere/jdk:1.8.0_72

RUN apk update && apk upgrade

WORKDIR /opt/app/hashsalt

RUN mkdir -p /tmp

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "app.jar"]
