CREATE TABLE public.cm_card_group_ceilings (
                                               card_group_id int8 NOT NULL,
                                               ceiling_id int8 NOT NULL,
                                               CONSTRAINT cm_card_group_ceilings_ceiling_id_key UNIQUE (ceiling_id),
                                               CONSTRAINT cm_card_group_ceilings_pkey PRIMARY KEY (card_group_id, ceiling_id),
                                               CONSTRAINT fk_cm_card_group_ceiling FOREIGN KEY (ceiling_id) REFERENCES public.cm_counter(id),
                                               CONSTRAINT fk_cm_card_group FOREIGN KEY (card_group_id) REFERENCES public.cm_card_group(id)
);
CREATE TABLE public.cm_card_group_bonuses (
                                              card_group_id int8 NOT NULL,
                                              bonus_id int8 NOT NULL,
                                              CONSTRAINT cm_card_group_bonus_id_key UNIQUE (bonus_id),
                                              CONSTRAINT cm_card_group_bonuses_pkey PRIMARY KEY (card_group_id, bonus_id),
                                              CONSTRAINT fk_cm_card_group_bonuses_bonus FOREIGN KEY (bonus_id) REFERENCES public.cm_counter(id),
                                              CONSTRAINT fk_cm_card_group_bonuses_card_group FOREIGN KEY (card_group_id) REFERENCES public.cm_card_group(id)
);
