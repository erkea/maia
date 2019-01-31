package io.vilya.maia.jdbc.config;

import java.io.InputStream;

import javax.inject.Provider;
import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.configuration.ConfigurationProvider;

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
				useConfigurationProvider(MaiaConfigurationProvider.class);
				bindDataSourceProvider(dataSourceProvider);
				bindTransactionFactory(JdbcTransactionFactory::new);
			}
		};
	}
	
	private static class MaiaConfigurationProvider extends ConfigurationProvider {

		private String[] mapperLocations;
		
		public MaiaConfigurationProvider(Environment environment) {
			super(environment);
		}

		@Override
		public Configuration get() {
			Configuration configuration = super.get();
			configMapperLocations(configuration);
			return configuration;
		}

		private void configMapperLocations(Configuration configuration) {
			if (mapperLocations != null) {
				for (String mapperLocation : mapperLocations) {
					ClassLoader classLoader = MaiaConfigurationProvider.class.getClassLoader();
					InputStream is = classLoader.getResourceAsStream(mapperLocation);
					if (is != null) {
						XMLMapperBuilder builder = new XMLMapperBuilder(is, configuration, mapperLocation, configuration.getSqlFragments());
						builder.parse();
					}
				}
			}
		}

		public String[] getMapperLocations() {
			return mapperLocations;
		}

		public void setMapperLocations(String[] mapperLocations) {
			this.mapperLocations = mapperLocations;
		}
		
	}

}
