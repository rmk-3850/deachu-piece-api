CREATE DATABASE piece_schema;

CREATE USER 'piece_app'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON piece_schema.* TO 'piece_app'@'%';

FLUSH PRIVILEGES;