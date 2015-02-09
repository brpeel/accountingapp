package org.spsu.accounting.data.mapper

import org.spsu.accounting.data.domain.DocumentDO

public class DocumentDOMapper extends BaseMapper<DocumentDO>{

	@Override
	protected DocumentDO map(Map data) {

		DocumentDO document = new DocumentDO()

        if (data.id)
            document.id = getValue(data, "id")
        if (data.transaction_id)
            document.transactionId = getValue(data, "transaction_id")
        if (data.document_uri)
            document.documentUri = getValue(data, "document_uri")

		return document
	}

}
