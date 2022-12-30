delimiter //
create function CustomerLevel(
amount decimal(10,2)) 
returns varchar(20)
deterministic
begin
	declare customerLevel varchar(20);
    if amount > 50000 then
		set customerLevel = 'PLATINUM';
	elseif amount >= 10000 and amount <=50000 then
		set customerLevel = 'GOLD';
	elseif amount <10000 then
		set customerLevel = 'SILVER';
    end if;
    return customerLevel;
end //
delimiter ;

-- call stored function
select name, status, CustomerLevel(amount) from orders;