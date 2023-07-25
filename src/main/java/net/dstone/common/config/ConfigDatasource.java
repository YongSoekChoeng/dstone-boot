package net.dstone.common.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.zaxxer.hikari.HikariDataSource;

import net.dstone.common.utils.LogUtil;

@Configuration
public class ConfigDatasource {

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigDatasource.class);

    @Bean
	@Qualifier("dataSourceDb1")
    @ConfigurationProperties("spring.datasource.db1.hikari")
    public DataSource dataSource1() {
    	if( "Y".equals(ConfigProperty.getProperty("use-jndi-lookup")) ) {
    		return (new JndiDataSourceLookup()).getDataSource(ConfigProperty.getProperty("jndi-lookup-name"));
    	}else {
    		return DataSourceBuilder.create().type(HikariDataSource.class).build();
    	}
    }
    
}
