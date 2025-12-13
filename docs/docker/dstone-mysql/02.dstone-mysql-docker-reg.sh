#! /bin/sh

docker commit dstone-mysql dstone-mysql:latest
docker tag dstone-mysql:latest jysn007/dstone-mysql:v0.8
docker login -u jysn007 -p 'db2admin!@'
docker push jysn007/dstone-mysql:v0.8
