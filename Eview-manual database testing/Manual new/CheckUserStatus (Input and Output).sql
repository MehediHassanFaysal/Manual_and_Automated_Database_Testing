delimiter //
create procedure CheckUserStatus(
in userID bigint,
out userStatus varchar(255)
)
begin
	declare customerStatus varchar(255);
		select is_active into customerStatus from users where id = userID COLLATE utf8mb4_unicode_ci;
		
		CASE customerStatus
			WHEN 1 THEN
				SET userStatus = "Active";
			ELSE
				SET userStatus = "InActive";
		
		END CASE;
    
end //
delimiter ;

-- invoke or call 
call CheckUserStatus(19, @userStatus);
select(@userStatus);