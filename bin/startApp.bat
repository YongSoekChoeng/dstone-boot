
set JAVA_OPTS=-Djava.ext.dirs=C:/DEV/JDK/1.8/jre/lib/ext;C:/Windows/Sun/Java/lib/ext;D:/AppHome/dstone-boot/extLib
start /B javaw %JAVA_OPTS% -jar ../target/dstone-boot-0.0.1-SNAPSHOT.war net.dstone.DstoneBootApplication

