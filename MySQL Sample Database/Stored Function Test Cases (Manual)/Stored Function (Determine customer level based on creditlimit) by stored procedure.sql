delimiter $$

create procedure GetCustomerLevel(
IN customerNo INT,
OUT customerLevel VARCHAR(20)
)

begin
	declare credit dec(10,2) default 0;
    
    -- get credit limit of a customer
    select creditLimit into credit from customers where customerNumber = customerNo;
    
    -- call the function
    set customerLevel = CustomerLevel(credit);
end $$
delimiter ;