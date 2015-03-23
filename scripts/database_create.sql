
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

drop table if exists token cascade;

CREATE TABLE token
(
  id character varying(100) NOT NULL,
  data character varying(500),
  expiration bigint NOT NULL,
  user_id INTEGER NOT NULL,
  CONSTRAINT pk_session_id PRIMARY KEY (id)
);

-- Create Table: accounting_trans_Entry
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans_entry
(
   id INTEGER NOT NULL
  ,trans_id INTEGER NOT NULL
  ,account_id INTEGER NOT NULL
  ,account DECIMAL(15, 2) NOT NULL
  ,debit BOOLEAN NOT NULL
  ,CONSTRAINT PK_accounting_trans_Entry PRIMARY KEY (id)
);



-- Create Table: accounting_trans
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


-- Create Table: accounting_trans_Document
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans_document
(
   id SERIAL NOT NULL
  ,trans_id int not null
  ,document_uri VARCHAR(250) NOT NULL
  ,CONSTRAINT PK_accounting_trans_document_id PRIMARY KEY (id)
);

-- Create Table: Account
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
  ,subcategory VARCHAR(250)  NULL
  ,CONSTRAINT PK_Account_id PRIMARY KEY (id)
);



-- Create Table: Account_Statement
--------------------------------------------------------------------------------
CREATE TABLE account_statement
(
   account_id int not null
  ,statement_id int not null
  ,CONSTRAINT PK_Account_Statement_account_id PRIMARY KEY (account_id, statement_id)
);



-- Create Table: Statement
--------------------------------------------------------------------------------
CREATE TABLE statement
(
   id SERIAL NOT NULL
  ,name VARCHAR(250) NOT NULL
  ,CONSTRAINT PK_Statement_id PRIMARY KEY (id)
);



-- Create Table: User
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

-- Create Table: User_Membership
--------------------------------------------------------------------------------
CREATE TABLE user_membership
(
   user_id INTEGER NOT NULL
  ,user_type_id VARCHAR(10) NOT NULL
  ,membership_start TIMESTAMP  NULL DEFAULT NOW()
  ,membership_end TIMESTAMP  NULL
  ,added_by INTEGER NOT NULL
  ,added TIMESTAMP NOT NULL DEFAULT NOW()
  ,CONSTRAINT PK_User_Membership PRIMARY KEY (user_id, user_type_id)
);

-- Create Table: user_permission
--------------------------------------------------------------------------------
CREATE TABLE user_permission
(
   permission varchar(50) NOT NULL
  ,user_type_id VARCHAR(10) NOT NULL
  ,permission_group VARCHAR(50) NOT NULL
  ,group_order int default 0
  ,label varchar(100)
  ,style varchar(100)
  ,active boolean default true
  ,CONSTRAINT PK_User_Permission PRIMARY KEY (permission)
);


-- Create Table: User_Type
--------------------------------------------------------------------------------
CREATE TABLE user_type
(
   type VARCHAR(10) NOT NULL
  ,description VARCHAR(250) NOT NULL
  ,CONSTRAINT PK_User_Type_type PRIMARY KEY (type)
);

-- Create Table: trans_log
--------------------------------------------------------------------------------
CREATE TABLE trans_log
(
   id BIGSERIAL NOT NULL
  ,trans_id INTEGER NOT NULL
  ,status VARCHAR(10) NOT NULL
  ,changed_by INTEGER NOT NULL
  ,CONSTRAINT PK_trans_log_id PRIMARY KEY (id)
);

-- Create Table: entry_log
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

