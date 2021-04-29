--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true

UPDATE violation
SET name_ua = 'Порушення порядку визначення предмета закупівлі'
WHERE name = 'corruptionDescription';

UPDATE violation
SET name_ua = 'Порушення законодавства в частині неправомірного обрання та застосування процедури закупівлі'
WHERE name = 'corruptionProcurementMethodType';

UPDATE violation
SET name_ua = 'Неоприлюднення або порушення строків оприлюднення інформації про закупівлі'
WHERE name = 'corruptionPublicDisclosure';

UPDATE violation
SET name_ua = 'Тендерна документація складена не у відповідності до вимог закону'
WHERE name = 'corruptionBiddingDocuments';

UPDATE violation
SET name_ua = 'Порушення законодавства в частині складання форм документів у сфері публічних закупівель'
WHERE name = 'documentsForm';

UPDATE violation
SET name_ua = 'Не відхилення тендерних пропозицій, які підлягали відхиленню відповідно до закону'
WHERE name = 'corruptionAwarded';

UPDATE violation
SET name_ua = 'Порушення законодавства в частині не відміни замовником закупівлі'
WHERE name = 'corruptionCancelled';

UPDATE violation
SET name_ua = 'Укладення з учасником, який став переможцем процедури закупівлі, договору про закупівлю, умови якого не відповідають вимогам тендерної документації та/або тендерної пропозиції переможця процедури закупівлі'
WHERE name = 'corruptionContracting';

UPDATE violation
SET name_ua = 'Внесення змін до істотних умов договору про закупівлю у випадках, не передбачених законом'
WHERE name = 'corruptionChanges';

UPDATE violation
SET name_ua = 'Інші порушення законодавства у сфері закупівель'
WHERE name = 'other';

UPDATE violation
SET name_ua = 'Несвоєчасне надання або ненадання замовником роз''яснень щодо змісту тендерної документації'
WHERE name = 'corruptionUntimely';

UPDATE violation
SET name_ua = 'Розмір забезпечення тендерної пропозиції, встановлений у тендерній документації, перевищує межі, визначені законом'
WHERE name = 'corruptionBidSecurity';

UPDATE violation
SET name_ua = 'Ненадання інформації, документів у випадках, передбачених законом'
WHERE name = 'corruptionFailureDocuments';

UPDATE violation
SET name_ua = 'Порушення строків розгляду тендерної пропозиції'
WHERE name = 'corruptionConsideration';

UPDATE violation
SET name_ua = 'Придбання товарів, робіт і послуг до/без проведення процедур закупівель/спрощених закупівель відповідно до вимог закону'
WHERE name = 'servicesWithoutProcurementProcedure';

UPDATE violation
SET name_ua = 'Застосування конкурентного діалогу або торгів з обмеженою участю, або переговорної процедури закупівлі на умовах, не передбачених законом'
WHERE name = 'useProceduresNotByLaw';

UPDATE violation
SET name_ua = 'Відхилення тендерних пропозицій на підставах, не передбачених законом або не у відповідності до вимог закону (безпідставне відхилення)'
WHERE name = 'rejectionOfBidsNotByLaw';

UPDATE violation
SET name_ua = 'Внесення недостовірних персональних даних до електронної системи закупівель та неоновлення у разі їх зміни'
WHERE name = 'inaccuratePersonalData';

UPDATE violation
SET name_ua = 'Порушення строків оприлюднення тендерної документації'
WHERE name = 'deadlineForThePublicationDocumentation';

UPDATE violation
SET name_ua = 'Невиконання рішення Антимонопольного комітету України як органу оскарження за результатами розгляду скарг суб’єктів оскарження, подання яких передбачено законом'
WHERE name = 'notComplyDecisionACU';

UPDATE violation
SET name_ua = 'Укладення договорів, які передбачають оплату замовником товарів, робіт і послуг до/без проведення процедур закупівель/спрощених закупівель, визначених законом'
WHERE name = 'contractsWithoutProcurement';
