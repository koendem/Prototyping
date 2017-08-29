db2 connect to test

db2 -tvsf dropTables.sql
db2 -tvsf schema.sql
db2 -tvsf insertKinds.sql

db2 connect reset


