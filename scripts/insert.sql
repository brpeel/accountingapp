truncate table account CASCADE;

--Assets
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 101, 'Cash', 0.00, 'Credit', now(), true, id, 'Asset'
  from accounting_user where username = 'brpeela';

insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 142, 'Office Supplies', 0.00, 'Credit', now(), true, id, 'Asset'
  from accounting_user where username = 'brpeela';

--Revenues
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 401, 'Professional Fees', 0.00, 'Credit', now(), true, id, 'Revenue'
  from accounting_user where username = 'brpeela';

--Liabilities
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 202, 'Accounts Payable', 0.00, 'Credit', now(), true, id, 'Liability'
  from accounting_user where username = 'brpeela';

--Owner's Equity
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 311, 'George Fielding, Capital', 20000.00, 'Credit', now(), true, id, 'Owner Equity'
  from accounting_user where username = 'brpeela';

insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 312, 'George Fielding, Drawing', 0.00, 'Debit', now(), true, id, 'Owner Equity'
  from accounting_user where username = 'brpeela';

--Expenses
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 511, 'Wages Expense', 0.00, 'Debit', now(), true, id, 'Expense'
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 521, 'Rent Expense', 0.00, 'Debit', now(), true, id, 'Expense'
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 525, 'Telephone Expense', 0.00, 'Debit', now(), true, id, 'Expense'
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 533, 'Utilities Expense', 0.00, 'Debit', now(), true, id, 'Expense'
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 534, 'Charitable Contributions Expense', 0.00, 'Debit', now(), true, id, 'Expense'
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 538, 'Automobile Expense', 0.00, 'Debit', now(), true, id, 'Expense'
  from accounting_user where username = 'brpeela';