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

COMMIT;

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.packages_jobs
	ADD CONSTRAINT fkeuokt97um3t5h864f42494wpl
	FOREIGN KEY (package_id)
	REFERENCES packages (id);

ALTER TABLE IF EXISTS ${flyway:defaultSchema}.packages_jobs
	ADD CONSTRAINT fk31fwtbqwcclei409b4x3wp2dy
	FOREIGN KEY (jobs_id)
	REFERENCES jobs (id);