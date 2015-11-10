package org.springframework.nanotrader.quote;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("cloud")
@EnableJpaRepositories("org.springframework.nanotrader.quote")
public class CloudConfig extends AbstractCloudConfig {

	@Bean
	public DataSource dataSource() {
		return connectionFactory().dataSource();
	}

}