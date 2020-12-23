BEGIN;

CREATE SEQUENCE public.users_sequence;

CREATE TABLE IF NOT EXISTS public.users (
	user_id bigint NOT NULL,
	department character varying(255),
	first_name character varying(255),
	last_name character varying(255),
	user_name character varying(255),
	PRIMARY KEY(user_id)
);


COMMIT;