use laravelcommerce;

delimiter //
create procedure GetALlUsers()
begin
	select * from users;
end //
delimiter ;

call GetALlUsers()