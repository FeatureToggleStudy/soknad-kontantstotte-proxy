FROM navikt/java:8

ADD ./VERSION /app/VERSION
COPY ./target/soknad-kontantstotte-proxy.jar "/app/app.jar"
