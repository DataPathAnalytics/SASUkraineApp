--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true

UPDATE reason
SET name_ua = 'Дані індикаторів ризиків'
WHERE name = 'indicator';

UPDATE reason
SET name_ua = 'Інформація від органів влади'
WHERE name = 'authorities';

UPDATE reason
SET name_ua = 'Повідомлення в ЗМІ'
WHERE name = 'media';

UPDATE reason
SET name_ua = 'Виявлені порушення Держфінконтролем'
WHERE name = 'fiscal';

UPDATE reason
SET name_ua = 'Інформація від НГО'
WHERE name = 'public';
