
ALTER TABLE Transaction DROP CONSTRAINT FK_Transaction_reported_by_User_id;
ALTER TABLE Transaction DROP CONSTRAINT FK_Transaction_approved_by_User_id;
ALTER TABLE Account DROP CONSTRAINT FK_Account_added_by_User_id;
ALTER TABLE Transaction_Document DROP CONSTRAINT FK_Transaction_Document_transaction_id_Transaction_id;
ALTER TABLE Transaction_Entry DROP CONSTRAINT FK_Transaction_Entry_transaction_id_Transaction_id;
ALTER TABLE User_Membership DROP CONSTRAINT FK_User_Membership_user_type_id_User_Type_id;
ALTER TABLE User_Membership DROP CONSTRAINT FK_User_Membership_user_id_User_id;
ALTER TABLE User_Membership DROP CONSTRAINT FK_User_Membership_added_by_User_id;
ALTER TABLE Account_Statement DROP CONSTRAINT FK_Account_Statement_statement_id_Statement_id;
ALTER TABLE Account_Statement DROP CONSTRAINT FK_Account_Statement_account_id_Account_id;
ALTER TABLE Transaction_Entry DROP CONSTRAINT FK_Transaction_Entry_account_id_Account_id;



drop table Transaction_Entry;
drop table TRANSACTION;
drop table Transaction_Document;
DROP TABLE Account;
DROP TABLE Account_Statement;
DROP TABLE Statement;
DROP TABLE Accounting_User;
DROP TABLE User_Membership;
DROP TABLE User_Type;



-- Create Table: Transaction_Entry
--------------------------------------------------------------------------------
CREATE TABLE Transaction_Entry
(
	id SERIAL
	,transaction_id INTEGER NOT NULL
	,account_id INTEGER NOT NULL
	,amount DECIMAL(15, 2) NOT NULL
	,CONSTRAINT PK_Transaction_Entry PRIMARY KEY (id)
);



-- Create Table: Transaction
--------------------------------------------------------------------------------
CREATE TABLE Transaction
(
	id SERIAL
	,reported_by INTEGER NOT NULL
	,approved_by INTEGER NOT NULL
	,reported TIMESTAMP NOT NULL
	,approved TIMESTAMP  NULL
	,status VARCHAR(10) NOT NULL
	,source_document VARCHAR(250)  NULL
	,CONSTRAINT PK_Transaction_id PRIMARY KEY (id)
);


-- Create Table: Transaction_Document
--------------------------------------------------------------------------------
CREATE TABLE Transaction_Document
(
	id SERIAL
	,transaction_id int
	,document_uri VARCHAR(250) NOT NULL
	,CONSTRAINT PK_Transaction_Document_id PRIMARY KEY (id)
);

-- Create Table: Account
--------------------------------------------------------------------------------
CREATE TABLE Account
(
	id SERIAL
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
CREATE TABLE Account_Statement
(
	account_id SERIAL
	,statement_id SERIAL
	,CONSTRAINT PK_Account_Statement_account_id PRIMARY KEY (account_id, statement_id)
);



-- Create Table: Statement
--------------------------------------------------------------------------------
CREATE TABLE Statement
(
	id SERIAL
	,name VARCHAR(250) NOT NULL
	,CONSTRAINT PK_Statement_id PRIMARY KEY (id)
);



-- Create Table: User
--------------------------------------------------------------------------------
CREATE TABLE Accounting_User
(
	id SERIAL
	,username VARCHAR(250) NOT NULL
	,password VARCHAR(50) NOT NULL
	,first_name VARCHAR(50) NOT NULL
	,last_name VARCHAR(50) NOT NULL
	,active BOOLEAN NOT NULL DEFAULT true
	,locked BOOLEAN NOT NULL DEFAULT false
	,password_reset TIMESTAMP NULL
	,email VARCHAR(250) NOT NULL
	,login_attempts SMALLINT NOT NULL DEFAULT 0
	,CONSTRAINT PK_User_id PRIMARY KEY (id)
);



-- Create Table: User_Membership
--------------------------------------------------------------------------------
CREATE TABLE User_Membership
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
CREATE TABLE User_Type
(
	id INTEGER NOT NULL
	,type VARCHAR(10) NOT NULL
	,description VARCHAR(250) NOT NULL
	,CONSTRAINT PK_User_Type_id PRIMARY KEY (id)
);



-- Create Foreign Key: Transaction.reported_by -> User.id
ALTER TABLE Transaction ADD CONSTRAINT FK_Transaction_reported_by_User_id FOREIGN KEY (reported_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: Transaction.approved_by -> User.id
ALTER TABLE Transaction ADD CONSTRAINT FK_Transaction_approved_by_User_id FOREIGN KEY (approved_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: Account.added_by -> User.id
ALTER TABLE Account ADD CONSTRAINT FK_Account_added_by_User_id FOREIGN KEY (added_by) REFERENCES Accounting_User(id);


-- Create Foreign Key: Transaction_Document.transaction_id -> Transaction.id
ALTER TABLE Transaction_Document ADD CONSTRAINT FK_Transaction_Document_transaction_id_Transaction_id FOREIGN KEY (transaction_id) REFERENCES Transaction(id);

-- Create Foreign Key: Transaction_Entry.transaction_id -> Transaction.id
ALTER TABLE Transaction_Entry ADD CONSTRAINT FK_Transaction_Entry_transaction_id_Transaction_id FOREIGN KEY (transaction_id) REFERENCES Transaction(id);


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


-- Create Foreign Key: Transaction_Entry.account_id -> Account.id
ALTER TABLE Transaction_Entry ADD CONSTRAINT FK_Transaction_Entry_account_id_Account_id FOREIGN KEY (account_id) REFERENCES Account(id);

drop table session;
CREATE TABLE session
(
	id varchar(100)
	,data varchar(500)
	,expiration BigInt
	,CONSTRAINT PK_Session_id PRIMARY KEY (id)
);
