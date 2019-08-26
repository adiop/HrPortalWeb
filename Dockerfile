FROM openjdk:8
ADD build/libs/HrPortalWeb-1.0-SNAPSHOT.war HrPortalWeb-1.0-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "HrPortalWeb-1.0-SNAPSHOT.war"]
