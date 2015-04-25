package org.spsu.accounting.data.dao

import org.apache.commons.fileupload.FileItem
import org.spsu.accounting.data.domain.DocumentDO

/**
 * Created by bpeel on 4/25/15.
 */
interface DocumentDAO {

    DocumentDO createDocument(int transId, FileItem fileItem)

    List<DocumentDO> createDocuments(int transId, Collection<FileItem> fileItem)

    List<DocumentDO> getDocuments(int transId)
}