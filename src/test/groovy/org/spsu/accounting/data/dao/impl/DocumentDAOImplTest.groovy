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

    DocumentDAOImpl dao
    DocumentDBI dbi

    void setup() {

        //DBConnection db = DBConnection.openConnection("Doc")
        dbi = Mock(DocumentDBI)
        dao = new DocumentDAOImpl(dbi: dbi)

       // transid = db.minFieldValue("accounting_trans", "id")
    }

    def "CreateDocument"() {
        given:
        File file = new File("src/test/resources/DocumentDAOTestFile.txt")

        FileItem fileItem = Mock(FileItem)
        fileItem.getContentType() >> "text/plain"
        fileItem.get() >> file.getBytes()
        fileItem.getName() >> file.getName()
        1 * dbi.documentExists(_,_) >> null

        when:
        dao.createDocument(1, 1, fileItem )

        then:
        noExceptionThrown()
        1 * dbi.createDocument(1, file.getName(), "text/plain", file.getBytes().size(), _, _, 1) >> 1000
        1 * dbi.getDocumentById(1000)
    }

}
