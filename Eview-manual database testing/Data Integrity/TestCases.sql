-- connect with schema (Database)
-- Check table presence in database schema
-- Check table name conversion
-- Check or count number of columns in a table
-- check data type of columns in a table
-- Check column size in a table
-- Check null fields in a table
-- Check column keys in a table
-- check varchar data type of columns in a table
-- check or count number of varchar data type of columns in a table

use laravelcommerce;   

show tables; 

select column_name from information_schema.columns where table_name='users';
select count(*) as NameOfColumns from information_schema.columns where table_name='users'; 
select column_name, data_type from information_schema.columns where table_name='users'; 
select column_name, column_type from information_schema.columns where table_name = "users"; 
select column_name, is_nullable from information_schema.columns where table_name = "users";
select column_name, column_key from information_schema.columns where table_name = "users";

select column_name, data_type from information_schema.columns where table_name = "users" and data_type="varchar";
select count(*) as NameOfColumns from information_schema.columns where table_name = "users" and data_type="varchar";


describe information_schema.columns;    -- describe all the information of metadata of the table 


