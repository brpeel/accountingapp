
grant all privileges on schema public to accounting_user;

GRANT SELECT ON ALL TABLES IN SCHEMA public TO accounting_user;
GRANT UPDATE ON ALL TABLES IN SCHEMA public TO accounting_user;
GRANT INSERT ON ALL TABLES IN SCHEMA public TO accounting_user;
GRANT DELETE ON ALL TABLES IN SCHEMA public TO accounting_user;
GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO accounting_user;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO accounting_user;
