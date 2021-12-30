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

CREATE TABLE ${flyway:defaultSchema}.jobs
(
    id bigint NOT NULL,
	"name" character varying(255) NOT NULL,
    "description" character varying(8096),
    "enabled" boolean,

    CONSTRAINT jobs_pkey PRIMARY KEY (id)
);

CREATE SEQUENCE ${flyway:defaultSchema}.jobs_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.packages_jobs (
	package_id bigint NOT NULL,
	jobs_id bigint NOT NULL,
	PRIMARY KEY(jobs_id)
);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.packages_jobs
	ADD CONSTRAINT fkeuokt97um3t5h864f42494wpl
	FOREIGN KEY (package_id)
	REFERENCES ${flyway:defaultSchema}.packages (id);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.packages_jobs
	ADD CONSTRAINT fk31fwtbqwcclei409b4x3wp2dy
	FOREIGN KEY (jobs_id)
	REFERENCES ${flyway:defaultSchema}.jobs (id);

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.passes (
	id bigint NOT NULL,
	clear_table boolean NOT NULL,
	description character varying(255),
	name character varying(255),
	rank bigint NOT NULL,
	table_name character varying(255),
	"type" integer,
	system_id character varying(255),
	PRIMARY KEY(id)
);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.passes
	ADD CONSTRAINT fk5cv2ucg3j3b2lxlnoqba8dafq
	FOREIGN KEY (system_id)
	REFERENCES remotesystems (id);

CREATE SEQUENCE ${flyway:defaultSchema}.passes_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.jobs_passes (
	job_id bigint NOT NULL,
	passes_id bigint NOT NULL,
	PRIMARY KEY(passes_id)
);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.jobs_passes
	ADD CONSTRAINT fkib3qj1wb1e925aly8wutrm7ti
	FOREIGN KEY (job_id)
	REFERENCES ${flyway:defaultSchema}.jobs (id);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.jobs_passes
	ADD CONSTRAINT fkpo8drmhh38g5r8sud1kg4y2x
	FOREIGN KEY (passes_id)
	REFERENCES ${flyway:defaultSchema}.passes (id);

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.passmappings (
	id bigint NOT NULL,
	destination character varying(255),
	source character varying(255),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.passproperties (
	id bigint NOT NULL,
	description character varying(255) ,
	"key" character varying(255)  NOT NULL,
	"value" character varying(255),
	PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.passes_mappings (
	pass_id bigint NOT NULL,
	mappings_id bigint NOT NULL,
	PRIMARY KEY(mappings_id)
);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.passes_mappings
	ADD CONSTRAINT fklx7itletmwjr0oxfckuq95k8a
	FOREIGN KEY (mappings_id)
	REFERENCES ${flyway:defaultSchema}.passmappings (id);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.passes_mappings
	ADD CONSTRAINT fkbyr7hqr5tk164dglc9rc2888h
	FOREIGN KEY (pass_id)
	REFERENCES ${flyway:defaultSchema}.passes (id);

CREATE TABLE IF NOT EXISTS ${flyway:defaultSchema}.passes_properties (
	pass_id bigint NOT NULL,
	properties_id bigint NOT NULL,
	PRIMARY KEY(properties_id)
);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.passes_properties
	ADD CONSTRAINT fkeuga13gurw5p8ik4lvtodx8ib
	FOREIGN KEY (pass_id)
	REFERENCES ${flyway:defaultSchema}.passes (id);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.passes_properties
	ADD CONSTRAINT fk6w35ws54fl11tpheny43hfmy7
	FOREIGN KEY (properties_id)
	REFERENCES ${flyway:defaultSchema}.passproperties (id);

CREATE SEQUENCE ${flyway:defaultSchema}.passmapping_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE SEQUENCE ${flyway:defaultSchema}.passproperty_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;