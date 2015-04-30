package org.spsu.accounting.data.dao

import org.apache.commons.fileupload.FileItem
import org.joda.time.DateTime
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.UserDO

/**
 * Created by bpeel on 3/22/15.
 */
interface TransactionDAO extends DAO<TransactionDO> {

    public void post(int id, UserDO user);

    public void reject(int id, UserDO user);

    void removeEntries(List<Integer> entries);

    void removeEntry(int entryId);



    List<TransactionDO> search(Integer id, String keyword, DateTime startRange, DateTime endRange)
    List<TransactionDO> search(Integer id, String keyword, DateTime startRange, DateTime endRange, boolean pendingOnly)
    List<TransactionDO> search(String id, String keyword, DateTime startRange, DateTime endRange)
    List<TransactionDO> search(String id, String keyword, DateTime startRange, DateTime endRange, boolean pendingOnly)
}