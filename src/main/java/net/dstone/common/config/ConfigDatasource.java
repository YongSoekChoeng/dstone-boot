package net.dstone.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.zaxxer.hikari.HikariDataSource;

import net.dstone.common.utils.LogUtil;

@Configuration
public class ConfigDatasource {

	@Autowired 
	ConfigProperty configProperty; // 프로퍼티 가져오는 bean

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigDatasource.class);

    @Bean(name = "dataSourceCommon")
    @ConfigurationProperties("spring.datasource.common.hikari")
    public DataSource dataSourceCommon() {
    	if( "Y".equals(configProperty.getProperty("use-jndi-lookup")) ) {
    		return (new JndiDataSourceLookup()).getDataSource(configProperty.getProperty("jndi-lookup-name"));
    	}else {
    		return DataSourceBuilder.create().type(HikariDataSource.class).build();
    	}
    }

    @Bean(name = "dataSourceSample")
    @ConfigurationProperties("spring.datasource.sample.hikari")
    public DataSource dataSourceSample() {
    	if( "Y".equals(configProperty.getProperty("use-jndi-lookup")) ) {
    		return (new JndiDataSourceLookup()).getDataSource(configProperty.getProperty("jndi-lookup-name"));
    	}else {
    		return DataSourceBuilder.create().type(HikariDataSource.class).build();
    	}
    }

    @Bean(name = "dataSourceSampleOracle")
    @ConfigurationProperties("spring.datasource.sample-oracle.hikari")
    public DataSource dataSourceSampleOracle() {
    	if( "Y".equals(configProperty.getProperty("use-jndi-lookup")) ) {
    		return (new JndiDataSourceLookup()).getDataSource(configProperty.getProperty("jndi-lookup-name"));
    	}else {
    		return DataSourceBuilder.create().type(HikariDataSource.class).build();
    	}
    }

    @Bean(name = "dataSourceAnalyzer")
    @ConfigurationProperties("spring.datasource.analyzer.hikari")
    public DataSource dataSourceAnalyzer() {
    	if( "Y".equals(configProperty.getProperty("use-jndi-lookup")) ) {
    		return (new JndiDataSourceLookup()).getDataSource(configProperty.getProperty("jndi-lookup-name"));
    	}else {
    		return DataSourceBuilder.create().type(HikariDataSource.class).build();
    	}
    }
    
}
