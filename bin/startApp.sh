
JAVA_OPTS:="-Djava.ext.dirs=/DEV/JDK/1.8/jre/lib/ext:/Sun/Java/lib/ext:/AppHome/dstone-boot/extLib"
javaw %JAVA_OPTS% -jar ../target/dstone-boot-0.0.1-SNAPSHOT.war net.dstone.DstoneBootApplication

