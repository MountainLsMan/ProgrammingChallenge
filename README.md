# Programming Challenge

Author - Mountain Man

## Table of Contents

- [Libraries](#Libraries)
- [Configuration](#Configuration)
    - [Applicaiton](#Application)
    - [Portfolio](#Portfolio)
    - [SecurityDefinition](#SecurityDefinition)
- [Tests](#Tests)
- [HowToRun](#HowToRun)
    - [Server](#Server)
    - [Client](#Client)


## Libraries

| name             | version       |
| ---------------- |:-------------:|
| openJDK          | 1.8           |
| Spring Boot      | 2.5.6         |
| H2               | 1.4.200       |
| Chronicle Queue  | 5.21.93       |
| JUnit            | 4.13.2        |

## Configuration

## Application

> Same as other spring application, the configration file exists at `/src/main/resource/application.yaml`

#### Sample application.yaml

```
spring:
  main:
    bannerMode: off
    webApplicationType: none
  datasource:
    url: jdbc:h2:mem:mydb
    username: sa
    password: password
    driverClassName: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    defer-datasource-initialization: true

client:
  portfolio:
    filePath: classpath:/config/portfolio.csv
```

## Portfolio
> a csv file contains sales position porfolio, expected with 2 columns (ticker, size) only.

> Default path `/src/main/resources/config/portfolio.csv`

#### Sample portfolio.csv

```
symbol,positionSize
AAPL,1000
AAPL-OCT-2020-110-C,-20000
AAPL-OCT-2020-110-P,20000
TESLA,-500
TESLA-NOV-2020-400-C,10000
TESLA-DEC-2020-400-P,-10000
```

## SecurityDefinition

> Security Definition is stored in a in-memory H2 database, schema and data will initial when start

> initial SQL `/src/main/resources/data.sql`

#### Schema

```
create table security_definition (
    type varchar(31) not null, 
    id integer not null, 
    ticker varchar(255), 
    primary key (id)
);

create table stock_security_definition (
    id integer not null, primary key (id)
);

create table option_security_definition (
    option_type varchar(255), 
    strike_price double not null, 
    time_to_expiration double not null,
    underlying varchar(255), 
    volatility double not null, 
    id integer not null, 
    primary key (id)
);

```

#### Sample Records
```
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

```

# Tests


| Type | Description             | category      |
| ---- | ---------------- |:-------------:|
| Repositroy Tests | To ensure database connectivity       | Integration Tests  |
| Calculator Tests          | For security and options price calculation       | Unit |
| Csv Tests               | To ensure the csv parser correct        | Unit |
| View Tests  | To ensure the print out format      | Unit |
| End to End Tests            | To make sure the component         | End 2 End tests of Cleint Side |

# HowToRun

The program contains 2 sections, it's recommended to start with IDE

### Server

There is a long runnning console server program which will publish fake market data (AAPL and TESLA) every 2sec via Chronicle Queue

`src/main/java/server/ServerMain`


### Client

Same as Server Side, there is a long running console program for subscribe the fake market data from server and render the portfolio from CSV file with SecurityDefinition.

`src/main/java/client/ClientMain`
