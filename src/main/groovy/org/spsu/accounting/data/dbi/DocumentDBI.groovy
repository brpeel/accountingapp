package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.SqlBatch
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.mapper.DocumentDOMapper

@RegisterMapper(DocumentDOMapper.class)
interface DocumentDBI{

    @SqlQuery("select id, trans_id, file_name, content_type, size, hash, file, uploaded from accounting_trans_document where id = :id")
    @MapResultAsBean
    DocumentDO getDocumentById(@Bind("id") int id)

    @SqlQuery("select id, trans_id, file_name, content_type, size, hash, file, uploaded from accounting_trans_document where trans_id = :transId")
    @MapResultAsBean
    List<DocumentDO> getAll(@Bind("transId") int transactionId)

    @SqlUpdate("""
    insert into accounting_trans_document (trans_id, file_name, content_type, size, hash, file)
    values (:transId, :name, :type, :size, :hash, :data)""")
    @GetGeneratedKeys
    int createDocument(@Bind("transId") int transId,
                       @Bind("name") String name,
                       @Bind("type") String type,
                       @Bind("size") int size,
                       @Bind("hash") String hash,
                       @Bind("data") String data)


    @SqlQuery("""select id from accounting_trans_document where trans_id = :transId and hash = :hash""")
    Integer documentExists(@Bind("transId") int transId, @Bind("hash") String hash)
}
