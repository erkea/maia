package io.vilya.maia.jdbc.config;

import javax.inject.Provider;
import javax.sql.DataSource;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;

import com.google.inject.Module;

import io.vilya.maia.core.annotation.Component;
import io.vilya.maia.core.context.guice.GuiceModuleProvider;

/**
 * @author erkea <erkea@vilya.io>
 */
@Component
public class MybatisModuleProvider implements GuiceModuleProvider {

	@Override
	public Module get() {
		Provider<DataSource> dataSourceProvider = new HikariDataSourceProvider();
		
		return new MyBatisModule() {
			@Override
			protected void initialize() {
				environmentId("dev");
				addMapperClasses("io.vilya.maia.cms.repository");	// TODO reuse candidates in ApplicationContext
				bindDataSourceProvider(dataSourceProvider);
				bindTransactionFactory(JdbcTransactionFactory::new);
			}
		};
	}

}
