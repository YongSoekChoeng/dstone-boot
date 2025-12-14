#! /bin/sh

docker commit dstone-rabbitmq dstone-rabbitmq:v0.8
docker tag dstone-rabbitmq:v0.8 jysn007/dstone-rabbitmq:v0.8
docker login -u jysn007 -p 'db2admin!@'
docker push jysn007/dstone-rabbitmq:v0.8
