CREATE TABLE ${flyway:defaultSchema}.packages
(
    id bigint NOT NULL,
	"name" character varying(255) UNIQUE NOT NULL,
    CONSTRAINT packages_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE ${flyway:defaultSchema}.packages_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;