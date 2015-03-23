package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.mapper.DocumentDOMapper

@RegisterMapper(DocumentDOMapper.class)
interface DocumentDBI{

	@SqlQuery("select transaction_id, document_uri from transaction_document where trans_id = :trans_id and document_uri = :uri")
	@MapResultAsBean
	String get(@Bind("trans_id") int transactionId, @Bind("uri") String uri)

    @SqlQuery("select transaction_id, document_uri from transaction_document where trans_id = :trans_id")
    @MapResultAsBean
    List<String> getAll(@Bind("trans_id") int transactionId)

    @SqlQuery("insert into DocumentDO (transaction_id, document_uri) values (:transactionId, :documentUri)")
    @GetGeneratedKeys
	int insert(@Bind("accounting_trans_id") int transactionId, @Bind("uri") String uri)

    @SqlUpdate("delete DocumentDO where accounting_trans_id = :accounting_trans_id and document_uri = :uri")
    int delete(@Bind("accounting_trans_id") int transactionId, @Bind("uri") String uri)
}
