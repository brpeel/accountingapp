package org.spsu.accounting.data.dao.impl

import org.apache.commons.lang3.text.WordUtils
import org.spsu.accounting.data.dao.AccountDAO
import org.spsu.accounting.data.domain.AccountDO
import org.spsu.accounting.data.domain.BaseDO

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Response
import java.sql.SQLException

/**
 * Created by bpeel on 3/28/15.
 */
class AccountDAOImpl  extends ActiveDAOImpl<AccountDO> implements AccountDAO{

    private int getNextAccountId(String category){
        category = WordUtils.capitalize(category.toLowerCase())
        dbi.incrementSeq(category)
        return dbi.getSeq(category)
    }
    @Override
    def create(AccountDO object) {
        AccountDO existing =  object.id ? dbi.get(object.id) : null

        if (!existing){
            List validationMessages = validateObject(object)

            if (validationMessages)
                throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).entity(validationMessages).build());
            int id = getNextAccountId(object.category)
            object.id = id
            dbi.insert(object)
            return id
        }
        else if (existing.deleted){
            return activate(object.id) && this.save(object)
        }

        throw new SQLException("Object for id $object.id already exists")
    }

    @Override
    ArrayList<String> validateObject(AccountDO obj) {
        ArrayList<String> messages = super.validateObject(obj)
        messages = messages ? messages : new ArrayList<String>()

        String startsWith
        switch (obj.category?.toLowerCase()){
            case "asset": startsWith = "1"; break;
            case "liability": startsWith = "2"; break;
            case "owner equity": startsWith = "3"; break;
            case "expense": startsWith = "4"; break;
            case "revenue": startsWith = "5"; break;
            default: messages.add("Invalid account category ${obj.category}")
        }

        if (obj.id && startsWith && startsWith != "${obj.id}")
            messages.add("Invalid account id: ${obj.category} accounts must start with ${startsWith}")

        if (messages.size() > 0)
            return messages
        return null
    }

    @Override
    def postTransaction(int transId) {

    }
}
