ISQLite
BBB
INSERT INTO UserXXX(chatUUID,ChatEnd,UserTableName) 
values (ZZZ,ZZZ,ZZZ)
ON CONFLICT(chatUUID) 
DO UPDATE SET ChatEnd=excluded.ChatEnd, UserTableName=excluded.UserTableName;

