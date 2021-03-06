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
import org.glassfish.jersey.media.multipart.MultiPartFeature
import org.skife.jdbi.v2.DBI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.auth.AccountingAuthenticator
import org.spsu.accounting.auth.SessionFilter
import org.spsu.accounting.data.dao.AccountDAO
import org.spsu.accounting.data.dao.DocumentDAO
import org.spsu.accounting.data.dao.PermissionDAO
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.dao.impl.AccountDAOImpl
import org.spsu.accounting.data.dao.impl.DocumentDAOImpl
import org.spsu.accounting.data.dao.impl.PermissionDAOImpl
import org.spsu.accounting.data.dao.impl.StartDAO
import org.spsu.accounting.data.dao.impl.UserDAOImpl
import org.spsu.accounting.data.dbi.*
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.report.resource.*
import org.spsu.accounting.resource.*
import org.spsu.accounting.resource.base.BaseResource
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
        //bootstrap.addBundle(new MultiPartBundl());
    }

    @Override
    void run(AccountingApplicationConfiguration configuration, Environment environment) throws Exception {


        environment.jersey().register(MultiPartFeature.class);

        final DBI jdbi = createDBI(configuration, environment)
        StartDAO db = new StartDAO(dbi: jdbi.onDemand(StartDBI))
        logger.info("DB connected = " + (db.test() == 1))

        MailConfig mailConfig = configuration.mail
        mailServer = new MailServerImpl(mailConfig)

        //registerHealthChecks(configuration, environment, jdbi)
        PermissionDAO permissionDAO = new PermissionDAOImpl(dbi: jdbi.onDemand(PermissionDBI))
        permissionDAO.loadPermissions()
        registerResources(environment, jdbi)
        registerReports(environment, jdbi)

        if (mailConfig.notifyStart)
            mailServer.send(mailConfig.username, "Application Started : ${this.class.simpleName}", "Application Started : ${this.class.simpleName}")
    }

    private DBI createDBI(AccountingApplicationConfiguration configuration, Environment environment) {

        final DBIFactory factory = new DBIFactory();

        return factory.build(environment, configuration.getDatabase(), "postgresql");
        //return null;
    }

    private void registerHealthChecks(AccountingApplicationConfiguration configuration, Environment environment, DBI jdbi) {

        def healthDBI = (jdbi ? jdbi.onDemand(HealthCheckDBI) : null)
        //environment.healthChecks().register("database", new DatabaseHealthCheck(healthDBI));
    }

    private void registerResources(Environment environment, DBI jdbi) {

        UserDAO userDAO = new UserDAOImpl<UserDO>(dbi: jdbi.onDemand(UserDBI))
        DocumentDAO documentDAO = new DocumentDAOImpl(dbi: jdbi.onDemand(DocumentDBI))

        environment.jersey().register(new MenuResource(dao:userDAO))
        environment.jersey().register(new EmailResource(dao:userDAO, documentDAO: documentDAO))
        environment.jersey().register(new AboutResource())
        environment.jersey().register(new TimelineResource(dbi: jdbi.onDemand(TimelineDBI)))

        registerAuth(environment, jdbi)

        (new AccountResource()).register(environment, jdbi)
        (new UserResource()).register(environment, jdbi)
        (new TransactionResource()).register(environment, jdbi)

        (new DocumentResource()).register(environment, jdbi)


    }

    private void registerReports(Environment environment, DBI jdbi) {
        AccountStatementDBI statementDBI = jdbi.onDemand(AccountStatementDBI)

        AccountDBI accountDBI = jdbi.onDemand(AccountDBI)
        AccountDAO accountDAO = new AccountDAOImpl(dbi: accountDBI)
        accountDAO.validator = environment.getValidator()
        accountDAO.objectMapper = environment.getObjectMapper()

        CategoryTotalDBI categoryDBI = jdbi.onDemand(CategoryTotalDBI)

        environment.jersey().register(new IncomeStatementResource(accountDBI: statementDBI, categoryTotalDBI: categoryDBI))
        environment.jersey().register(new OwnerEquityResource(accountDBI: statementDBI, categoryTotalDBI: categoryDBI, accountDAO: accountDAO))
        environment.jersey().register(new BalanceSheetResource(accountDBI: statementDBI, categoryTotalDBI: categoryDBI))
        environment.jersey().register(new TrailBalanceResource(accountDBI: statementDBI, categoryTotalDBI: categoryDBI))
        environment.jersey().register(new FinancialRatioResource(accountDBI: statementDBI, categoryTotalDBI: categoryDBI))


    }

    private void registerAuth(Environment environment, DBI jdbi) {

        UserDAO userDAO = new UserDAOImpl(dbi: jdbi.onDemand(UserDBI))
        environment.jersey().register(new BasicAuthProvider<UserDO>(new AccountingAuthenticator(dao: userDAO), "SUPER SECRET STUFF"));

        environment.servlets().addFilter("SessionFilter", new SessionFilter(dao: userDAO))
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");


        environment.jersey().register(new AuthResource(userDAO))

        environment.jersey().register(HttpSessionProvider.class)
        environment.servlets().setSessionHandler(new SessionHandler())
    }

    private void register(BaseResource resource, Environment environment) {
        environment.jersey().register(resource)
    }

    public static void main(String[] args) throws Exception {
        new AccountingApplication().run(args)
    }

}
