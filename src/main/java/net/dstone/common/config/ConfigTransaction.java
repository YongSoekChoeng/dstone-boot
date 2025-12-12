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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import net.dstone.common.core.BaseObject;

@Component
public class ConfigTransaction extends BaseObject{

	/********************************************************************************
	Datasource/Transaction 설정
	********************************************************************************/
	private int TX_METHOD_TIMEOUT = 30;
	private static String AOP_POINTCUT_EXPRESSION = "execution(public * net.dstone.*..*ServiceImpl.*(..))";
	

	/********************************************************************************
	1. Common 관련 설정
	********************************************************************************/
	@Bean(name = "txManagerCommon")
	public DataSourceTransactionManager txManagerCommon(@Qualifier("dataSourceCommon") DataSource dataSourceCommon) {
		DataSourceTransactionManager txManagerCommon = new DataSourceTransactionManager(dataSourceCommon);
		return txManagerCommon;
	}

	@Bean(name = "txAdviceCommon")
	public TransactionInterceptor txAdviceCommon(DataSourceTransactionManager txManagerCommon) {
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
		transactionInterceptor.setTransactionManager(txManagerCommon);
	    return transactionInterceptor;
	}

	@Bean(name = "txAdvisorCommon")
	public Advisor txAdvisorCommon(@Qualifier("txManagerCommon") DataSourceTransactionManager txManagerCommon) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, txAdviceCommon(txManagerCommon));
	}

	/********************************************************************************
	2. Sample 관련 설정
	********************************************************************************/
	@Bean(name = "txManagerSample")
	public DataSourceTransactionManager txManagerSample(@Qualifier("dataSourceSample") DataSource dataSourceSample) {
		DataSourceTransactionManager txManagerSample = new DataSourceTransactionManager(dataSourceSample);
		return txManagerSample;
	}

	@Bean(name = "txAdviceSample")
	public TransactionInterceptor txAdviceSample(DataSourceTransactionManager txManagerSample) {
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
		transactionInterceptor.setTransactionManager(txManagerSample);
	    return transactionInterceptor;
	}

	@Bean(name = "txAdvisorSample")
	public Advisor txAdvisorSample(@Qualifier("txManagerSample") DataSourceTransactionManager txManagerSample) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, txAdviceSample(txManagerSample));
	}

	/********************************************************************************
	3. Sample-Oracle 관련 설정
	********************************************************************************/
//	@Bean(name = "txManagerSampleOracle")
//	public DataSourceTransactionManager txManagerSampleOracle(@Qualifier("dataSourceSampleOracle") DataSource dataSourceSampleOracle) {
//		DataSourceTransactionManager txManagerSampleOracle = new DataSourceTransactionManager(dataSourceSampleOracle);
//		return txManagerSampleOracle;
//	}

//	@Bean(name = "txAdviceSampleOracle")
//	public TransactionInterceptor txAdviceSampleOracle(DataSourceTransactionManager txManagerSampleOracle) {
//	    TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
//		Properties txAttributes = new Properties();
//		List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
//		/** If need to add additionall exception, add here **/
//		rollbackRules.add(new RollbackRuleAttribute(Exception.class));
//		// read-only
//		DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute( TransactionDefinition.PROPAGATION_SUPPORTS);
//		readOnlyAttribute.setReadOnly(true);
//		readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);
//		String readOnlyTransactionAttributesDefinition = readOnlyAttribute.toString();
//		txAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
//		txAttributes.setProperty("select*", readOnlyTransactionAttributesDefinition);
//		txAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
//		// write rollback-rule
//		RuleBasedTransactionAttribute writeAttribute = new RuleBasedTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
//		writeAttribute.setTimeout(TX_METHOD_TIMEOUT);
//		String writeTransactionAttributesDefinition = writeAttribute.toString();
//		txAttributes.setProperty("insert*", writeTransactionAttributesDefinition);
//		txAttributes.setProperty("update*", writeTransactionAttributesDefinition);
//		txAttributes.setProperty("delete*", writeTransactionAttributesDefinition);
//		
//		transactionInterceptor.setTransactionAttributes(txAttributes);
//		transactionInterceptor.setTransactionManager(txManagerSampleOracle);
//	    return transactionInterceptor;
//	}

