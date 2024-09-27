# Spring Boot Starters

## YugabyteDB: YSQL via JPA

This is a basic Spring Boot project for anyone wanting to get started quickly
with YugabyteDB using YSQL via JPA (Hibernate).  If you are not interested in
using JPA, an alternate starter with JDBC is also available (TODO).

This project has minimal code and configuration but is still robust enough to
make getting started with a real production-ready project very easy.

This project was initialized with:

* Spring Boot: 3.3.4
* Java: 21
* Gradle

* -web
* -data-jpa
* -actuator
* -security
* -test
* -testcontainers
* flyway

## Additional Component Versions

* YSQL Smart Driver: 42.3.5-yb-6
* Testcontainer: yugabytedb/yugabyte:yugabyte:2024.1.2.0-b77
