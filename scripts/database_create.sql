
DROP INDEX IF EXISTS username_uidx;

drop table if exists accounting_trans_document cascade;
drop table if exists accounting_trans cascade;
drop table if exists trans_log cascade;
drop table if exists user_type cascade;
drop table if exists accounting_user cascade;
drop table if exists user_membership cascade;
drop table if exists user_permission cascade;
drop table if exists statement cascade;
drop table if exists account_statement cascade;
drop table if exists account cascade;
drop table if exists accounting_trans_entry cascade;
drop table if exists entry_log cascade;
drop table if exists user_password cascade;
drop table if exists account_seq cascade;

drop table if exists token cascade;

CREATE TABLE token
(
  id character varying(100) NOT NULL,
  data character varying(500),
  expiration bigint NOT NULL,
  user_id INTEGER NOT NULL,
  CONSTRAINT pk_session_id PRIMARY KEY (id)
);

-- Create Table accounting_trans_Entry
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans_entry
(
   id SERIAL NOT NULL
  ,trans_id INTEGER NOT NULL
  ,account_id INTEGER NOT NULL
  ,amount DECIMAL(15, 2) NOT NULL
  ,debit BOOLEAN NOT NULL
  ,CONSTRAINT PK_accounting_trans_Entry PRIMARY KEY (id)
);



-- Create Table accounting_trans
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans
(
   id SERIAL NOT NULL
  ,reported_by INTEGER NOT NULL
  ,approved_by INTEGER NULL
  ,reported TIMESTAMP NOT NULL
  ,approved TIMESTAMP  NULL
  ,status VARCHAR(10) NOT NULL
  ,description VARCHAR(500)  NULL
  ,CONSTRAINT PK_trans_id PRIMARY KEY (id)
);


-- Create Table accounting_trans_Document
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans_document
(
   id SERIAL NOT NULL
  ,trans_id int not null
  ,document_uri VARCHAR(250) NOT NULL
  ,CONSTRAINT PK_accounting_trans_document_id PRIMARY KEY (id)
);

-- Create Table Account
--------------------------------------------------------------------------------
CREATE TABLE account
(
   id SERIAL NOT NULL
  ,name VARCHAR(250) NOT NULL
  ,initial_balance DECIMAL(15, 2) NOT NULL
  ,normal_side VARCHAR(10) NOT NULL
  ,added TIMESTAMP NOT NULL
  ,active BOOLEAN NOT NULL
  ,added_by INTEGER NOT NULL
  ,category VARCHAR(250) NOT NULL
  ,subcategory VARCHAR(250)  NULL
  ,CONSTRAINT PK_Account_id PRIMARY KEY (id)
);


-- Create Table Account_Statement
--------------------------------------------------------------------------------
CREATE TABLE account_statement
(
   account_id int not null
  ,statement_id int not null
  ,CONSTRAINT PK_Account_Statement_account_id PRIMARY KEY (account_id, statement_id)
);



-- Create Table Statement
--------------------------------------------------------------------------------
CREATE TABLE statement
(
   id SERIAL NOT NULL
  ,name VARCHAR(250) NOT NULL
  ,CONSTRAINT PK_Statement_id PRIMARY KEY (id)
);



