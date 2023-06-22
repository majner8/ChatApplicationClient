WSQLite
BBB

-- chat end when a value is not null it mean that user was kick from server,
create table if not exists UserXXX(chatUUID varchar(20) primary key,ChatEnd DateTime default null,UserTableName varchar(20)) ;

