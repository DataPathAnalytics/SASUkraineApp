--liquibase formatted sql

--changeset eduard.david:1 splitStatements:false runOnChange:true

INSERT INTO region(id, name)
VALUES (1, 'Автономна республіка Крим')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases   = '{АР Крим}',
    case_name = 'АР Крим'
WHERE id = 1;

INSERT INTO region(id, name)
VALUES (2, 'Вінницька область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases   = '{Вiнницька область,Вінницька область,Вінницька обл.,Винницкая область,ВІННИЦЬКА ОБЛ,ВИННИЦКАЯ ОБЛ.,Вінницька}',
    case_name = 'Вiнницькій області'
WHERE id = 2;

INSERT INTO region(id, name)
VALUES (3, 'Волинська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases   = '{Волинська обл.,Волинська область,Волынская область,ВОЛЫНСКАЯ ОБЛ.,Волинська}',
    case_name = 'Волинській області'
WHERE id = 3;

INSERT INTO region(id, name)
VALUES (4, 'Дніпропетровська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Дніпропетровська область',
    'Днiпропетровська область',
    'Дніпропетровська обл.',
    'ДНІПРОПЕТРОВСЬКА ОБЛ',
    'ДНЕПРОПЕТРОВСКАЯ ОБЛ',
    'Днепропетровская область Украина',
    'Днепропетровская обл.',
    'Днепропетровская',
    'Днепр',
    'Дніпропетровська',
    'Днепропетровская область',
    'Дніпропетровька обл.',
    'Січеславська область',
    'Січеславська обл.',
    'Січеславська',
    'СІЧЕСЛАВСЬКА ОБЛ.',
    'СІЧЕСЛАВСЬКА',
    'СЕЧЕСЛАВСКАЯ',
    'Дніпровська область',
    'Дніпро',
    'ДНЕПРОПЕТРОВСКАЯ ОБЛ.',
    'Дніпропетровська'
    ]::TEXT[],
case_name = 'Дніпропетровській області'
WHERE id = 4;

INSERT INTO region(id, name)
VALUES (5, 'Донецька область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Донецька область',
    'Донецька обл.',
    'ДОНЕЦЬКА ОБЛ.',
    'ДОНЕЦЬКА ОБЛ',
    'Донецкая область',
    'Донецька',
    'ДОНЕЦКАЯ ОБЛ.',
    'Донецька'
    ],
    case_name = 'Донецькій області'
WHERE id = 5;

INSERT INTO region(id, name)
VALUES (6, 'Житомирська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Житомирська обл.',
    'Житомирська область',
    'Житомирская область',
    'Житомирська обл',
    'Коростишівський',
    'ЖИТОМИРСЬКА ОБЛ',
    'Житомирська'
    ],
    case_name = 'Житомирській області'
WHERE id = 6;

INSERT INTO region(id, name)
VALUES (7, 'Закарпатська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Закарпатська обл.',
    'Закарпатська область',
    'Закарпатская область',
    'Закарпатська',
    'ЗАКАРПАТСКАЯ ОБЛ.',
    'Закарпатська'
    ],
    case_name = 'Закарпатській області'
WHERE id = 7;

INSERT INTO region(id, name)
VALUES (8, 'Запорізька область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Запорізька область',
    'Запорiзька область',
    'Запорізька обл.',
    'Запорізька',
    'Запорізька область Токмацький район',
    'Запорожская область',
    'Україна, Запорізька область,Чернігівський район',
    'Запорізька область',
    'Запорізька область, Великобілозерський район',
    'Запорізька область  Токмацький район',
    'Україна, Запорізька область,Чернігівський район',
    'ЗАПОРІЗЬКА ОБЛ',
    'Запорізька область  Токмацький район',
    'Великобілозерський район',
    'Запорізька'
    ],
    case_name = 'Запорізькій області'
WHERE id = 8;

INSERT INTO region(id, name)
VALUES (9, 'Івано-Франківська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Івано-Франківська область',
    'Івано-Франківська обл.',
    'Ивано-Франковская область',
    'Галицький район Івано-Франківська обл',
    'Iвано-Франкiвська область',
    'Івано-Франківська'
    ],
    case_name = 'Івано-Франківській області'
WHERE id = 9;

INSERT INTO region(id, name)
VALUES (10, 'Київська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'КИЕВСКАЯ ОБЛ.',
    'Київська область',
    'Київська обл.',
    'Киевская область',
    'Київська',
    'київська',
    'Київська обл',
    'Киевская'
    ],
    case_name = 'Київській області'
WHERE id = 10;

INSERT INTO region(id, name)
VALUES (11, 'Кіровоградська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Кiровоградська область',
    'Кіровоградська область',
    'КІРОВОГРАДСЬКА ОБЛ',
    'Кіровоградська обл.',
    'Новомиргородський район',
    'Кропивницька',
    'Кировоградская область',
    'Кіровоградська'
    ],
    case_name = 'Кіровоградській області'
WHERE id = 11;


INSERT INTO region(id, name)
VALUES (12, 'Луганська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Луганська область',
    'ЛУГАНСКАЯ ОБЛ.',
    'Луганська обл.',
    'Луганская область',
    'ЛУГАНСЬКА ОБЛ',
    'Луганська'
    ],
    case_name = 'Луганській області'
WHERE id = 12;

INSERT INTO region(id, name)
VALUES (13, 'Львівська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Львівська обл.',
    'Львівська область',
    'Львовская область',
    'Львiвська область',
    'ЛЬВОВСКАЯ ОБЛ.',
    'Львівська'
    ],
    case_name = 'Львівській області'
WHERE id = 13;

INSERT INTO region(id, name)
VALUES (14, 'Миколаївська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Миколаївська область',
    'Миколаївська обл.',
    'Николаевская область',
    'смт.Воскресенське',
    'Врадіїївський район Миколаївська область',
    'Миколаївська'
    ],
    case_name = 'Миколаївській області'
WHERE id = 14;

INSERT INTO region(id, name)
VALUES (15, 'Одеська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Одеська обл.',
    'Одеська область',
    'Одесская область',
    'Одесская обл.',
    'Одеська',
    'ОДЕССКАЯ ОБЛ.',
    'Одесская'
    ],
    case_name = 'Одеській області'
WHERE id = 15;

INSERT INTO region(id, name)
VALUES (16, 'Полтавська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Полтавська обл.',
    'Полтавська область',
    'Полтавська',
    'Полтавская',
    'Полтавская область',
    'ПОЛТАВСЬКА'
    ],
    case_name = 'Полтавській області'
WHERE id = 16;

INSERT INTO region(id, name)
VALUES (17, 'Рівненська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Рівненська обл.',
    'Рівненська область',
    'Rivnens''ka oblast',
    'Ровенская область',
    'Рiвненська область',
    'Рівненська'
    ],
    case_name = 'Рівненській області'
WHERE id = 17;

INSERT INTO region(id, name)
VALUES (18, 'Сумська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Сумська область',
    'Сумська обл.',
    'Сумська обл., Великописарівський р-н',
    'Сумская область',
    'Сумська'
    ],
    case_name = 'Сумській області'
WHERE id = 18;

INSERT INTO region(id, name)
VALUES (19, 'Тернопільська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Тернопільська обл.',
    'Тернопільська область',
    'Тернопольская область',
    'Тернопільська',
    'Тернопiльська область'
    ],
    case_name = 'Тернопільській області'
WHERE id = 19;


INSERT INTO region(id, name)
VALUES (20, 'Харківська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Харківська обл.',
    'Харківська область',
    'Харкiвська область',
    'Харківська',
    'ХАРКІВСЬКА ОБЛ.',
    'Харьковская область',
    'Харьківська обл.',
    'Харківська'
    ],
    case_name = 'Харківській області'
WHERE id = 20;

INSERT INTO region(id, name)
VALUES (21, 'Херсонська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Херсонська область',
    'Херсонская область',
    'Херсонська обл.',
    'Нижньосірогозький р-н Херсонської області',
    'Херсонская',
    'Херсонська'
    ],
    case_name = 'Херсонській області'
WHERE id = 21;


INSERT INTO region(id, name)
VALUES (22, 'Хмельницька область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Хмельницька область',
    'Хмельницька обл.',
    'ХМЕЛЬНИЦКАЯ ОБЛ.',
    'Хмельницька обл.',
    'Хмельницкая область',
    'Хмельницька'
    ],
    case_name = 'Хмельницькій області'
WHERE id = 22;

INSERT INTO region(id, name)
VALUES (23, 'Черкаська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Черкаська область',
    'Черкаська обл.',
    'Черкасская область',
    'Черкаська',
    'ЧЕРКАССКАЯ ОБЛ.'
    ],
    case_name = 'Черкаській області'
WHERE id = 23;


INSERT INTO region(id, name)
VALUES (24, 'Чернівецька область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Чернівецька область',
    'Чернівецька обл.',
    'Черновицкая область',
    'Чернiвецька область',
    'Боянчук',
    'Чернівецька'
    ],
    case_name = 'Чернівецькій області'
WHERE id = 24;

INSERT INTO region(id, name)
VALUES (25, 'Чернігівська область')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'Чернiгiвська область',
    'Чернігівська область',
    'Чернігівська обл.',
    'Чернігівська',
    'Черниговская область',
    'ЧЕРНИГОВСКАЯ ОБЛ.',
    'Чернігівська'
    ],
    case_name = 'Чернігівській області'
WHERE id = 25;

INSERT INTO region(id, name)
VALUES (26, 'м. Київ')
ON CONFLICT DO NOTHING;

UPDATE region
SET aliases = ARRAY [
    'м.Київ',
    'м. Київ',
    'місто Київ',
    'Київ',
    'КИЇВ М',
    'Киев',
    'М. КИЇВ',
    'Україна',
    'м. Київ',
    'Державна',
    'закупке',
    'Відповідно до документації',
    'Голосіївський район'
    ],
    case_name = 'м.Київ'
WHERE id = 26;
