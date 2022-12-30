delimiter $$
create procedure SelectAllUserByName(
IN myName varchar (255)
)
begin
	select * from users where name=myName COLLATE utf8mb4_unicode_ci;
end $$
delimiter ;

call SelectAllUserByName("Faysal");

-- or
-- set @myName = "Faysal";
-- call SelectAllUserByName(@myName);
