package net.dstone.common.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import net.dstone.common.utils.LogUtil;

@Configuration
public class ConfigTransaction {

	private static final LogUtil logger = new LogUtil(ConfigTransaction.class);

	/********************************************************************************
	Datasource/Transaction 설정
	********************************************************************************/
	private int TX_METHOD_TIMEOUT = 30;
	private static String AOP_POINTCUT_EXPRESSION = "execution(public * net.dstone.*..*ServiceImpl.*(..))";
	

	/********************************************************************************
	1. DB1 관련 설정
	********************************************************************************/
	@Bean
	@Qualifier("txManagerDb1")
	public DataSourceTransactionManager txManagerDb1(@Qualifier("dataSourceDb1") DataSource dataSourceDb1) {
		DataSourceTransactionManager txManagerDb1 = new DataSourceTransactionManager(dataSourceDb1);
		return txManagerDb1;
	}

	@Bean
	@Qualifier("txAdviceDb1")
	public TransactionInterceptor txAdviceDb1(DataSourceTransactionManager txManagerDb1) {
	    TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		Properties txAttributes = new Properties();
		List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
		/** If need to add additionall exception, add here **/
		rollbackRules.add(new RollbackRuleAttribute(Exception.class));
		// read-only
		DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute( TransactionDefinition.PROPAGATION_SUPPORTS);
		readOnlyAttribute.setReadOnly(true);
		readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);
		String readOnlyTransactionAttributesDefinition = readOnlyAttribute.toString();
		txAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
		txAttributes.setProperty("select*", readOnlyTransactionAttributesDefinition);
		txAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
		// write rollback-rule
		RuleBasedTransactionAttribute writeAttribute = new RuleBasedTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
		writeAttribute.setTimeout(TX_METHOD_TIMEOUT);
		String writeTransactionAttributesDefinition = writeAttribute.toString();
		txAttributes.setProperty("insert*", writeTransactionAttributesDefinition);
		txAttributes.setProperty("update*", writeTransactionAttributesDefinition);
		txAttributes.setProperty("delete*", writeTransactionAttributesDefinition);
		
		transactionInterceptor.setTransactionAttributes(txAttributes);
		transactionInterceptor.setTransactionManager(txManagerDb1);
	    return transactionInterceptor;
	}

	@Bean
	public Advisor txAdvisorDb1(@Qualifier("txManagerDb1") DataSourceTransactionManager txManagerDb1) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, txAdviceDb1(txManagerDb1));
	}

}
