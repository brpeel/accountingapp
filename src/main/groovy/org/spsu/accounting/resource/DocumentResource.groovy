package org.spsu.accounting.resource

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.ByteStreams
import io.dropwizard.setup.Environment
import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.io.FileUtils
import org.skife.jdbi.v2.DBI
import org.spsu.accounting.data.dao.DocumentDAO
import org.spsu.accounting.data.dao.UserDAO
import org.spsu.accounting.data.dao.impl.DocumentDAOImpl
import org.spsu.accounting.data.dao.impl.UserDAOImpl
import org.spsu.accounting.data.dbi.DocumentDBI
import org.spsu.accounting.data.dbi.UserDBI
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.domain.UserDO

import javax.servlet.http.HttpServletRequest
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.StreamingOutput

/**
 * Created by bpeel on 4/25/15.
 */
@Path("api/transdocument")
class DocumentResource {

    protected DocumentDAO dao;
    protected UserDAO userDAO;
    ObjectMapper mapper

    public void register(Environment environment, DBI jdbi){
        init(environment, jdbi)
        environment.jersey().register(this)
    }

    protected void init(Environment environment, DBI jdbi){
        dao = new DocumentDAOImpl(dbi: jdbi.onDemand(DocumentDBI))

        userDAO = new UserDAOImpl<UserDO>(dbi: jdbi.onDemand(UserDBI))

        mapper = environment.getObjectMapper()
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@Context HttpServletRequest request, @PathParam("id") int transId) {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        final List<FileItem> items = upload.parseRequest(request);
        final List<DocumentDO> documents = dao.createDocuments(transId, items)

        return Response.ok(documents).build()
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDocumentList(@PathParam("id") int transId){

        return Response.ok(dao.getDocuments(transId)).build()
    }

    @GET
    @Path("/download/{id}")
    public Response getDocument(@PathParam("id") int id){
        DocumentDO documentDO = dao.get(id)

        final byte[] data = dao.getFile(id);
        InputStream  stream = new ByteArrayInputStream(data)

        StreamingOutput streamOutput = new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException, WebApplicationException {
                ByteStreams.copy(stream, os)
            }
        };

        Response response = Response.ok(stream).type(documentDO.contentType).build()
        return response
    }
}
