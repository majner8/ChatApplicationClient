WSQLite
BBB
create table if not exists chatXXX(
userUUID TEXT(20), 
    message TEXT(300),  
    TimeOfMessage DATETIME DEFAULT CURRENT_TIMESTAMP, 
    numberOFmessage unique default null, 
    MessageUUID TEXT(15) UNIQUE
);
CCC

WSQLite
BBB
create table if not exists AdministrationXXX(
userUUID varchar(20) unique, chatName varchar(15) default null
);