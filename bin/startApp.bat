
set JAVA_OPTS=-Djava.ext.dirs=C:/DEV/JDK/1.8/jre/lib/ext;C:/Windows/Sun/Java/lib/ext;D:/AppHome/dstone-boot/extLib
set CONF_FILE=-Dspring.config.location=file:../conf/application.yml
set JAR_FILE=../target/dstone-boot-0.0.1-SNAPSHOT.war
start /B javaw %JAVA_OPTS% %CONF_FILE% -jar %JAR_FILE% net.dstone.DstoneBootApplication

