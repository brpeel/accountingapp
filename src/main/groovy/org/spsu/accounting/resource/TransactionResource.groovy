package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.sun.jersey.core.header.FormDataContentDisposition;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException
import org.apache.commons.codec.binary.Base64
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.FileItemFactory
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
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
import org.spsu.accounting.data.domain.TransactionEntryDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.data.mapper.BaseMapper
import org.spsu.accounting.data.serial.DateTimeSerializer
import org.spsu.accounting.data.serial.MoneySerializer
import org.spsu.accounting.resource.base.BaseResource

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
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
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    Response search( Map searchTerms){

        List results = []
        if (!searchTerms || searchTerms.size() == 0)
            results = getAllObjects(false);
       else {
            String id = searchTerms?.id
            String keyword = searchTerms?.keyword

            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

            DateTime startRange = null;
            if (searchTerms.startDate)
                startRange = formatter.parseDateTime(searchTerms.startDate)

            DateTime endRange = null;
            if (searchTerms.endDate)
                endRange = formatter.parseDateTime(searchTerms.endDate)

            results = dao.search(id, keyword, startRange, endRange)
        }

        return Response.ok(results).build()
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
    Response update( @Context HttpServletRequest request, @PathParam("id") int id, TransactionDO changes){

        TransactionDO transaction = get(id);

        transaction.description = changes.description;

        Set<Integer> changeEntries = new HashSet<>()
        changes?.entries?.each {TransactionEntryDO it ->
            if (it.id)
                changeEntries.add(it.id)
        }

        List<Integer> removedEntries = []
        List<TransactionEntryDO> entries = []
        transaction.entries?.each {TransactionEntryDO it ->
            if (!changeEntries.contains(it.id))
              removedEntries.add(it.id)
        }

        transaction.entries = changes.entries;

        dao.save(transaction);
        dao.removeEntries(removedEntries)
        return Response.noContent().header("Location", buildURI(id)).build()
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



    protected List<String> writeFiles(List<FileItem> items){
        List<String> downloadPaths = []


        return downloadPaths
    }


    protected List<AccountTransactions> getAccountTranactions(int accountid){
        return accountTransDBI.getTransByAccount(accountid)
    }

    public static class AccountTransactions {

        Integer id

        Integer reportedby

        Integer approvedby

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
        select t.id, t.reported_by as reportedby, t.approved_by as approvedby, t.reported, t.approved, t.status, t.description, e.account_id, e.amount, e.debit
        from accounting_trans t
          join accounting_trans_entry e
            on t.id = e.trans_id
        where e.account_id = :account
        order by t.id
        """)
        List<AccountTransactions> getTransByAccount(@Bind("account") int account)
    }
}
