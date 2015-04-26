package org.spsu.accounting.data.dbi

import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper
import org.spsu.accounting.data.domain.TimelineEntry
import org.spsu.accounting.data.mapper.TimelineMapper

/**
 * Created by bpeel on 4/26/15.
 */
@RegisterMapper(TimelineMapper)
interface TimelineDBI {

    @SqlQuery("""
    SELECT
      date_trunc('hour', eventtime) as eventhour,
      username,
      first_name || ' ' || last_name AS fullname,
      msg,
      eventtime
    FROM (
           SELECT
             added_by                 AS userid,
             'Added account ' || name AS msg,
             added                    AS eventtime
           FROM account
           UNION
           SELECT
             uploaded_by                                                AS userid,
             'Uploaded ' || file_name || ' to transaction ' || trans_id AS msg,
             uploaded                                                   AS eventtime
           FROM accounting_trans_document doc
           UNION
           SELECT
             reported_by                            AS userid,
             'Reported transaction ' || description AS msg,
             reported                               AS eventtime
           FROM accounting_trans
           UNION
           SELECT
             approved_by                            AS userid,
             'Approved transaction ' || description AS msg,
             approved                               AS eventtime
           FROM accounting_trans) AS Event
      JOIN accounting_user AS accuser ON Event.userid = accuser.id
    WHERE Event.eventtime IS NOT NULL
    ORDER BY eventtime DESC
    LIMIT 50
    """)
    List<TimelineEntry> getTimeline()
}