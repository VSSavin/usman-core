package com.github.vssavin.usmancore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

/**
 * Configuration of user management data sources.
 *
 * @author vssavin on 29.11.2023.
 */
public class UsmanDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(UsmanDataSourceConfig.class);

    private final UsmanDatabaseConfig usmanDatabaseConfig;

    private final UsmanPasswordEncodingArgumentsHandler argumentsHandler;

    private DataSource umDataSource;

    public UsmanDataSourceConfig(UsmanDatabaseConfig usmanDatabaseConfig,
            UsmanPasswordEncodingArgumentsHandler argumentsHandler) {
        this.usmanDatabaseConfig = usmanDatabaseConfig;
        this.argumentsHandler = argumentsHandler;
    }

    @Bean
    protected DataSource usmanDataSource() {
        if (this.umDataSource != null) {
            return this.umDataSource;
        }
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        try {
            dataSource.setDriverClassName(usmanDatabaseConfig.getDriverClass());
            String url = usmanDatabaseConfig.getUrl() + "/" + usmanDatabaseConfig.getName();
            if (usmanDatabaseConfig.getDriverClass().equals("org.h2.Driver")) {
                url += ";" + usmanDatabaseConfig.getAdditionalParams();
            }
            dataSource.setUrl(url);
            dataSource.setUsername(usmanDatabaseConfig.getUser());
            setDatasourcePassword(dataSource, argumentsHandler);
        }
        catch (Exception e) {
            log.error("Creating datasource error: ", e);
        }
        this.umDataSource = dataSource;

        SqlScriptExecutor sqlScriptExecutor = new SqlScriptExecutor(umDataSource);
        List<String> script = Collections.singletonList("init.sql");
        sqlScriptExecutor.executeSqlScriptsFromResource(UsmanDataSourceConfig.class, script, "");

        return dataSource;
    }

    @Bean
    AbstractRoutingDataSource routingDatasource(
            @Autowired(required = false) @Qualifier("appDatasource") DataSource appDatasource,
            @Autowired DataSource usmanDataSource) {
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.addDataSource(RoutingDataSource.DATASOURCE_TYPE.UM_DATASOURCE, usmanDataSource);
        if (appDatasource == null) {
            routingDataSource.setKey(RoutingDataSource.DATASOURCE_TYPE.UM_DATASOURCE);
            routingDataSource.setDefaultTargetDataSource(usmanDataSource);
        }
        else {
            routingDataSource.addDataSource(RoutingDataSource.DATASOURCE_TYPE.APPLICATION_DATASOURCE, appDatasource);
            routingDataSource.setDefaultTargetDataSource(appDatasource);
        }

        return routingDataSource;
    }

    private void setDatasourcePassword(DriverManagerDataSource dataSource,
            UsmanPasswordEncodingArgumentsHandler argumentsHandler) {
        if (argumentsHandler.isDbPasswordEncoded()) {
            try {
                dataSource
                    .setPassword(argumentsHandler.getPasswordService().decrypt(usmanDatabaseConfig.getPassword()));
            }
            catch (Exception e) {
                log.debug("Can't decrypt password! Using a password from the config...", e);
                dataSource.setPassword(usmanDatabaseConfig.getPassword());
            }
        }
        else {
            dataSource.setPassword(usmanDatabaseConfig.getPassword());
        }
    }

}
