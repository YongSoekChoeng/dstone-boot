package net.dstone.common.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import net.dstone.common.utils.LogUtil;

@Configuration
public class ConfigMapper {

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigMapper.class);

	@Bean(name = "sqlSessionFactoryCommon")
	public SqlSessionFactory sqlSessionFactoryCommon(@Qualifier("dataSourceCommon") DataSource dataSourceCommon) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceCommon);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/common/**/*Dao.xml"));
		return bean.getObject();
	}
	@Bean(name = "sqlSessionCommon")
	public SqlSessionTemplate sqlSessionCommon(@Qualifier("sqlSessionFactoryCommon") SqlSessionFactory sqlSessionFactoryCommon) {
		return new SqlSessionTemplate(sqlSessionFactoryCommon);
	}

	@Bean(name = "sqlSessionFactorySample")
	public SqlSessionFactory sqlSessionFactorySample(@Qualifier("dataSourceSample") DataSource dataSourceSample) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceSample);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/sample/**/*Dao.xml"));
		return bean.getObject();
	}
	@Bean(name = "sqlSessionSample")
	public SqlSessionTemplate sqlSessionSample(@Qualifier("sqlSessionFactorySample") SqlSessionFactory sqlSessionFactorySample) {
		return new SqlSessionTemplate(sqlSessionFactorySample);
	}

	@Bean(name = "sqlSessionFactorySampleOracle")
	public SqlSessionFactory sqlSessionFactorySampleOracle(@Qualifier("dataSourceSampleOracle") DataSource dataSourceSampleOracle) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceSampleOracle);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/sample/**/*Dao.xml"));
		return bean.getObject();
	}
	@Bean(name = "sqlSessionSampleOracle")
	public SqlSessionTemplate sqlSessionSampleOracle(@Qualifier("sqlSessionFactorySampleOracle") SqlSessionFactory sqlSessionFactorySampleOracle) {
		return new SqlSessionTemplate(sqlSessionFactorySampleOracle);
	}

	@Bean(name = "sqlSessionFactoryAnalyzer")
	public SqlSessionFactory sqlSessionFactoryAnalyzer(@Qualifier("dataSourceAnalyzer") DataSource dataSourceAnalyzer) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceAnalyzer);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/analyzer/**/*Dao.xml"));
		return bean.getObject();
	}
	@Bean(name = "sqlSessionAnalyzer")
	public SqlSessionTemplate sqlSessionAnalyzer(@Qualifier("sqlSessionFactoryAnalyzer") SqlSessionFactory sqlSessionFactoryAnalyzer) {
		return new SqlSessionTemplate(sqlSessionFactoryAnalyzer);
	}

}
