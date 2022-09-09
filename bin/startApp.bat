
set JAVA_OPTS=-Xms512m -Xmx1024m
set JAR_FILE=../target/dstone-boot-0.0.1-SNAPSHOT.war
set CONF_FILE=--spring.config.location=file:../conf/application.yml

rem start /B javaw %JAVA_OPTS% -jar %JAR_FILE% net.dstone.DstoneBootApplication %CONF_FILE%
java %JAVA_OPTS% -jar %JAR_FILE% net.dstone.DstoneBootApplication %CONF_FILE%
