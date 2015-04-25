package org.spsu.accounting.data.dao.impl

import org.apache.commons.fileupload.FileItem
import org.apache.commons.fileupload.disk.DiskFileItem
import org.spsu.accounting.data.DBConnection
import org.spsu.accounting.data.dbi.DocumentDBI
import org.spsu.accounting.data.domain.DocumentDO
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Created by bpeel on 4/25/15.
 */
@Stepwise
class DocumentDAOImplTest extends Specification {

    @Shared DocumentDAOImpl dao
    @Shared DocumentDBI dbi
    @Shared int transid

    void setupSpec() {

        DBConnection db = DBConnection.openConnection("Doc")
        dbi = db.onDemand(DocumentDBI)
        dao = new DocumentDAOImpl(dbi: dbi)

        transid = db.minFieldValue("accounting_trans", "id")
    }

    def "CreateDocument"() {
        given:
        File file = new File("src/test/resources/DocumentDAOTestFile.txt")

        FileItem fileItem = Mock(FileItem)
        fileItem.getContentType() >> "text/plain"
        fileItem.get() >> file.getBytes()
        fileItem.getName() >> file.getName()


        when:
        def documentId = dao.createDocument(transid, fileItem )

        then:
        documentId != null
        documentId != 0
    }

    def "GetDocuments"() {

        when:
        List<DocumentDO> docs = dao.getDocuments(transid)
        docs.each {println it}
        then:
        docs?.size() > 0

    }
}