//	@Bean(name = "txAdvisorSampleOracle")
//	public Advisor txAdvisorSampleOracle(@Qualifier("txManagerSampleOracle") DataSourceTransactionManager txManagerSampleOracle) {
//		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
//		return new DefaultPointcutAdvisor(pointcut, txAdviceSampleOracle(txManagerSampleOracle));
//	}

	/********************************************************************************
	4. Sample-Postgresql 관련 설정
	********************************************************************************/
//	@Bean(name = "txManagerSamplePostgresql")
//	public DataSourceTransactionManager txManagerSamplePostgresql(@Qualifier("dataSourceSamplePostgresql") DataSource dataSourceSamplePostgresql) {
//		DataSourceTransactionManager txManagerSamplePostgresql = new DataSourceTransactionManager(dataSourceSamplePostgresql);
//		return txManagerSamplePostgresql;
//	}

//	@Bean(name = "txAdviceSamplePostgresql")
//	public TransactionInterceptor txAdviceSamplePostgresql(DataSourceTransactionManager txManagerSamplePostgresql) {
//	    TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
//		Properties txAttributes = new Properties();
//		List<RollbackRuleAttribute> rollbackRules = new ArrayList<RollbackRuleAttribute>();
//		/** If need to add additionall exception, add here **/
//		rollbackRules.add(new RollbackRuleAttribute(Exception.class));
//		// read-only
//		DefaultTransactionAttribute readOnlyAttribute = new DefaultTransactionAttribute( TransactionDefinition.PROPAGATION_SUPPORTS);
//		readOnlyAttribute.setReadOnly(true);
//		readOnlyAttribute.setTimeout(TX_METHOD_TIMEOUT);
//		String readOnlyTransactionAttributesDefinition = readOnlyAttribute.toString();
//		txAttributes.setProperty("get*", readOnlyTransactionAttributesDefinition);
//		txAttributes.setProperty("select*", readOnlyTransactionAttributesDefinition);
//		txAttributes.setProperty("list*", readOnlyTransactionAttributesDefinition);
//		// write rollback-rule
//		RuleBasedTransactionAttribute writeAttribute = new RuleBasedTransactionAttribute( TransactionDefinition.PROPAGATION_REQUIRED, rollbackRules);
//		writeAttribute.setTimeout(TX_METHOD_TIMEOUT);
//		String writeTransactionAttributesDefinition = writeAttribute.toString();
//		txAttributes.setProperty("insert*", writeTransactionAttributesDefinition);
//		txAttributes.setProperty("update*", writeTransactionAttributesDefinition);
//		txAttributes.setProperty("delete*", writeTransactionAttributesDefinition);
//		
//		transactionInterceptor.setTransactionAttributes(txAttributes);
//		transactionInterceptor.setTransactionManager(txManagerSamplePostgresql);
//	    return transactionInterceptor;
//	}

//	@Bean(name = "txAdvisorSamplePostgresql")
//	public Advisor txAdvisorSamplePostgresql(@Qualifier("txManagerSamplePostgresql") DataSourceTransactionManager txManagerSamplePostgresql) {
//		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
//		return new DefaultPointcutAdvisor(pointcut, txAdviceSamplePostgresql(txManagerSamplePostgresql));
//	}

	/********************************************************************************
	5. Analyzer 관련 설정
	********************************************************************************/
	@Bean(name = "txManagerAnalyzer")
	public DataSourceTransactionManager txManagerAnalyzer(@Qualifier("dataSourceAnalyzer") DataSource dataSourceAnalyzer) {
		DataSourceTransactionManager txManagerAnalyzer = new DataSourceTransactionManager(dataSourceAnalyzer);
		return txManagerAnalyzer;
	}

	@Bean(name = "txAdviceAnalyzer")
	public TransactionInterceptor txAdviceAnalyzer(DataSourceTransactionManager txManagerAnalyzer) {
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
		transactionInterceptor.setTransactionManager(txManagerAnalyzer);
	    return transactionInterceptor;
	}

	@Bean(name = "txAdvisorAnalyzer")
	public Advisor txAdvisorAnalyzer(@Qualifier("txManagerAnalyzer") DataSourceTransactionManager txManagerAnalyzer) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
		return new DefaultPointcutAdvisor(pointcut, txAdviceAnalyzer(txManagerAnalyzer));
	}

}
