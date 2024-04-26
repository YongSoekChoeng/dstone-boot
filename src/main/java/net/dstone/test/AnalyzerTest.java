package net.dstone.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LiteralStringValueExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class AnalyzerTest extends VoidVisitorAdapter<Void> {

    public static void main(String[] args) throws Exception {


    	System.out.println("================ START ================");
    	
    	testAnalysis();
    	
    	//test();

    	System.out.println("================ END ================");
    }
    
    private static void d(Object o) {
    	System.out.println(o);
    }
    
	private static CombinedTypeSolver setClassPath(CombinedTypeSolver combinedTypeSolver) {
    	try {
    		
    		combinedTypeSolver.add(new JarTypeSolver(new File("C:/DEV/JDK/1.8/jre/lib/rt.jar")));

    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter-web/2.7.0/spring-boot-starter-web-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter/2.7.0/spring-boot-starter-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/yaml/snakeyaml/1.30/snakeyaml-1.30.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter-json/2.7.0/spring-boot-starter-json-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/datatype/jackson-datatype-jdk8/2.13.3/jackson-datatype-jdk8-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.13.3/jackson-datatype-jsr310-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/module/jackson-module-parameter-names/2.13.3/jackson-module-parameter-names-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-web/5.3.20/spring-web-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-webmvc/5.3.20/spring-webmvc-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-expression/5.3.20/spring-expression-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter-validation/2.7.0/spring-boot-starter-validation-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/tomcat/embed/tomcat-embed-el/9.0.63/tomcat-embed-el-9.0.63.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/hibernate/validator/hibernate-validator/6.2.3.Final/hibernate-validator-6.2.3.Final.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/jakarta/validation/jakarta.validation-api/2.0.2/jakarta.validation-api-2.0.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/jboss/logging/jboss-logging/3.4.3.Final/jboss-logging-3.4.3.Final.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/classmate/1.5.1/classmate-1.5.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter-tomcat/2.7.0/spring-boot-starter-tomcat-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/jakarta/annotation/jakarta.annotation-api/1.3.5/jakarta.annotation-api-1.3.5.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/tomcat/embed/tomcat-embed-core/9.0.63/tomcat-embed-core-9.0.63.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/tomcat/embed/tomcat-embed-websocket/9.0.63/tomcat-embed-websocket-9.0.63.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter-security/2.7.0/spring-boot-starter-security-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-aop/5.3.20/spring-aop-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/security/spring-security-config/5.7.1/spring-security-config-5.7.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/security/spring-security-core/5.7.1/spring-security-core-5.7.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/security/spring-security-crypto/5.7.1/spring-security-crypto-5.7.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/security/spring-security-web/5.7.1/spring-security-web-5.7.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-configuration-processor/2.7.0/spring-boot-configuration-processor-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter-log4j2/2.7.0/spring-boot-starter-log4j2-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/logging/log4j/log4j-slf4j-impl/2.17.2/log4j-slf4j-impl-2.17.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/logging/log4j/log4j-jul/2.17.2/log4j-jul-2.17.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/slf4j/jul-to-slf4j/1.7.36/jul-to-slf4j-1.7.36.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-starter-cache/2.7.0/spring-boot-starter-cache-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-context-support/5.3.20/spring-context-support-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/jakarta/xml/bind/jakarta.xml.bind-api/2.3.3/jakarta.xml.bind-api-2.3.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/jakarta/activation/jakarta.activation-api/1.2.2/jakarta.activation-api-1.2.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-core/5.3.20/spring-core-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-jcl/5.3.20/spring-jcl-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-devtools/2.7.0/spring-boot-devtools-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot/2.7.0/spring-boot-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/boot/spring-boot-autoconfigure/2.7.0/spring-boot-autoconfigure-2.7.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-jdbc/5.3.20/spring-jdbc-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-beans/5.3.20/spring-beans-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-tx/5.3.20/spring-tx-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/zaxxer/HikariCP/4.0.3/HikariCP-4.0.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/hsqldb/hsqldb/2.5.2/hsqldb-2.5.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/AppHome/framework/dstone-boot/src/main/webapp/WEB-INF/lib/ojdbc8-12.2.0.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/commons/commons-dbcp2/2.9.0/commons-dbcp2-2.9.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/commons/commons-pool2/2.11.1/commons-pool2-2.11.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/mybatis/mybatis-spring/1.3.0/mybatis-spring-1.3.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/mybatis/mybatis/3.4.1/mybatis-3.4.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/javax/servlet/jsp/javax.servlet.jsp-api/2.2.1/javax.servlet.jsp-api-2.2.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/javax/servlet/jsp/jstl/javax.servlet.jsp.jstl-api/1.2.1/javax.servlet.jsp.jstl-api-1.2.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/tomcat/embed/tomcat-embed-jasper/9.0.63/tomcat-embed-jasper-9.0.63.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/eclipse/jdt/ecj/3.18.0/ecj-3.18.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/taglibs/standard/1.1.2/standard-1.1.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/aspectj/aspectjrt/1.9.7/aspectjrt-1.9.7.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/aspectj/aspectjweaver/1.9.7/aspectjweaver-1.9.7.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/aspectj/aspectjtools/1.9.7/aspectjtools-1.9.7.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/googlecode/json-simple/json-simple/1.1/json-simple-1.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/codehaus/jackson/jackson-jaxrs/1.9.13/jackson-jaxrs-1.9.13.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/codehaus/jackson/jackson-core-asl/1.9.13/jackson-core-asl-1.9.13.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/codehaus/jackson/jackson-mapper-asl/1.9.13/jackson-mapper-asl-1.9.13.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/core/jackson-core/2.13.3/jackson-core-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/core/jackson-databind/2.13.3/jackson-databind-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/core/jackson-annotations/2.13.3/jackson-annotations-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/commons-beanutils/commons-beanutils/1.9.3/commons-beanutils-1.9.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/commons-logging/commons-logging/1.2/commons-logging-1.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/commons-collections/commons-collections/3.2.2/commons-collections-3.2.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/commons-lang/commons-lang/2.6/commons-lang-2.6.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/servlets/com/cos/05Nov2002/cos-05Nov2002.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/projectlombok/lombok/1.18.24/lombok-1.18.24.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/commons-httpclient/commons-httpclient/3.1/commons-httpclient-3.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/commons-codec/commons-codec/1.15/commons-codec-1.15.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/httpcomponents/httpcore/4.4.15/httpcore-4.4.15.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/logging/log4j/log4j-core/2.16.0/log4j-core-2.16.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/logging/log4j/log4j-api/2.16.0/log4j-api-2.16.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/bgee/log4jdbc-log4j2/log4jdbc-log4j2-jdbc4.1/1.16/log4jdbc-log4j2-jdbc4.1-1.16.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/ulisesbocchio/jasypt-spring-boot-starter/3.0.4/jasypt-spring-boot-starter-3.0.4.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/ulisesbocchio/jasypt-spring-boot/3.0.4/jasypt-spring-boot-3.0.4.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/jasypt/jasypt/1.9.3/jasypt-1.9.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/AppHome/framework/dstone-boot/src/main/webapp/WEB-INF/lib/bcprov-ext-jdk18on-175.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/spotify/docker-client/8.15.1/docker-client-8.15.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/google/guava/guava/20.0/guava-20.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/jaxrs/jackson-jaxrs-json-provider/2.13.3/jackson-jaxrs-json-provider-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/jaxrs/jackson-jaxrs-base/2.13.3/jackson-jaxrs-base-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/module/jackson-module-jaxb-annotations/2.13.3/jackson-module-jaxb-annotations-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/fasterxml/jackson/datatype/jackson-datatype-guava/2.13.3/jackson-datatype-guava-2.13.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/glassfish/jersey/core/jersey-client/2.35/jersey-client-2.35.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/jakarta/ws/rs/jakarta.ws.rs-api/2.1.6/jakarta.ws.rs-api-2.1.6.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/glassfish/jersey/core/jersey-common/2.35/jersey-common-2.35.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/glassfish/hk2/osgi-resource-locator/1.0.3/osgi-resource-locator-1.0.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/glassfish/hk2/external/jakarta.inject/2.6.1/jakarta.inject-2.6.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/glassfish/jersey/connectors/jersey-apache-connector/2.35/jersey-apache-connector-2.35.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/glassfish/jersey/media/jersey-media-json-jackson/2.35/jersey-media-json-jackson-2.35.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/glassfish/jersey/ext/jersey-entity-filtering/2.35/jersey-entity-filtering-2.35.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/javax/activation/activation/1.1/activation-1.1.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/commons/commons-compress/1.18/commons-compress-1.18.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/commons-io/commons-io/2.5/commons-io-2.5.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/httpcomponents/httpclient/4.5.13/httpclient-4.5.13.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jnr-unixsocket/0.18/jnr-unixsocket-0.18.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jnr-ffi/2.1.4/jnr-ffi-2.1.4.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jffi/1.2.15/jffi-1.2.15.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jffi/1.2.15/jffi-1.2.15-native.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/ow2/asm/asm/5.0.3/asm-5.0.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/ow2/asm/asm-commons/5.0.3/asm-commons-5.0.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/ow2/asm/asm-analysis/5.0.3/asm-analysis-5.0.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/ow2/asm/asm-tree/5.0.3/asm-tree-5.0.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/ow2/asm/asm-util/5.0.3/asm-util-5.0.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jnr-x86asm/1.0.2/jnr-x86asm-1.0.2.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jnr-constants/0.9.8/jnr-constants-0.9.8.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jnr-enxio/0.16/jnr-enxio-0.16.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jnr/jnr-posix/3.0.35/jnr-posix-3.0.35.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/bouncycastle/bcpkix-jdk15on/1.60/bcpkix-jdk15on-1.60.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/bouncycastle/bcprov-jdk15on/1.60/bcprov-jdk15on-1.60.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/io/github/benas/jpopulator/1.2.0/jpopulator-1.2.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/commons/commons-lang3/3.12.0/commons-lang3-3.12.0.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/apache/commons/commons-math3/3.3/commons-math3-3.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/javax/validation/validation-api/2.0.1.Final/validation-api-2.0.1.Final.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/joda-time/joda-time/2.6/joda-time-2.6.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/springframework/spring-context/5.3.20/spring-context-5.3.20.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/jsoup/jsoup/1.11.3/jsoup-1.11.3.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/jsqlparser/jsqlparser/4.7/jsqlparser-4.7.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/javaparser/javaparser-core/3.25.7/javaparser-core-3.25.7.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/com/github/javaparser/javaparser-symbol-solver-core/3.25.7/javaparser-symbol-solver-core-3.25.7.jar")));
    		combinedTypeSolver.add(new JarTypeSolver(new File("D:/REPO/MAVEN/org/javassist/javassist/3.29.2-GA/javassist-3.29.2-GA.jar")));
    		
    		combinedTypeSolver.add(new JavaParserTypeSolver(new File("D:/AppHome/framework/dstone-boot/src/main/java")));

    		combinedTypeSolver.add(new ReflectionTypeSolver());

		} catch (Exception e) {
			e.printStackTrace();
		}

    	return combinedTypeSolver;
    }
	
	public static void testAnalysis() throws Exception {

        String srcRoot = "D:/AppHome/framework/dstone-boot/src/main/java";
    	String path = srcRoot + "/" + "net/dstone/sample/analyze";
    	
		
		JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver (AnalyzerTest.setClassPath(new CombinedTypeSolver())); 
		ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(javaSymbolSolver);
	
		StaticJavaParser.setConfiguration(parserConfiguration);
		JavaParser parser = new JavaParser (parserConfiguration);
		String[] filelist = FileUtil.readFileListAll(path); 
		String clzzNm = "";
		ClassOrInterfaceDeclaration clzzDec = null;
		List<MethodDeclaration> mtdDecList = null;
		
		ScanResult scanResult = null;
		
		try {
			
			scanResult = new ClassGraph().enableAllInfo().acceptPackages( StringUtil.replace( StringUtil.replace(path, srcRoot+"/", ""), "/", ".") ).scan();

			for (String file: filelist) {
				d("||------------------------------------------" + file + "-------------------------------------||");
				
				/*** A. 클래스 정보조회 ***/
				CompilationUnit clzzCU = parser.parse(FileUtil.readFile(file)).getResult().get();
				clzzDec = clzzCU.findFirst(ClassOrInterfaceDeclaration.class).get();
				clzzNm = clzzDec.resolve().getQualifiedName();
				d("ID:" + clzzNm);

				/*** A-1. 클래스멤버 목록조회 ***/
				HashMap<String, ArrayList<ClassOrInterfaceDeclaration>> clzzMemberMap = new HashMap<String, ArrayList<ClassOrInterfaceDeclaration>>(); 
				List<ResolvedFieldDeclaration> clzzFieldList = clzzDec.resolve().getAllFields();
				
				
				for (ResolvedFieldDeclaration clzzField: clzzFieldList) {
					String name = clzzField.getName();
					ClassOrInterfaceDeclaration clzzFieldClzz = ParseUtil.getClassDec(parser, srcRoot, clzzField.getType().describe());
					if( clzzFieldClzz != null) {
						if(!clzzMemberMap.containsKey(name)) {
							clzzMemberMap.put(name, new ArrayList<ClassOrInterfaceDeclaration>()); 
						}
						ArrayList<ClassOrInterfaceDeclaration> valClzzList = clzzMemberMap.get(name);
						valClzzList.add(clzzFieldClzz);
						d("\t" + "클래스멤버:" + name + ", 클래스멤버타입:" + clzzFieldClzz.resolve().getQualifiedName() + ", aa:" + clzzField.toAst(FieldDeclaration.class) );
						
					}
				}

				/*** B. 메서드 목록조회 ***/
				mtdDecList = clzzDec.getMethods();
				for (MethodDeclaration mtdDec : mtdDecList) {
					/*** B-1. 메서드 정보조회 ***/
//					d("\t" + "메서드ID:" + mtdDec.resolve().getQualifiedSignature());
					
					/***B-2. 메서드멤버 목록조회 ***/
					HashMap<String, ArrayList<ClassOrInterfaceDeclaration>> mtdMemberMap = new HashMap<String, ArrayList<ClassOrInterfaceDeclaration>>(); 
					List<VariableDeclarationExpr> varList = mtdDec.findAll(VariableDeclarationExpr.class);
					for (VariableDeclarationExpr var: varList) {
						String name = var.getVariable (0).getNameAsString(); 
						ClassOrInterfaceDeclaration valClzz = ParseUtil.getClassDec (parser, srcRoot, var.calculateResolvedType().describe());
						if( valClzz != null) {
							if( !valClzz.isInterface() && !valClzz.isAbstract()) {
								if(!mtdMemberMap.containsKey(name)) {
									mtdMemberMap.put(name, new ArrayList<ClassOrInterfaceDeclaration>());
								}
								ArrayList<ClassOrInterfaceDeclaration> valClzzList = mtdMemberMap.get(name);
								valClzzList.add(valClzz);
//								d("\t\t" + "메서드멤버:" + name + ", 메서드멤버타입:" + valClzz.resolve().getQualifiedName() );
							}
						}
					}

					/***B-3. 메서드멤버생성 목록조회 ***/
					List<AssignExpr> assignList = mtdDec.findAll(AssignExpr.class);
					for (AssignExpr assign : assignList) {
						ObjectCreationExpr objCre = null;
						if( assign.findFirst(ObjectCreationExpr.class).isPresent()) {
							objCre = assign.findFirst(ObjectCreationExpr.class).get();
							String name = assign.getTarget().toString();
							ClassOrInterfaceDeclaration valClzz = ParseUtil.getClassDec(parser, srcRoot, objCre.getType().resolve().describe());
							if( valClzz != null) {
								if(!mtdMemberMap.containsKey(name)) {
									mtdMemberMap.put(name, new ArrayList<ClassOrInterfaceDeclaration>());
								}
								ArrayList<ClassOrInterfaceDeclaration> valClzzList = mtdMemberMap.get(name);
								valClzzList.add(valClzz);
//								d("\t\t" + "메서드멤버생성:" + name + ", 메서드멤버생성타입:" + valClzz.resolve().getQualifiedName() );
							}
						}
					}
					
					/*** B-4. 메서드 호출 목록조회 ***/
					ArrayList<String> mtdCallList = new ArrayList<String>(); 
					List<MethodCallExpr> meCallList = mtdDec.findAll(MethodCallExpr.class);
					for (MethodCallExpr meCall : meCallList) {
						ResolvedMethodDeclaration mtdResolved = meCall.resolve(); 
						ClassOrInterfaceDeclaration valClzz = null; 
						Expression callerExp = null;
						
						String methodQualifiedSignature = mtdResolved.getQualifiedSignature(); 
						String clzzQualifiedName = "";
						String methodSignature = "";
						
						clzzQualifiedName = methodQualifiedSignature.substring(0, methodQualifiedSignature.indexOf("("));
						clzzQualifiedName = clzzQualifiedName.substring(0, clzzQualifiedName.lastIndexOf(".")); 
						methodSignature = StringUtil.replace(methodQualifiedSignature, clzzQualifiedName+".", "");
						
//						d("\t\t" + "메서드호출(가공전): " + methodQualifiedSignature );
						valClzz = ParseUtil.getClassDec (parser, srcRoot, clzzQualifiedName);
						if( valClzz != null) {

							// 클래스 일 경우 (MethodCallExpr 자체적으로 구현 클래스.메서드 등 찾을 수 있음)
							if( !valClzz.isInterface()) {
								/*** Class-Type. 메서드호출 목록조회 ***/
								List<MethodDeclaration> methodList = valClzz.getMethods();
								for (MethodDeclaration mDec : methodList) {
									if(mDec.resolve().getQualifiedSignature().endsWith("." + methodSignature) ) { 
										if( !mtdCallList.contains(mDec.resolve().getQualifiedSignature())) {
//											d("\t\t" + "Class-Type 메서드호출:"+ mDec.resolve().getQualifiedSignature() );
											mtdCallList.add(mDec.resolve().getQualifiedSignature()); 
											break;
										}
									}
								}
							// 인터페이스 일 경우
							}else {
								if(meCall.getScope().isPresent()) { 
									callerExp = meCall.getScope().get();
									String name= callerExp.toString(); 
									
									/*** Interface-Type. 메서드멤버.메서드호출 목록조회 **/
									if( mtdMemberMap.containsKey(name)) {
										ArrayList<ClassOrInterfaceDeclaration> mcList = mtdMemberMap.get(name);
										for (ClassOrInterfaceDeclaration mc : mcList) {
											Iterator<MethodUsage> mu = mc.resolve().getAllMethods().iterator(); 
											while(mu.hasNext()) {
												MethodUsage mur = mu.next();
												if( mur.getQualifiedSignature().endsWith("."+methodSignature)) {
													if(!mtdCallList.contains(mur.getQualifiedSignature())) {
//														d("\t\t" + "Interface-Type 메서드멤버.메서드호출 :"+mur.getQualifiedSignature() ); 
														mtdCallList.add(mur.getQualifiedSignature());
														break;
													}
												}
											}
										}
									}

									/*** Interface-Type. 클래스멤버.메서드호출 목록조회 **/
									if( clzzMemberMap.containsKey(name)) {
										ArrayList<ClassOrInterfaceDeclaration> mcList = clzzMemberMap.get(name);
										for (ClassOrInterfaceDeclaration mc : mcList) {
											Iterator<MethodUsage> mu = mc.resolve().getAllMethods().iterator(); 
											while(mu.hasNext()) {
												MethodUsage mur = mu.next();
												if( mur.getQualifiedSignature().endsWith("."+methodSignature)) {
													if(!mtdCallList.contains(mur.getQualifiedSignature())) {
//														d("\t\t" + "Interface-Type 클래스멤버.메서드호출 :"+mur.getQualifiedSignature() ); 
														mtdCallList.add(mur.getQualifiedSignature());
														break;
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}

			}
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(scanResult!=null) {scanResult.close();}
		}
		
	}
	
	private static void test() {
		
    	String param = "TestDao2";
    	
		List<String> implClassList = new ArrayList<String>();
		ScanResult scanResult = null;
		try {
			scanResult = new ClassGraph().enableAllInfo().acceptPackages("net.dstone.sample.analyze").scan();
			
//d( "param["+param+"] ==>>" + scanResult.getAnnotationsOnClass("net.dstone.sample.analyze.TestServiceImpl") );			
			for (ClassInfo ci : scanResult.getAnnotationsOnClass("net.dstone.sample.analyze.TestServiceImpl") ) {
				d(ci.getAnnotationInfo().getAsStringsWithSimpleNames() );
		    }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(scanResult!=null) {scanResult.close();}
		}
	}
}


