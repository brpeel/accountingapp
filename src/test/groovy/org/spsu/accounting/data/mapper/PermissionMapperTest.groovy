package org.spsu.accounting.data.mapper

import org.spsu.accounting.data.domain.Permission
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by bpeel on 3/14/15.
 */
@Unroll
class PermissionMapperTest extends Specification {

    PermissionMapper mapper

    void setup() {
        mapper = new PermissionMapper()
    }

    def "ConvertInputs"() {
        given:

        Map rawData = ["badfield": 1, "permission": permission, "user_type_id": user_type_id, "permission_group": permission_group, "group_order": group_order, "label": label, "style": style, "active": active]

        when:
        Permission result = mapper.map(rawData)

        then:
        result != null
        result.role == expectedRole
        result.permission == permission
        result.group == permission_group
        result.order == group_order
        result.label == label
        result.style == style
        result.active == (active == null ? true : active)

        where:
        permission    | user_type_id | permission_group | group_order | label          | style        | active | expectedRole
        "createTrans" | 10           | "Transaction"    | 0           | "Create Trans" | "some style" | true   | 10
        "Accounts"    | 100          | "MainMenu"       | 1           | null           | null         | false  | 100
        "createTrans" | 50           | "Transaction"    | 0           | "Create"       | "Style"      | null   | 50

    }
}
