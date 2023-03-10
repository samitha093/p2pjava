FROM ubuntu:20.04

RUN apt-get update && apt-get install -y default-jdk
WORKDIR /app
COPY ./Main.java ./

RUN javac Main.java

COPY startup.sh .
RUN chmod +x startup.sh

CMD ["./startup.sh"]

