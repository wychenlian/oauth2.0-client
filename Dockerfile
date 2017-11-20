FROM repository.hybris.com:5005/build/openjdk:latest

ARG traceability
# uncomment to make "traceability" mandatory for building
#RUN if [ -z "$traceability" ]; then echo "the build argument 'traceability' is mandatory"; exit 1; fi
LABEL traceability=$traceability
RUN echo "${traceability}" > /traceability.json

EXPOSE 8081

COPY build/libs/mobile-oauth-agent*SNAPSHOT.jar mobile-oauth-agent.jar
WORKDIR .

CMD ["java","-jar","-Xmx512M","-XX:MaxDirectMemorySize=128M","mobile-oauth-agent.jar"]
