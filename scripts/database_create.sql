drop table if exists accounting_trans_document cascade;
drop table if exists accounting_trans cascade;
drop table if exists trans_log cascade;
drop table if exists user_type cascade;
drop table if exists accounting_user cascade;
drop table if exists user_membership cascade;
drop table if exists statement cascade;
drop table if exists account_statement cascade;
drop table if exists account cascade;
drop table if exists accounting_trans_entry cascade;
drop table if exists entry_log cascade;

drop table if exists token cascade;

CREATE TABLE token
(
  id character varying(100) NOT NULL,
  data character varying(500),
  expiration bigint,
  user_id INTEGER NOT NULL,
  CONSTRAINT pk_session_id PRIMARY KEY (id)
);

-- Create Table: accounting_trans_Entry
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans_entry
(
	id INTEGER NOT NULL
	,accounting_trans_id INTEGER NOT NULL
	,account_id INTEGER NOT NULL
	,account DECIMAL(15, 2) NOT NULL
	,CONSTRAINT PK_accounting_trans_Entry PRIMARY KEY (id)
);



-- Create Table: accounting_trans
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans
(
	id SERIAL NOT NULL
	,reported_by INTEGER NOT NULL
	,approved_by INTEGER NOT NULL
	,reported TIMESTAMP NOT NULL
	,approved TIMESTAMP  NULL
	,status VARCHAR(10) NOT NULL
	,description VARCHAR(500)  NULL
	,CONSTRAINT PK_accounting_trans_id PRIMARY KEY (id)
);


-- Create Table: accounting_trans_Document
--------------------------------------------------------------------------------
CREATE TABLE accounting_trans_document
(
	id SERIAL NOT NULL
	,accounting_trans_id int not null
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
	,password VARCHAR(50) NOT NULL
	,first_name VARCHAR(50) NOT NULL
	,last_name VARCHAR(50) NOT NULL
	,active BOOLEAN NOT NULL
	,locked BOOLEAN NOT NULL
	,password_reset TIMESTAMP NOT NULL
	,email VARCHAR(250) NOT NULL
	,login_attempts SMALLINT NOT NULL
	,CONSTRAINT PK_User_id PRIMARY KEY (id)
);



-- Create Table: User_Membership
--------------------------------------------------------------------------------
CREATE TABLE user_membership
(
	user_id INTEGER NOT NULL
	,user_type_id INTEGER NOT NULL
	,membership_start TIMESTAMP  NULL
	,membership_end TIMESTAMP  NULL
	,added_by INTEGER NOT NULL
	,added TIMESTAMP NOT NULL
	,CONSTRAINT PK_User_Membership PRIMARY KEY (user_id, user_type_id)
);



-- Create Table: User_Type
--------------------------------------------------------------------------------
CREATE TABLE user_type
(
	id SERIAL NOT NULL
	,type VARCHAR(10) NOT NULL
	,description VARCHAR(250) NOT NULL
	,CONSTRAINT PK_User_Type_id PRIMARY KEY (id)
);

-- Create Table: trans_log
--------------------------------------------------------------------------------
CREATE TABLE trans_log
(
	id BIGSERIAL NOT NULL
	,accounting_trans_id INTEGER NOT NULL
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
	,changed_by INTEGER NOT NULL
	,CONSTRAINT PK_entry_log_id PRIMARY KEY (id)
);

-- Create Foreign Key: accounting_trans.reported_by -> User.id
ALTER TABLE accounting_trans ADD CONSTRAINT FK_accounting_trans_reported_by_User_id FOREIGN KEY (reported_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: accounting_trans.approved_by -> User.id
ALTER TABLE accounting_trans ADD CONSTRAINT FK_accounting_trans_approved_by_User_id FOREIGN KEY (approved_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: Account.added_by -> User.id
ALTER TABLE Account ADD CONSTRAINT FK_Account_added_by_User_id FOREIGN KEY (added_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: accounting_trans_Document.accounting_trans_id -> accounting_trans.id
ALTER TABLE accounting_trans_document ADD CONSTRAINT FK_accounting_trans_document_accounting_trans_id FOREIGN KEY (accounting_trans_id) REFERENCES accounting_trans(id);

-- Create Foreign Key: accounting_trans_Entry.accounting_trans_id -> accounting_trans.id
ALTER TABLE accounting_trans_entry ADD CONSTRAINT FK_accounting_trans_entry_accounting_trans_id FOREIGN KEY (accounting_trans_id) REFERENCES accounting_trans(id);


-- Create Foreign Key: User_Membership.user_type_id -> User_Type.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_user_type_id_User_Type_id FOREIGN KEY (user_type_id) REFERENCES User_Type(id);


-- Create Foreign Key: User_Membership.user_id -> User.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_user_id_User_id FOREIGN KEY (user_id) REFERENCES Accounting_User(id);


-- Create Foreign Key: User_Membership.added_by -> User.id
ALTER TABLE User_Membership ADD CONSTRAINT FK_User_Membership_added_by_User_id FOREIGN KEY (added_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: Account_Statement.statement_id -> Statement.id
ALTER TABLE Account_Statement ADD CONSTRAINT FK_Account_Statement_statement_id_Statement_id FOREIGN KEY (statement_id) REFERENCES Statement(id);


-- Create Foreign Key: Account_Statement.account_id -> Account.id
ALTER TABLE Account_Statement ADD CONSTRAINT FK_Account_Statement_account_id_Account_id FOREIGN KEY (account_id) REFERENCES Account(id);


-- Create Foreign Key: accounting_trans_Entry.account_id -> Account.id
ALTER TABLE accounting_trans_Entry ADD CONSTRAINT FK_accounting_trans_Entry_account_id_Account_id FOREIGN KEY (account_id) REFERENCES Account(id);


-- Create Foreign Key: trans_log.accounting_trans_id -> accounting_trans.id
ALTER TABLE trans_log ADD CONSTRAINT FK_trans_log_accounting_trans_id FOREIGN KEY (accounting_trans_id) REFERENCES accounting_trans(id);


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
