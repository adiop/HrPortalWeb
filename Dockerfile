FROM openjdk:8
ADD build/libs/HrProject-1.0-SNAPSHOT.war HrProject-1.0-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "HrProject-1.0-SNAPSHOT.war"]
