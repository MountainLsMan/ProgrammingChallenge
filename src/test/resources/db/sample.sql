delete from stock_security_definition;
delete from option_security_definition;
delete from security_definition;

insert into security_definition (ticker, type, id) values ('XYZ', 'Stock', 1);
insert into stock_security_definition (id) values (1);

insert into security_definition (ticker, type, id) values ('XYZ-PUT', 'Option', 2);
insert into option_security_definition (id, underlying, option_type, strike_price, volatility, time_to_expiration) values (2, 'XYZ', 'Put', 50, 0.15, 2);
