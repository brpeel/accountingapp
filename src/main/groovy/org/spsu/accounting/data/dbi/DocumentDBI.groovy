package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.Bind
import org.skife.jdbi.v2.sqlobject.BindBean
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.skife.jdbi.v2.sqlobject.helpers.MapResultAsBean
import org.spsu.accounting.data.domain.DocumentDO
import org.spsu.accounting.data.mapper.DocumentDOMapper

@RegisterMapper(DocumentDOMapper.class)
interface DocumentDBI{

	@SqlQuery("select transaction_id, document_uri from transaction_document where id = :id")
	@MapResultAsBean
	DocumentDO get(@Bind("id") int id)

	@SqlQuery("insert into DocumentDO (transaction_id, document_uri) \
	 values (:transactionId, :documentUri) \
	 returning id")
	int insert(@BindBean DocumentDO doBean)

	@SqlUpdate("update DocumentDO set  transaction_id = :transactionId, document_uri = :documentUri where id = :id")
	int update(@BindBean DocumentDO doBean)
}