-- Create Foreign Key: accounting_trans.reported_by -> User.id
ALTER TABLE accounting_trans ADD CONSTRAINT FK_accounting_trans_reported_by_User_id FOREIGN KEY (reported_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: accounting_trans.approved_by -> User.id
ALTER TABLE accounting_trans ADD CONSTRAINT FK_accounting_trans_approved_by_User_id FOREIGN KEY (approved_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: Account.added_by -> User.id
ALTER TABLE Account ADD CONSTRAINT FK_Account_added_by_User_id FOREIGN KEY (added_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: accounting_trans_Document.trans_id -> accounting_trans.id
ALTER TABLE accounting_trans_document ADD CONSTRAINT FK_accounting_trans_document_trans_id FOREIGN KEY (trans_id) REFERENCES accounting_trans(id);

-- Create Foreign Key: accounting_trans_Entry.trans_id -> accounting_trans.id
ALTER TABLE accounting_trans_entry ADD CONSTRAINT FK_accounting_trans_entry_trans_id FOREIGN KEY (trans_id) REFERENCES accounting_trans(id);


-- Create Foreign Key: User_Membership.user_type_id -> User_Type.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_user_type FOREIGN KEY (user_type_id) REFERENCES User_Type(type);


-- Create Foreign Key: User_Membership.user_id -> User.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_user_id_User_id FOREIGN KEY (user_id) REFERENCES Accounting_User(id);


-- Create Foreign Key: User_Membership.added_by -> User.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_added_by_User_id FOREIGN KEY (added_by) REFERENCES Accounting_User(id);

-- Create Foreign Key: User_Membership.user_type_id -> User_Type.id--
ALTER TABLE user_permission ADD CONSTRAINT FK_User_permission_user_type FOREIGN KEY (user_type_id) REFERENCES User_Type(type);

-- Create Foreign Key: Account_Statement.statement_id -> Statement.id
ALTER TABLE Account_Statement ADD CONSTRAINT FK_Account_Statement_statement_id_Statement_id FOREIGN KEY (statement_id) REFERENCES Statement(id);


-- Create Foreign Key: Account_Statement.account_id -> Account.id
ALTER TABLE Account_Statement ADD CONSTRAINT FK_Account_Statement_account_id_Account_id FOREIGN KEY (account_id) REFERENCES Account(id);


-- Create Foreign Key: accounting_trans_Entry.account_id -> Account.id
ALTER TABLE accounting_trans_Entry ADD CONSTRAINT FK_accounting_trans_Entry_account_id_Account_id FOREIGN KEY (account_id) REFERENCES Account(id);


-- Create Foreign Key: trans_log.trans_id -> accounting_trans.id
ALTER TABLE trans_log ADD CONSTRAINT FK_trans_log_trans_id FOREIGN KEY (trans_id) REFERENCES accounting_trans(id);


-- Create Foreign Key: entry_log.accounting_trans_logid -> trans_log.id
ALTER TABLE entry_log ADD CONSTRAINT FK_entry_log_trans_log_id FOREIGN KEY (accounting_trans_logid) REFERENCES trans_log(id);


-- Create Foreign Key: entry_log.entry_id -> accounting_trans_Entry.id
ALTER TABLE entry_log ADD CONSTRAINT FK_entry_log_entry_id FOREIGN KEY (entry_id) REFERENCES accounting_trans_Entry(id);


-- Create Foreign Key: trans_log.changed_by -> Accounting_User.id
ALTER TABLE trans_log ADD CONSTRAINT FK_trans_log_changed_by_Accounting_User_id FOREIGN KEY (changed_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: entry_log.changed_by -> Accounting_User.id
ALTER TABLE entry_log ADD CONSTRAINT FK_entry_log_changed_by_Accounting_User_id FOREIGN KEY (changed_by) REFERENCES Accounting_User(id);

-- Create Foreign Key: entry_log.changed_by -> Accounting_User.id
ALTER TABLE token ADD CONSTRAINT FK_token_user_id FOREIGN KEY (user_id) REFERENCES Accounting_User(id);


-- Create Foreign Key: user_password.userid -> User.id
ALTER TABLE user_password ADD CONSTRAINT FK_User_Password_User_id FOREIGN KEY (user_id) REFERENCES Accounting_User(id);

insert into user_type (type, description)  values ('admin', 'Administrator user');
insert into user_type (type, description)  values ('manager', 'Manager user');
insert into user_type (type, description)  values ('user', 'Ordinary user');

insert into accounting_user (username, first_name, last_name, email, reset_on_logon) values ('brpeela', 'Brett', 'Peel', 'bpeel56@gmail.com', false);
insert into accounting_user (username, first_name, last_name, email, reset_on_logon) values ('brpeelm', 'Brett', 'Peel', 'bpeel56@gmail.com', false);
insert into accounting_user (username, first_name, last_name, email, reset_on_logon) values ('brpeel', 'Brett', 'Peel', 'bpeel56@gmail.com', false);

insert into accounting_user (username, first_name, last_name, email) values ('emamoa', 'Ermais', 'Mamo', 'emamo@spsu.edu');
insert into accounting_user (username, first_name, last_name, email) values ('emamom', 'Ermais', 'Mamo', 'emamo@spsu.edu');
insert into accounting_user (username, first_name, last_name, email) values ('emamo', 'Ermais', 'Mamo', 'emamo@spsu.edu');

insert into user_membership (user_id, user_type_id, added_by)
  select id, 'admin', (select id from accounting_user where username = 'brpeela')
  from accounting_user
  where username like '%a';

insert into user_membership (user_id, user_type_id, added_by)
  select id, 'manager', (select id from accounting_user where username = 'brpeela')
  from accounting_user
  where username like '%m';

insert into user_membership (user_id, user_type_id, added_by)
  select id, 'user', (select id from accounting_user where username = 'brpeela')
  from accounting_user
  where username not like '%a' and username not like '%m';

insert into user_password (user_id, password)
  select id, '008c70392e3abfbd0fa47bbc2ed96aa99bd49e159727fcba0f2e6abeb3a9d601' from accounting_user;

insert into account (name, initial_balance, normal_side, added, active, added_by, subcategory)
  select 'Service Revenue', 0.00, 'Credit', now(), true, id, 'Revenue' from accounting_user where username = 'brpeela';

----- Permissions

--Main Menu
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'accounts', 0, 'user', 'Chart of Accounts', 'fa fa-columns');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'transactions', 1, 'user', 'Transactions', 'fa fa-columns');



insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'trialBalance', 2, 'user', 'TrialBalances', 'fa fa-usd');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'incomeStatement',3, 'user', 'Income Statement', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'balanceSheet',4, 'user', 'Balance Sheet', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'financialRatio',5, 'user', 'Financial Ratio', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'ownersEquity',6, 'user', 'Owner Equity', 'fa fa-pie-chart');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style, active) values ('MainMenu', 'CashFlow',7, 'user', 'Cash Flow', 'fa fa-pie-chart', false);

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'email', 20, 'user', 'Send Email', 'fa fa-envelope-o');

--Transactions
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'createTrans', 1, 'user', 'Add Transaction', 'fa fa-plus-square');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'submitTrans', 2, 'user', 'Submit', 'fa fa-paper-plane');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'editTrans', 3, 'user', 'Edit', 'fa fa-keyboard-0');


--Main Menu
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'AssignSurrogate', 2, 'manager', 'Assign Surrogate', 'fa fa-user');

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'ApproveTrans', 1, 'manager', 'Approve', 'fa fa-thumbs-up');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Transaction', 'RejectTrans', 2, 'manager', 'Reject', 'fa fa-thumbs-down');


insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('MainMenu', 'users', 19, 'admin', 'Users', 'fa fa-users');

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('User', 'createUser',0, 'admin', 'Add User', 'fa fa-user-plus');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('User', 'editUser', 1, 'admin', 'Edit User', 'fa fa-pencil');

insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Account', 'createAccount', 0, 'admin', 'Add Account', 'fa fa-plus-square');
insert into user_permission (permission_group, permission, group_order, user_type_id, label, style) values ('Account', 'removeAccount', 1, 'admin', 'Deactivate Account', 'fa fa-minus-square');


