#! /bin/sh

docker commit dstone-boot dstone-boot:v0.8
docker tag dstone-boot:v0.8 jysn007/dstone-boot:v0.8
docker login -u jysn007 -p 'db2admin!@'
docker push jysn007/dstone-boot:v0.8
