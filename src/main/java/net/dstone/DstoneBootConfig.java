package net.dstone;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.annotation.Aspect;
import org.jasypt.encryption.StringEncryptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import com.zaxxer.hikari.HikariDataSource;

@Aspect
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class DstoneBootConfig {
	
	/********************************************************************************
	1. Application 설정
	********************************************************************************/
	// 1-1. 암복호화 설정.
	@Bean("jasyptStringEncryptor") 
	public StringEncryptor stringEncryptor() { 
        return net.dstone.common.utils.EncUtil.getEncryptor();
	} 

	/********************************************************************************
	2. Sql/Datasource/Transaction 설정
	********************************************************************************/
	private int TX_METHOD_TIMEOUT = 30;
	private static String AOP_POINTCUT_EXPRESSION = "execution(public * net.dstone.*..*Service.*(..))";
	
	// DB1 관련 설정
	/* 2-1. Datasource Configuration START */
    @Bean
	@Qualifier("dataSource1")
    @ConfigurationProperties("spring.db1.datasource.hikari")
    public DataSource dataSource1() {
    	//return DataSourceBuilder.create().build();
    	return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
	/* 2-2. SqlMap Configuration START */
	@Bean
	public SqlSessionFactory sqlSessionFactory1(DataSource dataSource1) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath*:**/*Dao.xml");
		bean.setDataSource(dataSource1);
		bean.setMapperLocations(mapperLocations);
		return bean.getObject();
	}
	@Bean
	public SqlSessionTemplate sqlSession1(SqlSessionFactory sqlSessionFactory1) {
		return new SqlSessionTemplate(sqlSessionFactory1);
	}
	/* 2-3. Transaction Configuration START */
	@Bean
	@Qualifier("txManager1")
	@Primary
	public DataSourceTransactionManager txManager1() {
		DataSourceTransactionManager txManager1 = new DataSourceTransactionManager(dataSource1());
	    return txManager1;
	}
	@Bean
	@Qualifier("txAdvice1")
	public TransactionInterceptor txAdvice1() {
	    TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		Properties txAttributes = new Properties();
		List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
		/** If need to add additionall exception, add here **/
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));
		// read-only
		DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRED);
		readOnlyAttribute.setReadOnly(true);
		readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);
		String readOnlyTransactionAttributesDefinition = readOnlyAttribute.toString();
		txAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
		txAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
		// write rollback-rule
		RuleBasedTransactionAttribute writeAttribute = new RuleBasedTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		writeAttribute.setTimeout(TX_METHOD_TIMEOUT);
		String writeTransactionAttributesDefinition = writeAttribute.toString();
		txAttributes.setProperty("insert*", writeTransactionAttributesDefinition);
		txAttributes.setProperty("update*", writeTransactionAttributesDefinition);
		txAttributes.setProperty("delete", writeTransactionAttributesDefinition);
		
		transactionInterceptor.setTransactionAttributes(txAttributes);
		transactionInterceptor.setTransactionManager(txManager1());
	    return transactionInterceptor;
	}
	@Bean
	public Advisor transactionAdvisor1() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, txAdvice1());
	}
	

	// DB2 관련 설정
	/* 2-1. Datasource Configuration START */
    @Bean
	@Qualifier("dataSource1")
    @ConfigurationProperties("spring.db2.datasource.hikari")
    public DataSource dataSource2() {
    	//return DataSourceBuilder.create().build();
    	return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
	/* 2-2. SqlMap Configuration START */
	@Bean
	public SqlSessionFactory sqlSessionFactory2(DataSource dataSource2) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		Resource[] mapperLocations = new PathMatchingResourcePatternResolver().getResources("classpath*:**/*Dao.xml");
		bean.setDataSource(dataSource2);
		bean.setMapperLocations(mapperLocations);
		return bean.getObject();
	}
	@Bean
	public SqlSessionTemplate sqlSession2(SqlSessionFactory sqlSessionFactory2) {
		return new SqlSessionTemplate(sqlSessionFactory2);
	}
	/* 2-3. Transaction Configuration START */
	@Bean
	@Qualifier("txManager2")
	public DataSourceTransactionManager txManager2() {
		DataSourceTransactionManager txManager2 = new DataSourceTransactionManager(dataSource2());
	    return txManager2;
	}
	@Bean
	@Qualifier("txAdvice2")
	public TransactionInterceptor txAdvice2() {
	    TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		Properties txAttributes = new Properties();
		List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
		/** If need to add additionall exception, add here **/
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));
		// read-only
		DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRED);
		readOnlyAttribute.setReadOnly(true);
		readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);
		String readOnlyTransactionAttributesDefinition = readOnlyAttribute.toString();
		txAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
		txAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
		// write rollback-rule
		RuleBasedTransactionAttribute writeAttribute = new RuleBasedTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		writeAttribute.setTimeout(TX_METHOD_TIMEOUT);
		String writeTransactionAttributesDefinition = writeAttribute.toString();
		txAttributes.setProperty("insert*", writeTransactionAttributesDefinition);
		txAttributes.setProperty("update*", writeTransactionAttributesDefinition);
		txAttributes.setProperty("delete", writeTransactionAttributesDefinition);
		
		transactionInterceptor.setTransactionAttributes(txAttributes);
		transactionInterceptor.setTransactionManager(txManager2());
	    return transactionInterceptor;
	}
	@Bean
	public Advisor transactionAdvisor2() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, txAdvice2());
	}
	
}
