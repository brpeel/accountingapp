package org.spsu.accounting.app

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.ISO8601DateFormat
import com.fasterxml.jackson.datatype.joda.JodaModule
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.auth.basic.BasicAuthProvider
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.jersey.sessions.HttpSessionProvider
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.eclipse.jetty.server.session.SessionHandler
import org.skife.jdbi.v2.DBI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.auth.AccountingAuthenticator
import org.spsu.accounting.auth.SessionFilter
import org.spsu.accounting.data.dao.PermissionDAO
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.dao.impl.PermissionDAOImpl
import org.spsu.accounting.data.dao.impl.StartDAO
import org.spsu.accounting.data.dao.impl.UserDAOImpl
import org.spsu.accounting.data.dbi.HealthCheckDBI
import org.spsu.accounting.data.dbi.PermissionDBI
import org.spsu.accounting.data.dbi.StartDBI
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.resource.AboutResource
import org.spsu.accounting.resource.AccountResource
import org.spsu.accounting.resource.AuthResource
import org.spsu.accounting.resource.TransactionResource
import org.spsu.accounting.resource.base.BaseResource
import org.spsu.accounting.resource.MenuResource
import org.spsu.accounting.resource.UserResource
import org.spsu.accounting.utils.mail.MailConfig
import org.spsu.accounting.utils.mail.MailServer
import org.spsu.accounting.utils.mail.MailServerImpl

import javax.servlet.DispatcherType

/**
 * Created by bpeel on 1/28/15.
 */
class AccountingApplication extends Application<AccountingApplicationConfiguration> {

    private Logger logger = LoggerFactory.getLogger(AccountingApplication)
    public static MailServer mailServer
    public static String APPLICATION = AccountingApplication.class.simpleName

    @Override
    void initialize(Bootstrap<AccountingApplicationConfiguration> bootstrap) {

        bootstrap.addBundle(new MigrationsBundle<AccountingApplicationConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(AccountingApplicationConfiguration configuration) {
                return configuration.getDatabase();
            }
        })

        bootstrap.getObjectMapper().registerModule(new JodaModule());
        bootstrap.getObjectMapper().setDateFormat(new ISO8601DateFormat());
        bootstrap.getObjectMapper().getTypeFactory().constructMapType(HashMap.class, String.class, Object.class)

        bootstrap.getObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        bootstrap.getObjectMapper().enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);

        bootstrap.addBundle(new AssetsBundle("/ui/"));
    }

    @Override
    void run(AccountingApplicationConfiguration configuration, Environment environment) throws Exception {

        final DBI jdbi = createDBI(configuration, environment)
        StartDAO db = new StartDAO(dbi: jdbi.onDemand(StartDBI))
        logger.info("DB connected = "+(db.test() == 1))

        MailConfig mailConfig = configuration.mail
        mailServer = new MailServerImpl(mailConfig)

        //registerHealthChecks(configuration, environment, jdbi)
        PermissionDAO permissionDAO = new PermissionDAOImpl(dbi:jdbi.onDemand(PermissionDBI))
        permissionDAO.loadPermissions()
        registerResources(environment, jdbi)

        if (mailConfig.notifyStart)
            mailServer.send(mailConfig.username, "Application Started : ${this.class.simpleName}", "Application Started : ${this.class.simpleName}")
    }

    private DBI createDBI(AccountingApplicationConfiguration configuration, Environment environment){

        final DBIFactory factory = new DBIFactory();

        return factory.build(environment, configuration.getDatabase(), "postgresql");
        //return null;
    }

    private void registerHealthChecks(AccountingApplicationConfiguration configuration, Environment environment, DBI jdbi){

        def healthDBI = (jdbi ? jdbi.onDemand(HealthCheckDBI) : null)
        //environment.healthChecks().register("database", new DatabaseHealthCheck(healthDBI));
    }

    private void registerResources(Environment environment, DBI jdbi){


        environment.jersey().register(new MenuResource())
        environment.jersey().register(new AboutResource())


        registerAuth(environment, jdbi)

        (new AccountResource()).register(environment, jdbi)
        (new UserResource()).register(environment, jdbi)
        (new TransactionResource()).register(environment, jdbi)

    }

    private void registerAuth(Environment environment, DBI jdbi){

        UserDAO userDAO = new UserDAOImpl(dbi: jdbi.onDemand(UserDBI))
        environment.jersey().register(new BasicAuthProvider<UserDO>(new AccountingAuthenticator(dao:userDAO), "SUPER SECRET STUFF"));

        environment.servlets().addFilter("SessionFilter", new SessionFilter(dao: userDAO))
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");


        environment.jersey().register(new AuthResource(userDAO))

        environment.jersey().register(HttpSessionProvider.class)
        environment.servlets().setSessionHandler(new SessionHandler())
    }

    private void register(BaseResource resource, Environment environment){
        environment.jersey().register(resource)
    }

    public static void main(String[] args) throws Exception {
        new AccountingApplication().run(args)
    }

}
