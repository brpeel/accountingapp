ALTER TABLE accounting_trans_document drop IF EXISTS file_name;
ALTER TABLE accounting_trans_document drop IF EXISTS content_type;
ALTER TABLE accounting_trans_document drop IF EXISTS size;
ALTER TABLE accounting_trans_document drop IF EXISTS hash;
ALTER TABLE accounting_trans_document drop IF EXISTS uploaded;
ALTER TABLE accounting_trans_document drop IF EXISTS uploaded_by;
ALTER TABLE accounting_trans_document drop IF EXISTS file;

ALTER TABLE accounting_trans_document drop IF EXISTS document_uri;

alter table accounting_trans_document add column file_name varchar(100);
alter table accounting_trans_document add column content_type varchar(100);
alter table accounting_trans_document add column size INTEGER;
alter table accounting_trans_document add column hash VARCHAR(100);
alter table accounting_trans_document add column uploaded TIMESTAMP DEFAULT NOW();
alter table accounting_trans_document add column file TEXT;
alter table accounting_trans_document add column uploaded_by int ;

-- Create Foreign Key Account.added_by -> User.id
ALTER TABLE accounting_trans_document ADD CONSTRAINT FK_uploaded_by_User_id FOREIGN KEY (uploaded_by) REFERENCES Accounting_User(id);

update accounting_trans_document set uploaded_by = 1;


ALTER TABLE account drop IF EXISTS orderno;

ALTER TABLE account ADD COLUMN orderno int DEFAULT 100;


insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 103, 'Accounts Recievable', 0.00, 'Credit', now(), true, (select id from accounting_user where username = 'brpeela') as uid,
    'Asset', null
  where 0 = (select count(*) from account where name = 'Accounts Recievable');

--cash must be listed first followed by notes or accounts receivable and so forth.

UPDATE Account SET orderno = 0, subcategory = 'Cash' WHERE name = 'Cash';
UPDATE Account SET orderno = 1, subcategory = 'Current Asset' WHERE name = 'Accounts Recievable';
UPDATE Account SET orderno = 10, subcategory = 'Current Asset' WHERE name = 'Office Supplies';

UPDATE Account SET normal_side = 'Debit' WHERE category = 'Asset';
UPDATE Account SET normal_side = 'Debit' WHERE category = 'Expense';
UPDATE Account SET normal_side = 'Credit' WHERE category = 'Liability';
UPDATE Account SET normal_side = 'Credit' WHERE category = 'Owner Equity' and subcategory = 'Investment';
UPDATE Account SET normal_side = 'Debit' WHERE category = 'Owner Equity' and subcategory = 'Withdraw';
UPDATE Account SET normal_side = 'Credit' WHERE category = 'Revenue';

update account set subcategory = 'Operating Expense' where name = 'Wages Expense';
update account set subcategory = 'Operating Expense' where name = 'Utilities Expense';
update account set subcategory = 'Operating Expense' where name = 'Telephone Expense';
update account set subcategory = 'Operating Expense' where name = 'Rent Expense';
update account set subcategory = 'Revenue' where name = 'Professional Fees';
update account set subcategory = 'Other Expense' where name = 'Charitable Contributions Expense';
update account set subcategory = 'Operating Expense' where name = 'Automobile Expense';
update account set subcategory = 'Current Liability' where name = 'Accounts Payable';

UPDATE account set initial_balance = 0;

