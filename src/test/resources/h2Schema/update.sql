alter table accounting_trans_document add column file_name varchar(100);
alter table accounting_trans_document add column content_type varchar(100);
alter table accounting_trans_document add column size INTEGER;
alter table accounting_trans_document add column hash VARCHAR(100);
alter table accounting_trans_document add column uploaded TIMESTAMP DEFAULT NOW();
alter table accounting_trans_document add column file TEXT;

ALTER TABLE accounting_trans_document drop document_uri;


alter table accounting_trans_document add column uploaded_by int ;

-- Create Foreign Key Account.added_by -> User.id
ALTER TABLE accounting_trans_document ADD CONSTRAINT FK_uploaded_by_User_id FOREIGN KEY (uploaded_by) REFERENCES Accounting_User(id);

update accounting_trans_document set uploaded_by = 1;