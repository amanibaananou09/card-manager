-- Create sequence for the primary key
CREATE SEQUENCE cm_card_movement_history_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

CREATE TABLE cm_card_movement_history (
                                       id int8 NOT NULL,
                                       "version" int8 NULL,
                                       card_id int8 NULL,
                                       authorization_id int8 NULL,
                                       ctr_transaction_ref int8 NULL,
                                       old_status VARCHAR(50),
                                       new_status VARCHAR(50),
                                       date_time timestamp(6) NULL,
                                       CONSTRAINT cm_card_movement_history_pkey PRIMARY KEY (id),
                                       CONSTRAINT fk_card FOREIGN KEY (card_id) REFERENCES public.cm_card(id),
                                       CONSTRAINT fk_authorization FOREIGN KEY (authorization_id) REFERENCES public.cm_authorization(id)
);