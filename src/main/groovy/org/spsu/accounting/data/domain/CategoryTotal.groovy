package org.spsu.accounting.data.domain

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.spsu.accounting.data.serial.MoneySerializer

/**
 * Created by bpeel on 4/29/15.
 */
class CategoryTotal {

    String category

    String subcategory

    @JsonSerialize(using = MoneySerializer.class)
    BigDecimal balance = 0.0
}
