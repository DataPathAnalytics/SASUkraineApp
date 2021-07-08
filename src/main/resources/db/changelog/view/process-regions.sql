--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true runAlways:true

DROP MATERIALIZED VIEW IF EXISTS process_regions;

CREATE MATERIALIZED VIEW process_regions AS
SELECT t.id             AS tender_id,
       t.expected_value AS tender_expected_value,
       t.has_monitoring AS has_monitoring,
       c.cpv2           AS cpv2,
       p.id             AS procuring_entity_id,
       r.id             AS procuring_entity_region_id,
       s.sasu_region_id AS sasu_region_id,
       m.id             AS monitoring_id,
       m.end_month      AS monitoring_end_month,
       m.end_date       AS monitoring_end_date
FROM tender AS t
         JOIN procuring_entity AS p ON t.procuring_entity_id = p.id
         JOIN region AS r ON p.region_id = r.id
         JOIN tender_item AS i ON i.tender_id = t.id
         JOIN cpv_catalogue AS c ON i.cpv_id = c.id
         LEFT OUTER JOIN monitoring AS m ON t.id = m.tender_id
         LEFT OUTER JOIN (
    SELECT m.id AS monitoring_id, r.id AS sasu_region_id
    FROM monitoring AS m
             JOIN sasu_office AS o ON o.id = m.sasu_office_id
             JOIN region AS r ON o.region_id = r.id
) AS s ON m.id = s.monitoring_id
