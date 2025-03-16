DROP DATABASE IF EXISTS videosystemdb;
CREATE DATABASE videosystemdb;

USE videosystemdb;



DROP TABLE IF EXISTS usr;
CREATE TABLE usr (
	uID varchar(20) NOT NULL,
	uPW varchar(20) NOT NULL,
	uSSN char(13) NOT NULL,
	/*grade는 1이면 광고 완전 제거, 0이면 광고 시청*/
	grade tinyint(1) NOT NULL, 
    /*interests는 관심분야. 번호로 지정 
    0 - comedy 1 - toy 2 - gaming 3 - educational 4 - music 5 - eating 6 - beauty 7 - sports */
    interests int(1) NOT NULL,
    PRIMARY KEY(uID)
);

LOCK TABLES usr WRITE;
INSERT INTO usr VALUES ("DUMMY", "DUMMY", 0, 0, 0);
UNLOCK TABLES;


DROP TABLE IF EXISTS playlist;
CREATE TABLE playlist (
	pName varchar(20) NOT NULL,
    pl_uID varchar(20) NOT NULL,
    plCategory int(1) NOT NULL,
    PRIMARY KEY(pName, pl_uID),
    CONSTRAINT playlist_FK
		FOREIGN KEY(pl_uID) REFERENCES usr (uID) ON DELETE CASCADE ON UPDATE CASCADE
);

LOCK TABLES playlist WRITE;
UNLOCK TABLES;

DROP TABLE IF EXISTS manager;
CREATE TABLE manager (
    mID varchar(20) NOT NULL,
	mPW varchar(20) NOT NULL,
    mSSN char(13) NOT NULL,
    PRIMARY KEY(mID)
);

LOCK TABLES manager WRITE;
INSERT INTO manager VALUES ("root", "root", "0000000000000");
UNLOCK TABLES;

DROP TABLE IF EXISTS ad;
CREATE TABLE ad (
	adName varchar(20) NOT NULL,
    adID varchar(20) NOT NULL,
	adCategory int(1) NOT NULL,
    adView double DEFAULT 0,
    profitPerView int NOT NULL,
    ad_mID varchar(20) DEFAULT NULL,
    PRIMARY KEY(adID),
	CONSTRAINT ad_FK_m
		FOREIGN KEY(ad_mID) REFERENCES manager(mID) ON DELETE CASCADE ON UPDATE CASCADE
);

LOCK TABLES ad WRITE;
INSERT INTO ad VALUES ("HYU0", "HYUHYUHYU0", 0, 0, 0, "root"), ("HYU1", "HYUHYUHYU1", 1, 0, 0, "root"), ("HYU2", "HYUHYUHYU2", 2, 0, 0, "root"), ("HYU3", "HYUHYUHYU3", 3, 0, 0, "root"), ("HYU4", "HYUHYUHYU4", 4, 0, 0, "root"), ("HYU5", "HYUHYUHYU5", 5, 0, 0, "root"),("HYU6", "HYUHYUHYU6", 6, 0, 0, "root"), ("HYU7", "HYUHYUHYU7", 7, 0, 0, "root");
UNLOCK TABLES;


