# Spring Boot Kick-Starter for YugabyteDB

## YSQL using JPA

This is a basic Spring Boot project for anyone wanting to get started quickly
with YugabyteDB using YSQL with JPA (Hibernate).

If you are not interested in using JPA, an alternate starter with JDBC is also
available (TODO).

This project has minimal code and configuration but is still robust enough to
make getting started with a real production-ready project very easy.

This project was initialized with:

* Spring Boot: 3.3.4
* Java: 21
* Gradle: 8.10.2

* -web
* -data-jpa
* -actuator
* -security
* -test
* -testcontainers
* flyway

## Additional Component Versions

* YSQL Smart Driver: 42.7.3-yb-1
  * See https://docs.yugabyte.com/preview/drivers-orms/java/yugabyte-jdbc/
* Testcontainers: 1.20.2 (yugabytedb/yugabyte:yugabyte:2024.1.3.0-b105)
