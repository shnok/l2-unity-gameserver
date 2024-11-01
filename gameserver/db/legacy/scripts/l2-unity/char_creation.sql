SET @CharName = LEFT(CAST(RANDOM_UUID() AS VARCHAR(36)), 8);

-- 0x1f: dark_fighter
-- 0x26: dark_mage
-- 0x35: dwarven_fighter
SET @Class_ID = 0x1f;

-- 0: male
-- 1: female
SET @Sex = 1;

SET @Face = 0;
SET @HairStyle = 0;
SET @HairColor = 0;

-- human fighter spawn
--SET @X = 4919.45;
--SET @Y = -59.12;
--SET @Z = -1358.82;

-- human mage spawn
SET @X = 4724.32;
SET @Y = -68.00;
SET @Z = -1731.24;

INSERT INTO PUBLIC."CHARACTER"
(ACCOUNT_NAME, CHAR_NAME, CLASS_ID, RACE, ACC, CRITICAL, EVASION, M_ATK, M_DEF, M_SPD, P_ATK, P_DEF, P_SPD, RUN_SPD, STR, CON, DEX, "_INT", MEN, WIT, COLR, COLH, 
HP, MAX_HP, CP, MAX_CP, MP, MAX_MP, FACE, HAIR_STYLE, HAIR_COLOR, SEX, X, Y, Z)
SELECT
    @CharName,
    @CharName,
    @Class_ID,
    RACEID,
    ACC,
    CRITICAL,
    EVASION,
    M_ATK,
    M_DEF,
    M_SPD,
    P_ATK,
    P_DEF,
    P_SPD,
    MOVE_SPD,
    STR,
    CON,
    DEX,
    "_INT",
    MEN,
    WIT,
    F_COL_R,
    F_COL_H,
    lv.DEFAULTHPBASE + lv.DEFAULTHPADD,
    lv.DEFAULTHPBASE + lv.DEFAULTHPADD,
    lv.DEFAULTCPBASE + lv.DEFAULTCPADD,
    lv.DEFAULTCPBASE + lv.DEFAULTCPADD,
    lv.DEFAULTMPBASE + lv.DEFAULTMPADD,
    lv.DEFAULTMPBASE + lv.DEFAULTMPADD,
    @Face,
    @HairStyle,
    @HairColor,
    @Sex,
    @X,
    @Y,
    @Z
FROM CHAR_TEMPLATE crt
INNER JOIN LVLUPGAIN lv ON lv.CLASSID = @Class_ID
WHERE crt.CLASSID = @Class_ID;

SET @InsertedID = LAST_INSERT_ID();

--items1: UpperBody
--items2: Legs
--items3: rhand

-- slots
-- 0:Head
-- 1:UpperBody
-- 2:Legs
-- 3:Gloves
-- 4:Boots
-- 5:Lefhand
-- 6:RightHand
-- 7:LRing
-- 8:RRing
-- 9:LEar
-- 10:REar
-- 11:Neck
INSERT INTO PUBLIC.ITEM (OWNER_ID, ITEM_ID, COUNT, LOC, SLOT)
SELECT @InsertedID,ITEMS1,1,1,1 FROM CHAR_TEMPLATE WHERE CLASSID = @Class_ID;
INSERT INTO PUBLIC.ITEM (OWNER_ID, ITEM_ID, COUNT, LOC, SLOT)
SELECT @InsertedID,ITEMS2,1,1,2 FROM CHAR_TEMPLATE WHERE CLASSID = @Class_ID;
INSERT INTO PUBLIC.ITEM (OWNER_ID, ITEM_ID, COUNT, LOC, SLOT)
SELECT @InsertedID,ITEMS3,1,1,6 FROM CHAR_TEMPLATE WHERE CLASSID = @Class_ID;
