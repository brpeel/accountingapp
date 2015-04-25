package org.spsu.accounting.data.dao

import org.apache.commons.fileupload.FileItem
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.UserDO

/**
 * Created by bpeel on 3/22/15.
 */
interface TransactionDAO extends DAO<TransactionDO> {

    public void approve(int id, UserDO user);

    public void reject(int id, UserDO user);

}