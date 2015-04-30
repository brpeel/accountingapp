package org.spsu.accounting.resource

import com.fasterxml.jackson.annotation.JsonProperty
import org.apache.commons.io.FileUtils
import org.spsu.accounting.app.AccountingApplication
import org.spsu.accounting.data.dao.DocumentDAO
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.dao.impl.UserDAOImpl
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.domain.UserDO
import org.spsu.accounting.utils.mail.MailServer

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * Created by bpeel on 4/30/15.
 */
@Path("api/email")
class EmailResource {


    public static class Email{
        @JsonProperty("subject")
        String subject

        @JsonProperty("body")
        String body
    }

    public static class EmailWithAttachment extends Email{

        @JsonProperty("attachment")
        Integer documentid
    }

    UserDAOImpl dao
    protected DocumentDAO documentDAO;
    public static MailServer mailServer = AccountingApplication.mailServer

    @Path("simple/{to}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendSimpleEmail(@Context HttpServletRequest request, @PathParam("to") int toId, Email email){
        Integer userid = request.getAttribute("userid")
        if (userid == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED)
        UserDO from = dao.get(userid)

        if (!from)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED)

        UserDO toUser = dao.get(toId)
        if (!toUser)
            throw new WebApplicationException(Response.Status.BAD_REQUEST)


        mailServer.send(toUser.email, email.subject, email.body, from)
    }

    @Path("senddocument/{to}")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendAttachmentEmail(@Context HttpServletRequest request, @PathParam("to") int toId, EmailWithAttachment email){

        Integer userid = request.getAttribute("userid")
        if (userid == null)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED)
        UserDO from = dao.get(userid)

        if (!from)
            throw new WebApplicationException(Response.Status.UNAUTHORIZED)

        UserDO toUser = dao.get(toId)
        if (!toUser)
            throw new WebApplicationException(Response.Status.BAD_REQUEST)

        DocumentDO documentDO = documentDAO.get(email.documentid)
        byte[] data = documentDAO.getFile(email.documentid)

        File file = new File(documentDO.name)
        FileUtils.writeByteArrayToFile(file, data);

        mailServer.send(toUser.email, email.subject, email.body, from, file)

        file.deleteOnExit()
        return Response.ok().build();
    }

}
