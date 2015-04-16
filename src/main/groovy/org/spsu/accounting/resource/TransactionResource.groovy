package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.spsu.accounting.data.dao.DAO
import org.spsu.accounting.data.dao.impl.ActiveDAOImpl
import org.spsu.accounting.data.dao.impl.TransactionDAOImpl
import org.spsu.accounting.data.dbi.AccountDBI
import org.spsu.accounting.data.dbi.TransactionDBI
import org.spsu.accounting.data.dbi.TransactionEntryDBI
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.data.mapper.BaseMapper
import org.spsu.accounting.data.serial.DateTimeSerializer
import org.spsu.accounting.data.serial.MoneySerializer
import org.spsu.accounting.resource.base.BaseResource

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 2/24/15.
 */
@Path("api/transaction")
class TransactionResource extends BaseResource<DAO<TransactionDO>> {

    DAO<AccountDO> accountDAO

    AccountTransDBI accountTransDBI

    @Override
    protected DAO createDAO(DBI jdbi) {
        accountDAO = new ActiveDAOImpl<AccountDO>(dbi: jdbi.onDemand(AccountDBI))
        accountTransDBI = jdbi.onDemand(AccountTransDBI)
        return new TransactionDAOImpl(dbi: jdbi.onDemand(TransactionDBI), entryDBI: jdbi.onDemand(TransactionEntryDBI))
    }

    @GET
    @Path("/account/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response getTransForAccount(@PathParam("id") int id){

        List trans = getAccountTranactions(id)

        return Response.ok(trans).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response create( @Context HttpServletRequest request, Map data){

        int userid = request.getAttribute("userid")
        data."reportedBy" = userid

        TransactionDO trans = new TransactionDO(data)

        return postObject(trans)
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response update( @Context HttpServletRequest request, @PathParam("id") int id, Map values){

        return super.patchObject(id, values)
    }

    @PUT
    @Path("/approve/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response approve( @Context HttpServletRequest request, @PathParam("id") int id){

        UserDO user = getUser(request)
        dao.approve(id, user)

        //Adjust account balances


        return Response.ok().build()
    }

    @PUT
    @Path("/reject/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Response reject( @Context HttpServletRequest request, @PathParam("id") int id){

        UserDO user = getUser(request)
        dao.reject(id, user)

        return Response.ok().build()
    }

    protected List<AccountTransactions> getAccountTranactions(int accountid){
        return accountTransDBI.getTransByAccount(accountid)
    }

    public static class AccountTransactions {

        Integer id

        Integer reportedBy

        Integer approvedBy

        @JsonSerialize(using = DateTimeSerializer)
        DateTime reported

        @JsonSerialize(using = DateTimeSerializer)
        DateTime approved

        String status

        String description

        int accountId

        @JsonSerialize(using = MoneySerializer.class)
        BigDecimal amount

        @JsonIgnore
        private boolean debit

        @JsonGetter("debitAmount")
        public String getDebitAmount(){
            return debit ? amount : null
        }

        @JsonGetter("creditAmount")
        public String getCreditAmount(){
            return debit ? null : amount
        }
    }

    public static class AccountTransMapper extends BaseMapper<AccountTransactions>{}

    @RegisterMapper(AccountTransMapper)
    public interface AccountTransDBI {

        @SqlQuery("""
        select t.id, t.reported_by, t.approved_by, t.reported, t.approved, t.status, t.description, e.account_id, e.amount, e.debit
        from accounting_trans t
          join accounting_trans_entry e
            on t.id = e.trans_id
        where e.account_id = :account
        order by t.id
        """)
        List<AccountTransactions> getTransByAccount(@Bind("account") int account)
    }
}
