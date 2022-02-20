FROM openjdk:11

ADD target/lab3.war lab3.war
ADD src/main/resources src/main/resources
EXPOSE 8787

ENTRYPOINT ["java", "-jar", "lab3.war"]