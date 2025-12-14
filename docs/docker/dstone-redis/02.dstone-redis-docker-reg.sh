#! /bin/sh

docker commit dstone-redis dstone-redis:v0.8
docker tag dstone-redis:v0.8 jysn007/dstone-redis:v0.8
docker login -u jysn007 -p 'db2admin!@'
docker push jysn007/dstone-redis:v0.8
