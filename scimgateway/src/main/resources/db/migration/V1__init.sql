

CREATE TABLE ${flyway:defaultSchema}.users
(
    id bigint NOT NULL,
    mail character varying(255),
    password character varying(255),
    type integer,
    user_name character varying(255),
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT uk_k8d0f2n7n88w1a16yhua64onx UNIQUE (user_name)
);

CREATE TABLE ${flyway:defaultSchema}.scripts
(
    id bigint NOT NULL,
    content character varying(8192),
    name character varying(255),
    CONSTRAINT scripts_pkey PRIMARY KEY (id),
    CONSTRAINT uk_dqj1s6qagg1wo6232ex5sb5an UNIQUE (name)
);

CREATE TABLE ${flyway:defaultSchema}.connectionproperties
(
    id bigint NOT NULL,
	description character varying(255),
	"encrypted" boolean NOT NULL,
	"key" character varying(255),
	"type" integer,
	"value" character varying(255),
    CONSTRAINT connectionproperties_pkey PRIMARY KEY (id)
);

CREATE TABLE ${flyway:defaultSchema}.logs
(
    id bigint NOT NULL,
	message character varying(1024),
	"timestamp" timestamp without time zone,
	"type" integer,
    CONSTRAINT logs_pkey PRIMARY KEY (id)
);

CREATE TABLE ${flyway:defaultSchema}.remotesystems_attributes
(
    remote_system_id character varying(255) NOT NULL,
	attributes_id bigint NOT NULL,
    CONSTRAINT remotesystems_attributes_pkey PRIMARY KEY (remote_system_id, attributes_id),
    CONSTRAINT uk_74aw23l83u8atoo5ysldlo7ak UNIQUE (attributes_id)
);

CREATE TABLE ${flyway:defaultSchema}.attributes
(
    id bigint NOT NULL,
	description character varying(255),
	destination character varying(255),
	is_encrypted boolean NOT NULL,
	source character varying(255),
	"type" integer,
	transformation_id bigint,
    CONSTRAINT attributes_pkey PRIMARY KEY (id),
    CONSTRAINT fkhk22a0ls4yw4q3vul08v9u5yp FOREIGN KEY (transformation_id)
        REFERENCES ${flyway:defaultSchema}.scripts (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE ${flyway:defaultSchema}.remotesystems
(
    id character varying(255) NOT NULL,
	active boolean NOT NULL,
	description character varying(255),
	"name" character varying(255),
	"type" character varying(255),
	serviceuser_id bigint,
	writenameid_id bigint,
    readnameid_id bigint,
    CONSTRAINT remotesystems_pkey PRIMARY KEY (id),
    CONSTRAINT uk_l5bg36l88f58hm5ts89gkqeo6 UNIQUE (name),
    CONSTRAINT fklehbquenmis19o8m0gd2xgbpk FOREIGN KEY (serviceuser_id)
        REFERENCES ${flyway:defaultSchema}.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkmh6mgxm30ktn1iawwycvv6q1q FOREIGN KEY (writenameid_id)
        REFERENCES ${flyway:defaultSchema}.attributes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);



CREATE TABLE ${flyway:defaultSchema}.remotesystems_properties
(
    remote_system_id character varying(255) NOT NULL,
    properties_id bigint NOT NULL,
    CONSTRAINT remotesystems_properties_pkey PRIMARY KEY (remote_system_id, properties_id),
    CONSTRAINT uk_tbcnsosfayc9lq4guqkcr1pqf UNIQUE (properties_id),
    CONSTRAINT fkd5ojj5ynfb71l1iwyxrpqiux5 FOREIGN KEY (remote_system_id)
        REFERENCES ${flyway:defaultSchema}.remotesystems (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkojtuj0odp2ompvy77ahdpeen2 FOREIGN KEY (properties_id)
        REFERENCES ${flyway:defaultSchema}.connectionproperties (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE ${flyway:defaultSchema}.remotesystems_write_mappings
(
    remote_system_id character varying(255) NOT NULL,
    write_mappings_id bigint NOT NULL,
    CONSTRAINT remotesystems_write_mappings_pkey PRIMARY KEY (remote_system_id, write_mappings_id),
    CONSTRAINT uk_l20ilfsb4tc54v0swixqpb7do UNIQUE (write_mappings_id),
    CONSTRAINT fk6wj467rpitsp13t49jq0fcy3t FOREIGN KEY (remote_system_id)
        REFERENCES ${flyway:defaultSchema}.remotesystems (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkb3lkdp4np65f9pv3qq14wrjy8 FOREIGN KEY (write_mappings_id)
        REFERENCES ${flyway:defaultSchema}.attributes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE ${flyway:defaultSchema}.remotesystems_read_mappings
(
    remote_system_id character varying(255) NOT NULL,
    read_mappings_id bigint NOT NULL,
    CONSTRAINT remotesystems_read_mappings_pkey PRIMARY KEY (remote_system_id, read_mappings_id),
    CONSTRAINT uk_paerncdwscnwxa4dj1cfa4el3 UNIQUE (read_mappings_id),
    CONSTRAINT fkmp3ks4pchvu0qeb6paqpw7k33 FOREIGN KEY (remote_system_id)
        REFERENCES ${flyway:defaultSchema}.remotesystems (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkqid4jkjchao8w0v3gilxa13qm FOREIGN KEY (read_mappings_id)
        REFERENCES ${flyway:defaultSchema}.attributes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE ${flyway:defaultSchema}.attributes_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE ${flyway:defaultSchema}.connectionproperties_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE ${flyway:defaultSchema}.logs_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE ${flyway:defaultSchema}.scripts_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE ${flyway:defaultSchema}.users_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;