DROP TABLE IF EXISTS video;
CREATE TABLE video (
	vID varchar(20) NOT NULL,
	vName varchar(20) NOT NULL,
    views int DEFAULT 0,
    vLength int NOT NULL,
    /*video category*/
    vCategory int(1) NOT NULL,
    /*video 시청 등급 : 연령제한 ,영화 연령제한 에서 따옴*/
    ageLimit tinyint(1) NOT NULL,
    upldDate timestamp NOT NULL,
    viewPerTime double DEFAULT 0,
    v_mID varchar(20) DEFAULT NULL,
    v_uID varchar(20) NOT NULL,
    v_adID varchar(20) NOT NULL,
	PRIMARY KEY(vID),
    CONSTRAINT video_FK_m
		FOREIGN KEY(v_mID) REFERENCES manager(mID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT video_FK_u
		FOREIGN KEY(v_uID) REFERENCES usr(uID) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT video_FK_ad
		FOREIGN KEY(v_adID) REFERENCES ad(adID) ON DELETE CASCADE ON UPDATE CASCADE  
);

LOCK TABLES video WRITE;
INSERT INTO video VALUES ("HYUVIDEOHYU0", "HYUfunnyMoments", 0, 30, 0, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU0" ), ("HYUVIDEOHYU1", "HYUtoyMoments", 0, 30, 1, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU1" ), ("HYUVIDEOHYU2", "HYUgamingMoment", 0, 30, 2, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU2" ), ("HYUVIDEOHYU3", "HYUeduMoments", 0, 30, 3, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU3" ),("HYUVIDEOHYU4", "HYUmusicMoments", 0, 30, 4, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU4" ), ("HYUVIDEOHYU5", "HYUeatMoments", 0, 30, 5, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU5" ), ("HYUVIDEOHYU6", "HYUbeautyMoment", 0, 30, 6, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU6" ), ("HYUVIDEOHYU7", "HYUsportsMoments", 0, 30, 7, 0, "2000-01-01", 0, null, "DUMMY", "HYUHYUHYU7" );
UNLOCK TABLES;






DROP TABLE IF EXISTS watchV;
CREATE TABLE watchV (
	wv_uID varchar(20) NOT NULL,
    wv_vID varchar(20) NOT NULL,
    wDate timestamp NOT NULL,
    PRIMARY KEY(wv_uID, wv_vID, wDate),
	CONSTRAINT watchV_FK_u
		FOREIGN KEY(wv_uID) REFERENCES usr (uID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT watchV_FK_v
		FOREIGN KEY(wv_vID) REFERENCES video (vID) ON DELETE CASCADE ON UPDATE CASCADE
);

LOCK TABLES watchV WRITE;
UNLOCK TABLES;

DROP TABLE IF EXISTS blockU;
CREATE TABLE blockU(
	/*reason은 객관식 선택지이다
    0. 성적인 사유 
    1. 폭력 사유 
    2. 혐오 조장 사유
    3. 저작권 침해 사유이다
    */
	bu_reason int(1) NOT NULL,
    bu_mID varchar(20) NOT NULL,
    bu_uID varchar(20) NOT NULL,
    PRIMARY KEY(bu_uID, bu_mID),
	CONSTRAINT blockU_FK_m
		FOREIGN KEY(bu_mID) REFERENCES manager(mID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT blockU_FK_u
		FOREIGN KEY(bu_uID) REFERENCES usr(uID) ON DELETE CASCADE ON UPDATE CASCADE
);    

LOCK TABLES blockU WRITE;
UNLOCK TABLES;


DROP TABLE IF EXISTS blockV;
CREATE TABLE blockV(
	/*reason은 객관식 선택지이다
    0. 성적인 사유 
    1. 폭력 사유 
    2. 혐오 조장 사유
    3. 저작권 침해 사유이다
    */
	bv_reason int(1) NOT NULL,
    bv_mID varchar(20) NOT NULL,
    bv_vID varchar(20) NOT NULL,
    PRIMARY KEY(bv_vID, bv_mID),
	CONSTRAINT blockV_FK_m
		FOREIGN KEY(bv_mID) REFERENCES manager(mID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT blockV_FK_u
		FOREIGN KEY(bv_vID) REFERENCES video(vID) ON DELETE CASCADE ON UPDATE CASCADE
);   

LOCK TABLES blockV WRITE;
UNLOCK TABLES;

DROP TABLE IF EXISTS registerPl;
CREATE TABLE registerPl (
	reg_pName varchar(20) NOT NULL,
	reg_pl_uID varchar(20) NOT NULL,
    reg_vID varchar(20) NOT NULL,
    PRIMARY KEY(reg_pName, reg_pl_uID, reg_vID),
	CONSTRAINT registerPl_FK_pl_name
		FOREIGN KEY(reg_pName) REFERENCES playlist(pName) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT registerPl_FK_pl_uID
		FOREIGN KEY(reg_pl_uID) REFERENCES playlist (pl_uID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT registerPl_FK_v
		FOREIGN KEY(reg_vID) REFERENCES video(vID) ON DELETE CASCADE ON UPDATE CASCADE
);   

LOCK TABLES registerPl WRITE;
UNLOCK TABLES;

DROP TABLE IF EXISTS report;
CREATE TABLE report (
	rpt_uID varchar(20) NOT NULL,
	rpt_vID varchar(20) NOT NULL,
    reason int(1) NOT NULL,
    PRIMARY KEY(rpt_uID, rpt_vID),
	CONSTRAINT report_FK_u
		FOREIGN KEY(rpt_uID) REFERENCES usr(uID) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT report_FK_v
		FOREIGN KEY(rpt_vID) REFERENCES video (vID) ON DELETE CASCADE ON UPDATE CASCADE
);

LOCK TABLES report WRITE;
UNLOCK TABLES;
