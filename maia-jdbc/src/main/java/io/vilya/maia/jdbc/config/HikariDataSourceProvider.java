package io.vilya.maia.jdbc.config;

import javax.inject.Provider;
import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author erkea <erkea@vilya.io>
 */
public class HikariDataSourceProvider implements Provider<DataSource> {

	@Override
	public DataSource get() {
		// TODO get config from ApplicationContext
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/cms?serverTimezone=GMT%2B8");
		config.setUsername("dev");
		config.setPassword("123456");
		config.setMinimumIdle(3);
		config.setMaximumPoolSize(10);
		return new HikariDataSource(config);
	}

}
