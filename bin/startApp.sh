#! /bin/sh

JAVA_OPTS="-Xms512m -Xmx1024m"
JAR_FILE="../target/dstone-boot-0.0.1-SNAPSHOT.war"
CONF_FILE="--spring.config.location=file:..\conf\application.yml"

nohup javaw ${JAVA_OPTS} -jar ${JAR_FILE} net.dstone.DstoneBootApplication ${CONF_FILE} > /dev/null &
# javaw ${JAVA_OPTS} -jar ${JAR_FILE} net.dstone.DstoneBootApplication ${CONF_FILE}

