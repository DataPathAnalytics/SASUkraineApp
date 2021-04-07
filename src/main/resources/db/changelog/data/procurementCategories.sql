--liquibase formatted sql

--changeset eddy:1 splitStatements:false runOnChange:true

INSERT INTO public.procurement_category (id, name_en, name_ua) VALUES (1, 'goods', 'Товари');
INSERT INTO public.procurement_category (id, name_en, name_ua) VALUES (2, 'services', 'Послуги');
INSERT INTO public.procurement_category (id, name_en, name_ua) VALUES (3, 'works', 'Роботи');
