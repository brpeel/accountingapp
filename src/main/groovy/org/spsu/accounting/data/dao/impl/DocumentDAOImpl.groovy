package org.spsu.accounting.data.dao.impl

import com.google.common.hash.Hashing
import org.apache.commons.codec.binary.Base64
import org.apache.commons.fileupload.FileItem
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.spsu.accounting.data.dao.DocumentDAO
import org.spsu.accounting.data.dbi.DocumentDBI
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.domain.TransactionDO

import javax.swing.text.Document

/**
 * Created by bpeel on 4/25/15.
 */
class DocumentDAOImpl implements DocumentDAO {

    private static final Logger logger = LoggerFactory.getLogger(DocumentDAOImpl)

    DocumentDBI dbi

    DocumentDO getDocumentById(Integer id){
        if (id == null)
            return null
        return dbi.getDocumentById(id)
    }

    @Override
    List<DocumentDO> createDocuments(int transId, int userid, Collection<FileItem> items) {
        final List<DocumentDO> createdDocuments = []
        items?.each { FileItem item ->
            createdDocuments.add(createDocument(transId, userid, item))
        }

        return createdDocuments
    }

    @Override
    DocumentDO createDocument(int transId, int userid, FileItem fileItem) {

        try {
            byte[] bytes = fileItem.get();
            byte[] encoded = Base64.encodeBase64(bytes);
            String encodedString = new String(encoded);

            String hash = Hashing.sha256().hashBytes(encoded).toString()

            Integer documentId = dbi.documentExists(transId, hash)
            if (!documentId)
                documentId = dbi.createDocument(transId, fileItem.name, fileItem.contentType, bytes.length, hash, encodedString, userid)

            return getDocumentById(documentId)
        }
        catch(Exception e){
            logger.error("Could not create document for ${fileItem?.name}", e)
            throw e;
        }
    }

    @Override
    List<DocumentDO> getDocuments(int transId) {
        return dbi.getAll(transId)
    }

    @Override
    DocumentDO get(int id) {
        return dbi.getDocumentById(id)
    }

    @Override
    byte[] getFile(int id) {
        String encodedString = dbi.getFile(id)

        return Base64.decodeBase64(encodedString)
    }
}
