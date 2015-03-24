package org.spsu.accounting.data.dao.impl

import org.joda.time.DateTime
import org.spsu.accounting.data.dao.TransactionDAO
import org.spsu.accounting.data.dbi.DocumentDBI
import org.spsu.accounting.data.dbi.TransactionEntryDBI
import org.spsu.accounting.data.domain.BaseDO
import org.spsu.accounting.data.domain.TransactionDO
import org.spsu.accounting.data.domain.TransactionEntryDO

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import java.sql.Timestamp

/**
 * Created by bpeel on 3/22/15.
 */
class TransactionDAOImpl extends DAOImpl<TransactionDO> implements TransactionDAO {

    TransactionEntryDBI entryDBI
    DocumentDBI documentDBI

    @Override
    TransactionDO get(id) {
        TransactionDO trans = super.get(id)
        if (!trans)
            return null

        if (trans.id)
            trans.entries = entryDBI.getEntries(trans.id)

        return trans
    }

    List<TransactionDO> search(Integer id, String keyword = null, DateTime startRange = null, DateTime endRange = null){
        return search(id?.toString(), keyword, startRange, endRange)
    }

    List<TransactionDO> search(String id, String keyword = null, DateTime startRange = null, DateTime endRange = null){

        List<String> conditions = []

        id = (id != null && id.trim().length() > 0 ? "%$id%" : null)
        keyword = (keyword != null && keyword.trim().length() > 0 ? "%$keyword%" : null)

        Timestamp start = (!startRange ? null : new Timestamp(startRange.millis))
        Timestamp end = (!endRange ? null : new Timestamp(endRange.millis))

        return dbi.search(id, keyword, start, end)

    }

    @Override
    def create(TransactionDO object) {
        int id = super.create(object)
        object?.entries?.each {TransactionEntryDO entry ->
            entry.transactionId = id
            entryDBI.insert(entry)
        }
        return id
    }

    def addEntry(int transactionId, TransactionEntryDO entry){
        TransactionDO trans = super.get(transactionId)
        if (!trans)
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Transaction $transactionId does not exist"))

        if (trans.isSubmitted())
            throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity("Transaction has already been submitted"))

        entry.transactionId = transactionId
        if (!entry.id)
            entryDBI.insert(entry)
        else
            entryDBI.update(entry)
    }

    def removeEntry(int transactionId, int entryId){
        entryDBI.delete(entryId)
    }

    def addDocument(int transactionId, String uri){
        if (!documentDBI.get(transactionId, uri))
            documentDBI.insert(transactionId, uri)
    }

    def removeDocument(int transactionId, String uri){
        if (!documentDBI.get(transactionId, uri))
            documentDBI.delete(transactionId, uri)
    }

    @Override
    int save(TransactionDO object) {
        int updated = super.save(object)
        if (updated > 0){

            final int transID = object.id
            object.entries?.each {TransactionEntryDO entry ->
                if (!entry.id)
                    entryDBI.insert(entry)
                else
                    entryDBI.update(entry)
            }

            object.documents?.each {String uri ->
                addDocument(transID, uri)
            }
        }
    }

    private void saveChildren(Collection<BaseDO> children, def dbi){
        if (!children)
            return

        children.each {TransactionEntryDO entry ->
            if (!entry.id)
                dbi.insert(entry)
            else
                dbi.update(entry)
        }
    }

    @Override
    int merge(Object id, Map values) {
        return super.merge(id, values)
    }

    @Override
    ArrayList<String> validateObject(TransactionDO obj) {
        ArrayList<String> messages = super.validateObject(obj)
        messages = messages ? messages : new ArrayList<String>()


        if (!hasEntry(obj))
            messages.add("Transaction must have credit and debt account entries")

        if (!creditsEqualDebits(obj))
            messages.add("Total credits must equal total debits")

        obj.entries?.each { TransactionEntryDO entry ->
            List msgs = super.validateObject(entry)
            if (msgs)
                messages.addAll(msgs)
        }

        if (messages.size() > 0)
            return messages
        return null
    }

    boolean hasEntry(TransactionDO obj){
        boolean hasEntry = false
        if (!obj.entries)
            return false
        Iterator<TransactionEntryDO> i = obj.entries.iterator()
        while (i.hasNext() && !hasEntry)
            hasEntry = i.next() != null
        return hasEntry
    }

    boolean creditsEqualDebits(TransactionDO obj){

        return obj.sumCredits() == obj.sumDebits()
    }
}
