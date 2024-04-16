FROM python:slim-bullseye

RUN apt-get update -y && apt-get install ncat -y

COPY server.py .

CMD ncat -lvp 1234 -k -e "/usr/local/bin/python server.py"

 
