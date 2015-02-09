package org.spsu.accounting.data.mapper

import org.spsu.accounting.data.domain.TransactionDO

public class TransactionDOMapper extends BaseMapper<TransactionDO>{

	@Override
	protected TransactionDO map(Map data) {

		TransactionDO transaction = new TransactionDO()

        if (data.id)
            transaction.id = getValue(data, "id")
        if (data.reported_by)
            transaction.reportedBy = getValue(data, "reported_by")
        if (data.approved_by)
            transaction.approvedBy = getValue(data, "approved_by")
        if (data.reported)
            transaction.reported = getDateValue(data, "reported")
        if (data.approved)
            transaction.approved = getDateValue(data, "approved")
        if (data.status)
            transaction.status = getValue(data, "status")

		return transaction
	}

}
