delimiter //
create procedure SelectAllUserByNameAndEmail(
in myName varchar(255),
in myEmail varchar(255)
)
begin
	select * from users where name = myName COLLATE utf8mb4_unicode_ci and email = myEmail COLLATE utf8mb4_unicode_ci;
end //
delimiter ;

call SelectAllUserByNameAndEmail("Faysal", "faysal@gmail.com");