-- Create Table User
--------------------------------------------------------------------------------
CREATE TABLE accounting_user
(
   id SERIAL NOT NULL
  ,username VARCHAR(250) NOT NULL
  ,first_name VARCHAR(50) NOT NULL
  ,last_name VARCHAR(50) NOT NULL
  ,active BOOLEAN NOT NULL DEFAULT true
  ,email VARCHAR(250) NOT NULL
  ,login_attempts SMALLINT NOT NULL DEFAULT 0
  ,reset_on_logon BOOLEAN NOT NULL DEFAULT true
  ,CONSTRAINT PK_User_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX username_uidx ON accounting_user (username);

CREATE TABLE user_password
(
  id SERIAL NOT NULL,
  user_id INTEGER NOT NULL,
  password VARCHAR(100) NOT NULL,
  password_set TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Create Table User_Membership
--------------------------------------------------------------------------------
CREATE TABLE user_membership
(
   user_id INTEGER NOT NULL
  ,user_type_id int NOT NULL
  ,membership_start TIMESTAMP  NULL DEFAULT NOW()
  ,membership_end TIMESTAMP  NULL
  ,added_by INTEGER NOT NULL
  ,added TIMESTAMP NOT NULL DEFAULT NOW()
  ,CONSTRAINT PK_User_Membership PRIMARY KEY (user_id, user_type_id)
);

-- Create Table user_permission
--------------------------------------------------------------------------------
CREATE TABLE user_permission
(
   permission varchar(50) NOT NULL
  ,user_type_id int NOT NULL
  ,permission_group VARCHAR(50) NOT NULL
  ,group_order int default 0
  ,label varchar(100)
  ,style varchar(100)
  ,active boolean default true
  ,CONSTRAINT PK_User_Permission PRIMARY KEY (permission)
);


-- Create Table User_Type
--------------------------------------------------------------------------------
CREATE TABLE user_type
(
   type int NOT NULL
  ,description VARCHAR(250) NOT NULL
  ,CONSTRAINT PK_User_Type_type PRIMARY KEY (type)
);

-- Create Table trans_log
--------------------------------------------------------------------------------
CREATE TABLE trans_log
(
   id BIGSERIAL NOT NULL
  ,trans_id INTEGER NOT NULL
  ,status VARCHAR(10) NOT NULL
  ,changed_by INTEGER NOT NULL
  ,CONSTRAINT PK_trans_log_id PRIMARY KEY (id)
);

-- Create Table entry_log
--------------------------------------------------------------------------------
CREATE TABLE entry_log
(
   id BIGSERIAL NOT NULL
  ,accounting_trans_logid BIGINT NOT NULL
  ,entry_id int NOT NULL
  ,amount DECIMAL(15, 2) NOT NULL
  ,debit BOOLEAN NOT NULL
  ,changed_by INTEGER NOT NULL
  ,CONSTRAINT PK_entry_log_id PRIMARY KEY (id)
);

-- Create Foreign Key accounting_trans.reported_by -> User.id
ALTER TABLE accounting_trans ADD CONSTRAINT FK_accounting_trans_reported_by_User_id FOREIGN KEY (reported_by) REFERENCES Accounting_User(id);


-- Create Foreign Key accounting_trans.approved_by -> User.id
ALTER TABLE accounting_trans ADD CONSTRAINT FK_accounting_trans_approved_by_User_id FOREIGN KEY (approved_by) REFERENCES Accounting_User(id);


-- Create Foreign Key Account.added_by -> User.id
ALTER TABLE Account ADD CONSTRAINT FK_Account_added_by_User_id FOREIGN KEY (added_by) REFERENCES Accounting_User(id);


-- Create Foreign Key accounting_trans_Document.trans_id -> accounting_trans.id
ALTER TABLE accounting_trans_document ADD CONSTRAINT FK_accounting_trans_document_trans_id FOREIGN KEY (trans_id) REFERENCES accounting_trans(id);

-- Create Foreign Key accounting_trans_Entry.trans_id -> accounting_trans.id
ALTER TABLE accounting_trans_entry ADD CONSTRAINT FK_accounting_trans_entry_trans_id FOREIGN KEY (trans_id) REFERENCES accounting_trans(id);


-- Create Foreign Key User_Membership.user_type_id -> User_Type.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_user_type FOREIGN KEY (user_type_id) REFERENCES User_Type(type);


-- Create Foreign Key User_Membership.user_id -> User.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_user_id_User_id FOREIGN KEY (user_id) REFERENCES Accounting_User(id);


-- Create Foreign Key User_Membership.added_by -> User.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_added_by_User_id FOREIGN KEY (added_by) REFERENCES Accounting_User(id);

-- Create Foreign Key User_Membership.user_type_id -> User_Type.id--
ALTER TABLE user_permission ADD CONSTRAINT FK_User_permission_user_type FOREIGN KEY (user_type_id) REFERENCES User_Type(type);

-- Create Foreign Key Account_Statement.statement_id -> Statement.id
ALTER TABLE Account_Statement ADD CONSTRAINT FK_Account_Statement_statement_id_Statement_id FOREIGN KEY (statement_id) REFERENCES Statement(id);


-- Create Foreign Key Account_Statement.account_id -> Account.id
ALTER TABLE Account_Statement ADD CONSTRAINT FK_Account_Statement_account_id_Account_id FOREIGN KEY (account_id) REFERENCES Account(id);


-- Create Foreign Key accounting_trans_Entry.account_id -> Account.id
ALTER TABLE accounting_trans_Entry ADD CONSTRAINT FK_accounting_trans_Entry_account_id_Account_id FOREIGN KEY (account_id) REFERENCES Account(id);


-- Create Foreign Key trans_log.trans_id -> accounting_trans.id
ALTER TABLE trans_log ADD CONSTRAINT FK_trans_log_trans_id FOREIGN KEY (trans_id) REFERENCES accounting_trans(id);


-- Create Foreign Key entry_log.accounting_trans_logid -> trans_log.id
ALTER TABLE entry_log ADD CONSTRAINT FK_entry_log_trans_log_id FOREIGN KEY (accounting_trans_logid) REFERENCES trans_log(id);


-- Create Foreign Key entry_log.entry_id -> accounting_trans_Entry.id
ALTER TABLE entry_log ADD CONSTRAINT FK_entry_log_entry_id FOREIGN KEY (entry_id) REFERENCES accounting_trans_Entry(id);


-- Create Foreign Key trans_log.changed_by -> Accounting_User.id
ALTER TABLE trans_log ADD CONSTRAINT FK_trans_log_changed_by_Accounting_User_id FOREIGN KEY (changed_by) REFERENCES Accounting_User(id);


-- Create Foreign Key entry_log.changed_by -> Accounting_User.id
ALTER TABLE entry_log ADD CONSTRAINT FK_entry_log_changed_by_Accounting_User_id FOREIGN KEY (changed_by) REFERENCES Accounting_User(id);

-- Create Foreign Key entry_log.changed_by -> Accounting_User.id
ALTER TABLE token ADD CONSTRAINT FK_token_user_id FOREIGN KEY (user_id) REFERENCES Accounting_User(id);


-- Create Foreign Key user_password.userid -> User.id
ALTER TABLE user_password ADD CONSTRAINT FK_User_Password_User_id FOREIGN KEY (user_id) REFERENCES Accounting_User(id);

insert into user_type (type, description)  values (100, 'Administrator user');
insert into user_type (type, description)  values (50, 'Manager user');
insert into user_type (type, description)  values (10, 'Ordinary user');

insert into accounting_user (username, first_name, last_name, email, reset_on_logon) values ('brpeela', 'Brett', 'Peel', 'bpeel56@gmail.com', false);
insert into accounting_user (username, first_name, last_name, email, reset_on_logon) values ('brpeelm', 'Brett', 'Peel', 'bpeel56@gmail.com', false);
insert into accounting_user (username, first_name, last_name, email, reset_on_logon) values ('brpeel', 'Brett', 'Peel', 'bpeel56@gmail.com', false);

insert into accounting_user (username, first_name, last_name, email) values ('emamoa', 'Ermais', 'Mamo', 'emamo@spsu.edu');
insert into accounting_user (username, first_name, last_name, email) values ('emamom', 'Ermais', 'Mamo', 'emamo@spsu.edu');
insert into accounting_user (username, first_name, last_name, email) values ('emamo', 'Ermais', 'Mamo', 'emamo@spsu.edu');

insert into user_membership (user_id, user_type_id, added_by)
  select id, 100, (select id from accounting_user where username = 'brpeela')
  from accounting_user
  where username like '%a';

insert into user_membership (user_id, user_type_id, added_by)
  select id, 50, (select id from accounting_user where username = 'brpeela')
  from accounting_user
  where username like '%m';

insert into user_membership (user_id, user_type_id, added_by)
  select id, 10, (select id from accounting_user where username = 'brpeela')
  from accounting_user
  where username not like '%a' and username not like '%m';

insert into user_password (user_id, password)
  select id, '008c70392e3abfbd0fa47bbc2ed96aa99bd49e159727fcba0f2e6abeb3a9d601' from accounting_user;


----- Permissions

--Main Menu
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'accounts', 0, 10, 'Chart of Accounts', 'fa fa-columns');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'transactions', 1, 10, 'Transactions', 'fa fa-columns');



insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'trialBalance', 2, 10, 'TrialBalances', 'fa fa-usd');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'incomeStatement',3, 10, 'Income Statement', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'balanceSheet',4, 10, 'Balance Sheet', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'financialRatio',5, 10, 'Financial Ratio', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'ownersEquity',6, 10, 'Owner Equity', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style, active) values ('MainMenu', 'CashFlow',7, 10, 'Cash Flow', 'fa fa-pie-chart', false);

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'email', 20, 10, 'Send Email', 'fa fa-envelope-o');

--Transactions
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'createTrans', 1, 10, 'Add Transaction', 'fa fa-plus-square');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'submitTrans', 2, 10, 'Submit', 'fa fa-paper-plane');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'editTrans', 3, 10, 'Edit', 'fa fa-keyboard-0');


--Main Menu
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'AssignSurrogate', 2, 50, 'Assign Surrogate', 'fa fa-user');

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'ApproveTrans', 1, 50, 'Approve', 'fa fa-thumbs-up');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'RejectTrans', 2, 50, 'Reject', 'fa fa-thumbs-down');


insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'users', 19, 100, 'Users', 'fa fa-users');

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('User', 'createUser',0, 100, 'Add User', 'fa fa-user-plus');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('User', 'editUser', 1, 100, 'Edit User', 'fa fa-pencil');

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Account', 'createAccount', 0, 100, 'Add Account', 'fa fa-plus-square');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Account', 'removeAccount', 1, 100, 'Deactivate Account', 'fa fa-minus-square');

