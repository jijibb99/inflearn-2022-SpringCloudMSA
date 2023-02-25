CREATE DATABASE orderdb ;

create user 'sa'@'%' identified by '1234';
grant all privileges on orderdb.* to 'sa'@'%' identified by '1234';
flush privileges;