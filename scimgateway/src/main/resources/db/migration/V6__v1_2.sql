CREATE TABLE ${flyway:defaultSchema}.entrytypemappings
(
    id bigint NOT NULL,
	"name" character varying(255),
    CONSTRAINT entrytypemappings_pkey PRIMARY KEY (id)
);


CREATE SEQUENCE ${flyway:defaultSchema}.entrytypemappings_sequence
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;

CREATE TABLE ${flyway:defaultSchema}.entrytypemappings_read_mappings
(
    entry_type_mapping_id bigint NOT NULL,
    read_mappings_id bigint NOT NULL,
    CONSTRAINT entrytypemappings_read_mappings_pkey PRIMARY KEY (entry_type_mapping_id, read_mappings_id),
    CONSTRAINT uk_j0uh2xyvk1tjrb21mi6r3obcr UNIQUE (read_mappings_id),
    CONSTRAINT fk2jckrhh1ej5aeheyrr0oghoe9 FOREIGN KEY (read_mappings_id)
        REFERENCES ${flyway:defaultSchema}.attributes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkg35sv1itvmyuyuunyiu46iq6d FOREIGN KEY (entry_type_mapping_id)
        REFERENCES ${flyway:defaultSchema}.entrytypemappings (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE ${flyway:defaultSchema}.entrytypemappings_write_mappings
(
    entry_type_mapping_id bigint NOT NULL,
    write_mappings_id bigint NOT NULL,
    CONSTRAINT entrytypemappings_write_mappings_pkey PRIMARY KEY (entry_type_mapping_id, write_mappings_id),
    CONSTRAINT uk_4punx7uf99uw0k6mwqd2kqmnr UNIQUE (write_mappings_id),
    CONSTRAINT fkjwniu060oqmygbk3gchcwbn9b FOREIGN KEY (write_mappings_id)
        REFERENCES ${flyway:defaultSchema}.attributes (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkt729jc2ih7fanwlaiuavbq88b FOREIGN KEY (entry_type_mapping_id)
        REFERENCES ${flyway:defaultSchema}.entrytypemappings (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE ${flyway:defaultSchema}.remotesystems_entry_type_mappings
(
    remote_system_id character varying(255) NOT NULL,
    entry_type_mappings_id bigint NOT NULL,
    CONSTRAINT remotesystems_entry_type_mappings_pkey PRIMARY KEY (remote_system_id, entry_type_mappings_id),
    CONSTRAINT uk_1wwc63fltxhyu1o2v2kc1nr45 UNIQUE (entry_type_mappings_id),
    CONSTRAINT fk91qvmi7wsgy5t7uxhvsb03alb FOREIGN KEY (entry_type_mappings_id)
        REFERENCES ${flyway:defaultSchema}.entrytypemappings (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkncei5c42ydat1towf16110dnb FOREIGN KEY (remote_system_id)
        REFERENCES ${flyway:defaultSchema}.remotesystems (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

do
$$
declare 
  cur_remotesystems  cursor for 
		select * 
		from ${flyway:defaultSchema}.remotesystems;
  rec_remotesystems   record;
  seq_entrytypemapping bigint;
begin
  open cur_remotesystems;
  loop
  	fetch cur_remotesystems into rec_remotesystems;

 	exit when not found;
	seq_entrytypemapping = NEXTVAL('${flyway:defaultSchema}.entrytypemappings_sequence');
	INSERT INTO ${flyway:defaultSchema}.entrytypemappings (id, name) VALUES (seq_entrytypemapping, 'User');
	INSERT INTO ${flyway:defaultSchema}.remotesystems_entry_type_mappings (remote_system_id, entry_type_mappings_id) VALUES (rec_remotesystems.id, seq_entrytypemapping);
	
	INSERT INTO ${flyway:defaultSchema}.entrytypemappings_read_mappings (entry_type_mapping_id, read_mappings_id) SELECT seq_entrytypemapping, read_mappings_id from ${flyway:defaultSchema}.remotesystems_read_mappings where remote_system_id = rec_remotesystems.id;
	INSERT INTO ${flyway:defaultSchema}.entrytypemappings_write_mappings (entry_type_mapping_id, write_mappings_id) SELECT seq_entrytypemapping, write_mappings_id from ${flyway:defaultSchema}.remotesystems_write_mappings where remote_system_id = rec_remotesystems.id;
	
   end loop;
   close cur_remotesystems;
end;
$$;;

DROP TABLE ${flyway:defaultSchema}.remotesystems_read_mappings;

DROP TABLE ${flyway:defaultSchema}.remotesystems_write_mappings;