use classicmodels;
SHOW PROCEDURE STATUS where db ='classicmodels';
SHOW PROCEDURE STATUS where name ='SelectAllCustomer';  
 
call SelectAllCustomer();

call SelectAllCustomerByCity('Singapore');

call SelectAllCustomerByCityAndName('Singapore','079903');

call get_order_by_customer(141, @shipped, @canceled, @resolved, @dispute);
select  @shipped, @canceled, @resolved, @dispute;

call Get_CustomerShipping(260, @pShipping);
select (@pShipping);

call Get_CustomerShipping(150, @pShipping);
select (@pShipping);

