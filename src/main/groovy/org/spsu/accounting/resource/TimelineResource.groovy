package org.spsu.accounting.resource

import org.joda.time.DateTime
import org.spsu.accounting.data.dbi.TimelineDBI
import org.spsu.accounting.data.domain.TimelineEntry

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Created by bpeel on 4/26/15.
 */
@Path("api/timeline")
class TimelineResource {

    TimelineDBI dbi

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<TimelineGroup> getEntries(){
        List<TimelineEntry> entries = dbi.getTimeline()

        final Map<String, List<TimelineEntry>> entryGroups = [:]

        entries?.each{ TimelineEntry e->
            DateTime eventhour = e.getEventhour();
            String key = eventhour.toString("yyyy-MM-dd")

            List<TimelineEntry> entryList = entryGroups.get(key)
            if (!entryList){
                entryList = new ArrayList<>()
                entryGroups.put(key, entryList)
            }
            entryList.add(e)
        }

        final List<TimelineGroup> groups = []
        entryGroups.each {Map.Entry i ->
            groups << new TimelineGroup(date: i.key, entries: i.value)
        }

        return groups
    }

    private class TimelineGroup {
        String date
        List<TimelineEntry> entries
    }
}