--Assets
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 101, 'Cash', 0.00, 'Credit', now(), true, id, 'Asset', NULL
  from accounting_user where username = 'brpeela';

insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 102, 'Office Supplies', 0.00, 'Credit', now(), true, id, 'Asset', null
  from accounting_user where username = 'brpeela';

--Revenues
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 501, 'Professional Fees', 0.00, 'Credit', now(), true, id, 'Revenue', null
  from accounting_user where username = 'brpeela';

--Liabilities
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 202, 'Accounts Payable', 0.00, 'Credit', now(), true, id, 'Liability', null
  from accounting_user where username = 'brpeela';

--Owners Equity
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 300, 'George Fielding, Capital', 20000.00, 'Credit', now(), true, id, 'Owner Equity', null
  from accounting_user where username = 'brpeela';

insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 301, 'George Fielding, Drawing', 0.00, 'Debit', now(), true, id, 'Owner Equity', null
  from accounting_user where username = 'brpeela';

--Expenses
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 401, 'Wages Expense', 0.00, 'Debit', now(), true, id, 'Expense', null
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 402, 'Rent Expense', 0.00, 'Debit', now(), true, id, 'Expense', null
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 403, 'Telephone Expense', 0.00, 'Debit', now(), true, id, 'Expense', null
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 404, 'Utilities Expense', 0.00, 'Debit', now(), true, id, 'Expense', null
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 405, 'Charitable Contributions Expense', 0.00, 'Debit', now(), true, id, 'Expense', null
  from accounting_user where username = 'brpeela';
insert into account (id, name, initial_balance, normal_side, added, active, added_by, category, subcategory)
  select 406, 'Automobile Expense', 0.00, 'Debit', now(), true, id, 'Expense', null
  from accounting_user where username = 'brpeela';


CREATE TABLE account_seq(
  category VARCHAR(250) NOT NULL,
  seq int NOT NULL DEFAULT 0,
  CONSTRAINT PK_Account_Seq PRIMARY KEY (category)
);

insert into account_seq (seq, category)
  select max(id) + 1, category from account group by category;


insert into accounting_trans (reported_by, approved_by, reported, approved, status, description)
  select 1, 4, now(), now(), 'Approved', 'Fielding invested cash to start the business, $20,000'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Paid Bollhorst Real Estate for December office rent, $1,000'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Received cash from Aaron Patton, a client, for services, $2,500'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Paid T.Z. Anderson Electric for December heating and light, $75'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Received cash Andrew Conder, a client, for services, $2,000'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Paid Fichters Super Service for gasoline and oil purchases for the company car, $60'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Paid Hillenburg Staffing for temporary secretarial services during the past two weeks, $600'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Bought office supplies from Bowers Office Supply on account, $280'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Paid Mitchell Telephone Co. for business calls during the past month, 100'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Fielding withdrew cash for personal use, $1,100'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Made donation to the National Multiple Sclerosis, $100'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Received cash from Billy Walters, a client, for services, $2,000'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Paid Hillenburg Staffing for temporary secretarial services during the the past two weeks, $600'
  UNION ALL select 1, 4, now(), now(), 'Approved', 'Made payment on account to Bowers Office Supply, $100'
;

insert into accounting_trans_entry (trans_id, account_id, amount, debit)
  select 5, 101, 20000, true
  UNION select 5, 300, 20000, false

  UNION select 6, 402, 1000, true
  UNION select 6, 101, 1000, false

  UNION select 7, 101, 2500, true
  UNION select 7, 501, 2500, false

  UNION select 8, 404, 75, true
  UNION select 8, 101, 75, false

  UNION select 9, 101, 2000, true
  UNION select 9, 501, 2000, false

  UNION select 10, 406, 60, true
  UNION select 10, 101, 60, false

  UNION select 11, 401, 600, true
  UNION select 11, 101, 600, false

  UNION select 12, 102, 280, true
  UNION select 12, 202, 280, false

  UNION select 13, 403, 100, true
  UNION select 13, 101, 100, false

  UNION select 14, 301, 1100, true
  UNION select 14, 101, 1100, false

  UNION select 15, 405, 100, true
  UNION select 15, 101, 100, false

  UNION select 16, 101, 2000, true
  UNION select 16, 501, 2000, false

  UNION select 17, 401, 600, true
  UNION select 17, 101, 600, false

  UNION select 18, 202, 100, true
  UNION select 18, 101, 100, false;
;