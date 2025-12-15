#! /bin/sh

JAVA_OPTS="-Xms512m -Xmx1024m"
JAR_FILE="../target/dstone-boot.war"

nohup java ${JAVA_OPTS} -jar ${JAR_FILE} net.dstone.DstoneBootApplication
# nohup java ${JAVA_OPTS} -jar ${JAR_FILE} net.dstone.DstoneBootApplication > /dev/null 2>&1 &
