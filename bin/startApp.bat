
set JAVA_OPTS=-Xms512m -Xmx1024m
set JAR_FILE=../target/dstone-boot.war
set APP_CONF_FILE=--spring.config.location=file:../conf/application.yml
set LOG_CONF_FILE=--logging.config=file:../conf/log4j2.xml

set SCOUTER_OPTS=-javaagent:"D:\AppHome\framework\dstone-boot\WorkShop\03.Tools\02.Scouter\scouter\agent.java\scouter.agent.jar"
set SCOUTER_OPTS=%SCOUTER_OPTS% -Dscouter.config="D:\AppHome\framework\dstone-boot\WorkShop\03.Tools\02.Scouter\scouter\agent.java\conf\scouter.conf"
set SCOUTER_OPTS=%SCOUTER_OPTS% -Dobj_name=dstone

rem start /B javaw %JAVA_OPTS% -jar %JAR_FILE% net.dstone.DstoneBootApplication %APP_CONF_FILE%
java %JAVA_OPTS% %SCOUTER_OPTS% -jar %JAR_FILE% net.dstone.DstoneBootApplication %APP_CONF_FILE% %LOG_CONF_FILE%
