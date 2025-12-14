#! /bin/sh

docker commit dstone-mysql dstone-mysql:v0.8
docker tag dstone-mysql:v0.8 jysn007/dstone-mysql:v0.8
docker login -u jysn007 -p 'db2admin!@'
docker push jysn007/dstone-mysql:v0.8
