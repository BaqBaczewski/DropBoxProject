--liquibase formatted sql

--changeset philip.frank:1602251197859-1
CREATE TABLE user (id VARCHAR(255) NOT NULL, encoded_password VARCHAR(255), name VARCHAR(255), role INT, CONSTRAINT userPK PRIMARY KEY (id));

--changeset philip.frank:1602251197859-2
ALTER TABLE user ADD CONSTRAINT UKgj2fy3dcix7ph7k8684gka40c UNIQUE (name);

