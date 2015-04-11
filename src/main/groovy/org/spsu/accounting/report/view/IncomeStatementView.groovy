package org.spsu.accounting.report.view

import io.dropwizard.views.View
import org.spsu.accounting.report.data.IncomeStatement

/**
 * Created by bpeel on 4/11/15.
 */
class IncomeStatementView extends View{

    private final IncomeStatement statement;
    public IncomeStatementView(IncomeStatement statement){
        super("incomestatement.ftl");
        this.statement = statement

    }
}
