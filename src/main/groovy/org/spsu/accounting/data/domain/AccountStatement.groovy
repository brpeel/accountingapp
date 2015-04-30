package org.spsu.accounting.data.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.spsu.accounting.data.serial.MoneySerializer

/**
 * Created by bpeel on 4/11/15.
 */
class AccountStatement {

    int id

    String name

    String category

    String subcategory

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal balance

    boolean debitNormal = false
}
