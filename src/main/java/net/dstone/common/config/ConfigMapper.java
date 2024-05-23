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

	@Bean
	public SqlSessionFactory sqlSessionFactoryCommon(@Qualifier("dataSourceCommon") DataSource dataSourceCommon) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceCommon);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/common/**/*Dao.xml"));
		return bean.getObject();
	}
	
	@Bean
	@Qualifier("sqlSessionCommon") 
	public SqlSessionTemplate sqlSessionCommon(SqlSessionFactory sqlSessionFactoryCommon) {
		return new SqlSessionTemplate(sqlSessionFactoryCommon);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactorySample(@Qualifier("dataSourceSample") DataSource dataSourceSample) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceSample);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/sample/**/*Dao.xml"));
		return bean.getObject();
	}
	
	@Bean
	@Qualifier("sqlSessionSample") 
	public SqlSessionTemplate sqlSessionSample(SqlSessionFactory sqlSessionFactorySample) {
		return new SqlSessionTemplate(sqlSessionFactorySample);
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryAnalyzer(@Qualifier("dataSourceAnalyzer") DataSource dataSourceAnalyzer) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceAnalyzer);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/analyzer/**/*Dao.xml"));
		return bean.getObject();
	}
	
	@Bean
	@Qualifier("sqlSessionFactoryAnalyzer") 
	public SqlSessionTemplate sqlSessionAnalyzer(SqlSessionFactory sqlSessionFactoryAnalyzer) {
		return new SqlSessionTemplate(sqlSessionFactoryAnalyzer);
	}

}
