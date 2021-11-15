delete from stock_security_definition;
delete from option_security_definition;
delete from security_definition;

insert into security_definition (ticker, type, id) values ('AAPL', 'Stock', 1);
insert into stock_security_definition (id) values (1);

insert into security_definition (ticker, type, id) values ('TESLA', 'Stock', 2);
insert into stock_security_definition (id) values (2);

insert into security_definition (ticker, type, id) values ('AAPL-OCT-2020-110-C', 'Option', 3);
insert into option_security_definition (id, underlying, option_type, strike_price, volatility, time_to_expiration) values (3, 'AAPL', 'Call', 110, 0.15, 0.5);

insert into security_definition (ticker, type, id) values ('AAPL-OCT-2020-110-P', 'Option', 4);
insert into option_security_definition (id, underlying, option_type, strike_price, volatility, time_to_expiration) values (4, 'AAPL', 'Put',  110, 0.15, 0.5);

insert into security_definition (ticker, type, id) values ('TESLA-NOV-2020-400-C', 'Option', 5);
insert into option_security_definition (id, underlying, option_type, strike_price, volatility, time_to_expiration) values (5, 'TESLA', 'Call', 400, 0.05, 0.5);

insert into security_definition (ticker, type, id) values ('TESLA-DEC-2020-400-P', 'Option', 6);
insert into option_security_definition (id, underlying, option_type, strike_price, volatility, time_to_expiration) values (6, 'TESLA', 'Put',  400, 0.05, 1);
