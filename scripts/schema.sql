CREATE DATABASE IF NOT EXISTS filesserver;

USE filesserver;

CREATE TABLE IF NOT EXISTS tbl_files (
id varchar(64) primary key,
file_name varchar(500) not null,
file_size int not null,
file_type varchar(50) not null,
file_content mediumblob not null,
file_duration int not null default 0,
file_uploaded timestamp
);

GRANT ALL PRIVILEGES ON *.* TO 'uploader';

FLUSH PRIVILEGES;

