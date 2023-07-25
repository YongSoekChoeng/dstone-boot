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
	public SqlSessionFactory sqlSessionFactoryEdb(@Qualifier("dataSourceDb1") DataSource dataSourceDb1) throws Exception {
		PathMatchingResourcePatternResolver pmrpr = new PathMatchingResourcePatternResolver();
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSourceDb1);
		bean.setConfigLocation(pmrpr.getResource("classpath:/sqlmap/sql-mapper-config.xml"));
		bean.setMapperLocations(pmrpr.getResources("classpath:/sqlmap/**/*Dao.xml"));
		return bean.getObject();
	}
	
	@Bean
	public SqlSessionTemplate sqlSessionDb1(SqlSessionFactory sqlSessionFactoryDb1) {
		return new SqlSessionTemplate(sqlSessionFactoryDb1);
	}

}
