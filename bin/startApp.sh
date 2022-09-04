
JAVA_OPTS:="-Xms512m -Xmx1024m -Djava.ext.dirs=/DEV/JDK/1.8/jre/lib/ext:/Sun/Java/lib/ext:/AppHome/dstone-boot/extLib"
CONF_FILE:="-Dspring.config.location=file:../conf/application.yml"
JAR_FILE="../target/dstone-boot-0.0.1-SNAPSHOT.war"
javaw ${JAVA_OPTS} ${CONF_FILE} -jar ${JAR_FILE} net.dstone.DstoneBootApplication

