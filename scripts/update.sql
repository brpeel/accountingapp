ALTER TABLE accounting_trans_document drop IF EXISTS file_name;
ALTER TABLE accounting_trans_document drop IF EXISTS content_type;
ALTER TABLE accounting_trans_document drop IF EXISTS size;
ALTER TABLE accounting_trans_document drop IF EXISTS hash;
ALTER TABLE accounting_trans_document drop IF EXISTS uploaded;
ALTER TABLE accounting_trans_document drop IF EXISTS file;

ALTER TABLE accounting_trans_document drop IF EXISTS document_uri;

alter table accounting_trans_document add column file_name varchar(100);
alter table accounting_trans_document add column content_type varchar(100);
alter table accounting_trans_document add column size INTEGER;
alter table accounting_trans_document add column hash VARCHAR(100);
alter table accounting_trans_document add column uploaded TIMESTAMP DEFAULT NOW();
alter table accounting_trans_document add column file TEXT;


update user_permission set group_order = group_order + 1 where permission_group = 'MainMenu' and group_order > 1;

insert into user_permission (permission, user_type_id, permission_group, group_order, label, style, active)
values ('findTransaction', 10, 'MainMenu', 3, 'Find Transaction', 'fa fa-columns', true);

