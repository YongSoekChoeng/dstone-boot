package net.dstone.common.tools.analyzer.util;

import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.UiVo;
import net.dstone.common.utils.DataSet;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.DbUtil.LoggableStatement;
import net.dstone.common.utils.LogUtil;
import net.dstone.common.utils.StringUtil;

public class DbGen {

	public static int FUNC_DEPTH_CNT = 10;
	
	public static class DDL {
		
		public static StringBuffer MYSQL_CREATE = new StringBuffer();
		public static StringBuffer ORACLE_CREATE = new StringBuffer();

		public static StringBuffer MYSQL_FUNCTION = new StringBuffer();
		public static StringBuffer ORACLE_FUNCTION = new StringBuffer();

		public static StringBuffer DROP = new StringBuffer();
		
		static {
			
			/* <클래스-TB_CLZZ> */
			MYSQL_CREATE.append("CREATE TABLE TB_CLZZ ( ").append("\n");
			MYSQL_CREATE.append("  CLZZ_ID VARCHAR(100) NOT NULL COMMENT '클래스ID', ").append("\n");
			MYSQL_CREATE.append("  PKG_ID VARCHAR(100) COMMENT '패키지ID', ").append("\n");
			MYSQL_CREATE.append("  CLZZ_NM VARCHAR(200) COMMENT '클래스명', ").append("\n");
			MYSQL_CREATE.append("  CLZZ_KIND VARCHAR(2) COMMENT '클래스종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지)', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (CLZZ_ID) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '클래스'; ").append("\n");
			/* <기능메서드-TB_FUNC> */
			MYSQL_CREATE.append("CREATE TABLE TB_FUNC ( ").append("\n");
			MYSQL_CREATE.append("  FUNC_ID VARCHAR(200) NOT NULL COMMENT '기능ID', ").append("\n");
			MYSQL_CREATE.append("  MTD_ID VARCHAR(100) COMMENT '메서드ID', ").append("\n");
			MYSQL_CREATE.append("  MTD_NM VARCHAR(200) COMMENT '메서드명', ").append("\n");
			MYSQL_CREATE.append("  CLZZ_ID VARCHAR(100) NOT NULL COMMENT '클래스ID', ").append("\n");
			MYSQL_CREATE.append("  MTD_URL VARCHAR(200) COMMENT '메서드URL', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (FUNC_ID) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '기능메서드'; ").append("\n");
			/* <테이블-TB_TBL> */
			MYSQL_CREATE.append("CREATE TABLE TB_TBL ( ").append("\n");
			MYSQL_CREATE.append("  TBL_ID VARCHAR(100) NOT NULL COMMENT '테이블ID', ").append("\n");
			MYSQL_CREATE.append("  TBL_OWNER VARCHAR(100) COMMENT '테이블오너', ").append("\n");
			MYSQL_CREATE.append("  TBL_NM VARCHAR(200) COMMENT '테이블명', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (TBL_ID) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '테이블'; ").append("\n");
			/* <기능간맵핑-TB_FUNC_FUNC_MAPPING> */
			MYSQL_CREATE.append("CREATE TABLE TB_FUNC_FUNC_MAPPING ( ").append("\n");
			MYSQL_CREATE.append("  FUNC_ID VARCHAR(200) NOT NULL COMMENT '기능ID', ").append("\n");
			MYSQL_CREATE.append("  CALL_FUNC_ID VARCHAR(200) NOT NULL COMMENT '호출기능ID', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (FUNC_ID, CALL_FUNC_ID) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '기능간맵핑'; ").append("\n");
			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			MYSQL_CREATE.append("CREATE TABLE TB_FUNC_TBL_MAPPING ( ").append("\n");
			MYSQL_CREATE.append("  FUNC_ID VARCHAR(200) NOT NULL COMMENT '기능ID', ").append("\n");
			MYSQL_CREATE.append("  TBL_ID VARCHAR(100) NOT NULL COMMENT '테이블ID', ").append("\n");
			MYSQL_CREATE.append("  JOB_KIND VARCHAR(10) COMMENT '작업종류', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (FUNC_ID, TBL_ID, JOB_KIND) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '테이블맵핑'; ").append("\n");
			/* <화면-TB_UI> */
			MYSQL_CREATE.append("CREATE TABLE TB_UI ( ").append("\n");
			MYSQL_CREATE.append("  UI_ID VARCHAR(100) NOT NULL COMMENT '화면ID', ").append("\n");
			MYSQL_CREATE.append("  UI_NM VARCHAR(200) COMMENT '화면명', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (UI_ID) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '화면'; ").append("\n");
			/* <화면기능맵핑-TB_UI_FUNC_MAPPING> */
			MYSQL_CREATE.append("CREATE TABLE TB_UI_FUNC_MAPPING ( ").append("\n");
			MYSQL_CREATE.append("  UI_ID VARCHAR(100) NOT NULL COMMENT '화면ID', ").append("\n");
			MYSQL_CREATE.append("  MTD_URL VARCHAR(200) NOT NULL COMMENT '메서드URL', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (UI_ID, MTD_URL) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '화면기능맵핑'; ").append("\n");
			/* <종합메트릭스-TB_METRIX> */
			MYSQL_CREATE.append("CREATE TABLE TB_METRIX ( ").append("\n");
			MYSQL_CREATE.append("  UI_ID VARCHAR(100) COMMENT '화면ID', ").append("\n");
			MYSQL_CREATE.append("  UI_NM VARCHAR(200) COMMENT '화면명', ").append("\n");
			MYSQL_CREATE.append("  BASIC_URL VARCHAR(200) COMMENT '기준URL', ").append("\n");
			for(int i=1; i<=FUNC_DEPTH_CNT; i++) {
				MYSQL_CREATE.append("  FUNCTION_ID_"+i+" VARCHAR(200) COMMENT '기능ID_"+i+"', ").append("\n");
				MYSQL_CREATE.append("  FUNCTION_NAME_"+i+" VARCHAR(200) COMMENT '기능명_"+i+"', ").append("\n");
				MYSQL_CREATE.append("  CLASS_KIND_"+i+" VARCHAR(2) COMMENT '클래스종류"+i+"(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지)', ").append("\n");
			}
			MYSQL_CREATE.append("  CALL_TBL VARCHAR(4000) COMMENT '호출테이블', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID' ").append("\n");
			MYSQL_CREATE.append(") COMMENT '종합메트릭스'; ").append("\n");

			/* <펑션-FN_FUNC_FUNC_MAPPING> */
			MYSQL_FUNCTION.append("/*******************************************************").append("\n");
			MYSQL_FUNCTION.append("펑션생성권한 허용.(root 계정으로 수행해야 함.)").append("\n");
			MYSQL_FUNCTION.append("SET GLOBAL log_bin_trust_function_creators = 1;").append("\n");
			MYSQL_FUNCTION.append("*******************************************************/").append("\n");
			MYSQL_FUNCTION.append("DROP FUNCTION IF EXISTS FN_FUNC_FUNC_MAPPING;").append("\n");
			MYSQL_FUNCTION.append("DELIMITER$$").append("\n");
			MYSQL_FUNCTION.append("CREATE FUNCTION FN_FUNC_FUNC_MAPPING(V_FUNC_ID VARCHAR(200), V_RECURSIVE_YN VARCHAR(1)) RETURNS VARCHAR(4000)").append("\n");
			MYSQL_FUNCTION.append("DETERMINISTIC").append("\n");
			MYSQL_FUNCTION.append("READS SQL DATA").append("\n");
			MYSQL_FUNCTION.append("BEGIN").append("\n");
			MYSQL_FUNCTION.append("	DECLARE RETURN_CALL_CHAIN VARCHAR(4000) DEFAULT '';").append("\n");
			MYSQL_FUNCTION.append("	DECLARE FINISHED INTEGER DEFAULT 0;").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CUR_FUNC_ID VARCHAR(200);	").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CUR_CALL_FUNC_ID VARCHAR(200);").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CALL_CHAIN_UNIT VARCHAR(200);	").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CURSOR_FUNC_FUNC_MAPPING CURSOR FOR").append("\n");
			MYSQL_FUNCTION.append("		SELECT").append("\n");
			MYSQL_FUNCTION.append("			IFNULL(A.FUNC_ID, '') FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("			, IFNULL(A.CALL_FUNC_ID, '') CALL_FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("		FROM").append("\n");
			MYSQL_FUNCTION.append("			(").append("\n");
			MYSQL_FUNCTION.append("			SELECT      ").append("\n");
			MYSQL_FUNCTION.append("				P5.FUNC_ID AS PARENT5_ID,").append("\n");
			MYSQL_FUNCTION.append("				P4.FUNC_ID AS PARENT4_ID,").append("\n");
			MYSQL_FUNCTION.append("				P3.FUNC_ID AS PARENT3_ID,").append("\n");
			MYSQL_FUNCTION.append("				P2.FUNC_ID AS PARENT2_ID,").append("\n");
			MYSQL_FUNCTION.append("				P1.FUNC_ID AS FUNC_ID,").append("\n");
			MYSQL_FUNCTION.append("				P1.CALL_FUNC_ID AS CALL_FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("			FROM        ").append("\n");
			MYSQL_FUNCTION.append("				TB_FUNC_FUNC_MAPPING P1").append("\n");
			MYSQL_FUNCTION.append("				LEFT JOIN TB_FUNC_FUNC_MAPPING P2 ON P2.CALL_FUNC_ID = P1.FUNC_ID ").append("\n");
			MYSQL_FUNCTION.append("				LEFT JOIN TB_FUNC_FUNC_MAPPING P3 ON P3.CALL_FUNC_ID = P2.FUNC_ID ").append("\n");
			MYSQL_FUNCTION.append("				LEFT JOIN TB_FUNC_FUNC_MAPPING P4 ON P4.CALL_FUNC_ID = P3.FUNC_ID  ").append("\n");
			MYSQL_FUNCTION.append("				LEFT JOIN TB_FUNC_FUNC_MAPPING P5 ON P5.CALL_FUNC_ID = P4.FUNC_ID  ").append("\n");
			MYSQL_FUNCTION.append("			WHERE 1=1").append("\n");
			MYSQL_FUNCTION.append("				AND V_FUNC_ID IN (").append("\n");
			MYSQL_FUNCTION.append("					P1.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("					P2.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("					P3.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("					P4.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("					P5.FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("				) ").append("\n");
			MYSQL_FUNCTION.append("			ORDER BY ").append("\n");
			MYSQL_FUNCTION.append("				P5.FUNC_ID, P4.FUNC_ID, P3.FUNC_ID, P2.FUNC_ID, P1.FUNC_ID, P1.CALL_FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("			) A").append("\n");
			MYSQL_FUNCTION.append("		WHERE 1=1").append("\n");
			MYSQL_FUNCTION.append("			AND FUNC_ID = IF('Y' = IFNULL(V_RECURSIVE_YN, 'Y'), FUNC_ID, V_FUNC_ID)").append("\n");
			MYSQL_FUNCTION.append("	;").append("\n");
			MYSQL_FUNCTION.append("			").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CONTINUE HANDLER FOR NOT FOUND SET FINISHED = 1;	").append("\n");
			MYSQL_FUNCTION.append("  ").append("\n");
			MYSQL_FUNCTION.append("    OPEN CURSOR_FUNC_FUNC_MAPPING;").append("\n");
			MYSQL_FUNCTION.append("		CURSOR_LOOP: LOOP").append("\n");
			MYSQL_FUNCTION.append("			FETCH CURSOR_FUNC_FUNC_MAPPING INTO CUR_FUNC_ID, CUR_CALL_FUNC_ID;").append("\n");
			MYSQL_FUNCTION.append("			IF FINISHED = 1 THEN ").append("\n");
			MYSQL_FUNCTION.append("				LEAVE CURSOR_LOOP;").append("\n");
			MYSQL_FUNCTION.append("			END IF;		").append("\n");
			MYSQL_FUNCTION.append("			SET CALL_CHAIN_UNIT = CONCAT(CUR_FUNC_ID, '->', CUR_CALL_FUNC_ID, '|');				").append("\n");
			MYSQL_FUNCTION.append("			IF INSTR(RETURN_CALL_CHAIN, CALL_CHAIN_UNIT) = 0 THEN").append("\n");
			MYSQL_FUNCTION.append("				SET RETURN_CALL_CHAIN = CONCAT(IFNULL(RETURN_CALL_CHAIN, ''), IFNULL(CALL_CHAIN_UNIT, ''));").append("\n");
			MYSQL_FUNCTION.append("			END IF;").append("\n");
			MYSQL_FUNCTION.append("		END LOOP CURSOR_LOOP;		").append("\n");
			MYSQL_FUNCTION.append("    CLOSE CURSOR_FUNC_FUNC_MAPPING;").append("\n");
			MYSQL_FUNCTION.append("    ").append("\n");
			MYSQL_FUNCTION.append("	RETURN RETURN_CALL_CHAIN;").append("\n");
			MYSQL_FUNCTION.append("END$$").append("\n");
			MYSQL_FUNCTION.append("DELIMITER ; ").append("\n");

			/* <펑션-FN_FUNC_TBL_MAPPING> */
			MYSQL_FUNCTION.append("/*******************************************************").append("\n");
			MYSQL_FUNCTION.append("펑션생성권한 허용.(root 계정으로 수행해야 함.)").append("\n");
			MYSQL_FUNCTION.append("SET GLOBAL log_bin_trust_function_creators = 1;").append("\n");
			MYSQL_FUNCTION.append("*******************************************************/").append("\n");
			MYSQL_FUNCTION.append("DROP FUNCTION IF EXISTS FN_FUNC_TBL_MAPPING;").append("\n");
			MYSQL_FUNCTION.append("DELIMITER$$").append("\n");
			MYSQL_FUNCTION.append("CREATE FUNCTION FN_FUNC_TBL_MAPPING(V_FUNC_ID VARCHAR(200), V_RECURSIVE_YN VARCHAR(1)) RETURNS VARCHAR(4000)").append("\n");
			MYSQL_FUNCTION.append("DETERMINISTIC").append("\n");
			MYSQL_FUNCTION.append("READS SQL DATA").append("\n");
			MYSQL_FUNCTION.append("BEGIN").append("\n");
			MYSQL_FUNCTION.append("	DECLARE RETURN_CALL_CHAIN VARCHAR(4000) DEFAULT '';").append("\n");
			MYSQL_FUNCTION.append("	DECLARE FINISHED INTEGER DEFAULT 0;").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CUR_FUNC_ID VARCHAR(200);	").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CUR_TBL_ID VARCHAR(100);	").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CUR_JOB_KIND VARCHAR(10);	").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CALL_CHAIN_UNIT VARCHAR(200);	").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CURSOR_FUNC_TBL_MAPPING CURSOR FOR	").append("\n");
			MYSQL_FUNCTION.append("		SELECT").append("\n");
			MYSQL_FUNCTION.append("			M.FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("			, M.TBL_ID").append("\n");
			MYSQL_FUNCTION.append("			, M.JOB_KIND").append("\n");
			MYSQL_FUNCTION.append("		FROM").append("\n");
			MYSQL_FUNCTION.append("			TB_FUNC_TBL_MAPPING M").append("\n");
			MYSQL_FUNCTION.append("			, (").append("\n");
			MYSQL_FUNCTION.append("			SELECT").append("\n");
			MYSQL_FUNCTION.append("				V_FUNC_ID FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("			UNION ALL").append("\n");
			MYSQL_FUNCTION.append("			SELECT").append("\n");
			MYSQL_FUNCTION.append("				IFNULL(A.CALL_FUNC_ID, '') FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("			FROM").append("\n");
			MYSQL_FUNCTION.append("				(").append("\n");
			MYSQL_FUNCTION.append("				SELECT      ").append("\n");
			MYSQL_FUNCTION.append("					P5.FUNC_ID AS PARENT5_ID,").append("\n");
			MYSQL_FUNCTION.append("					P4.FUNC_ID AS PARENT4_ID,").append("\n");
			MYSQL_FUNCTION.append("					P3.FUNC_ID AS PARENT3_ID,").append("\n");
			MYSQL_FUNCTION.append("					P2.FUNC_ID AS PARENT2_ID,").append("\n");
			MYSQL_FUNCTION.append("					P1.FUNC_ID AS FUNC_ID,").append("\n");
			MYSQL_FUNCTION.append("					P1.CALL_FUNC_ID AS CALL_FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("				FROM        ").append("\n");
			MYSQL_FUNCTION.append("					TB_FUNC_FUNC_MAPPING P1").append("\n");
			MYSQL_FUNCTION.append("					LEFT JOIN TB_FUNC_FUNC_MAPPING P2 ON P2.CALL_FUNC_ID = P1.FUNC_ID ").append("\n");
			MYSQL_FUNCTION.append("					LEFT JOIN TB_FUNC_FUNC_MAPPING P3 ON P3.CALL_FUNC_ID = P2.FUNC_ID ").append("\n");
			MYSQL_FUNCTION.append("					LEFT JOIN TB_FUNC_FUNC_MAPPING P4 ON P4.CALL_FUNC_ID = P3.FUNC_ID  ").append("\n");
			MYSQL_FUNCTION.append("					LEFT JOIN TB_FUNC_FUNC_MAPPING P5 ON P5.CALL_FUNC_ID = P4.FUNC_ID  ").append("\n");
			MYSQL_FUNCTION.append("				WHERE 1=1").append("\n");
			MYSQL_FUNCTION.append("					AND V_FUNC_ID IN (").append("\n");
			MYSQL_FUNCTION.append("						P1.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("						P2.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("						P3.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("						P4.FUNC_ID, ").append("\n");
			MYSQL_FUNCTION.append("						P5.FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("					) ").append("\n");
			MYSQL_FUNCTION.append("				ORDER BY ").append("\n");
			MYSQL_FUNCTION.append("					P5.FUNC_ID, P4.FUNC_ID, P3.FUNC_ID, P2.FUNC_ID, P1.FUNC_ID, P1.CALL_FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("				) A").append("\n");
			MYSQL_FUNCTION.append("			WHERE 1=1").append("\n");
			MYSQL_FUNCTION.append("				AND FUNC_ID = IF('Y' = IFNULL(V_RECURSIVE_YN, 'Y'), FUNC_ID, V_FUNC_ID)").append("\n");
			MYSQL_FUNCTION.append("			) A").append("\n");
			MYSQL_FUNCTION.append("		WHERE 1=1").append("\n");
			MYSQL_FUNCTION.append("			AND A.FUNC_ID = M.FUNC_ID").append("\n");
			MYSQL_FUNCTION.append("	;").append("\n");
			MYSQL_FUNCTION.append("			").append("\n");
			MYSQL_FUNCTION.append("	DECLARE CONTINUE HANDLER FOR NOT FOUND SET FINISHED = 1;	").append("\n");
			MYSQL_FUNCTION.append("  ").append("\n");
			MYSQL_FUNCTION.append("    OPEN CURSOR_FUNC_TBL_MAPPING;").append("\n");
			MYSQL_FUNCTION.append("		CURSOR_LOOP: LOOP").append("\n");
			MYSQL_FUNCTION.append("			FETCH CURSOR_FUNC_TBL_MAPPING INTO CUR_FUNC_ID, CUR_TBL_ID, CUR_JOB_KIND;").append("\n");
			MYSQL_FUNCTION.append("			IF FINISHED = 1 THEN ").append("\n");
			MYSQL_FUNCTION.append("				LEAVE CURSOR_LOOP;").append("\n");
			MYSQL_FUNCTION.append("			END IF;		").append("\n");
			MYSQL_FUNCTION.append("			SET CALL_CHAIN_UNIT = CONCAT(CUR_TBL_ID, ':', CUR_JOB_KIND, '|');				").append("\n");
			MYSQL_FUNCTION.append("			IF INSTR(RETURN_CALL_CHAIN, CALL_CHAIN_UNIT) = 0 THEN").append("\n");
			MYSQL_FUNCTION.append("				SET RETURN_CALL_CHAIN = CONCAT(IFNULL(RETURN_CALL_CHAIN, ''), IFNULL(CALL_CHAIN_UNIT, ''));").append("\n");
			MYSQL_FUNCTION.append("			END IF;").append("\n");
			MYSQL_FUNCTION.append("		END LOOP CURSOR_LOOP;		").append("\n");
			MYSQL_FUNCTION.append("    CLOSE CURSOR_FUNC_TBL_MAPPING;").append("\n");
			MYSQL_FUNCTION.append("    ").append("\n");
			MYSQL_FUNCTION.append("	RETURN RETURN_CALL_CHAIN;").append("\n");
			MYSQL_FUNCTION.append("END$$").append("\n");
			MYSQL_FUNCTION.append("DELIMITER ; ").append("\n");

			/* <클래스-TB_CLZZ> */
			ORACLE_CREATE.append("CREATE TABLE TB_CLZZ ( ").append("\n");
			ORACLE_CREATE.append("  CLZZ_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PKG_ID VARCHAR2(100), ").append("\n");
			ORACLE_CREATE.append("  CLZZ_NM VARCHAR2(200), ").append("\n");
			ORACLE_CREATE.append("  CLZZ_KIND VARCHAR2(2), ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (CLZZ_ID) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_CLZZ IS '클래스' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_CLZZ.CLZZ_ID IS '클래스ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_CLZZ.PKG_ID IS '패키지ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_CLZZ.CLZZ_NM IS '클래스명'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_CLZZ.CLZZ_KIND IS '클래스종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지)'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_CLZZ.WORKER_ID IS '입력자ID'; ").append("\n");
			/* <기능메서드-TB_FUNC> */
			ORACLE_CREATE.append("CREATE TABLE TB_FUNC ( ").append("\n");
			ORACLE_CREATE.append("  FUNC_ID VARCHAR2(200) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  MTD_ID VARCHAR2(100), ").append("\n");
			ORACLE_CREATE.append("  MTD_NM VARCHAR2(200), ").append("\n");
			ORACLE_CREATE.append("  CLZZ_ID VARCHAR2(100), ").append("\n");
			ORACLE_CREATE.append("  MTD_URL VARCHAR2(200), ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (FUNC_ID) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_FUNC IS '기능메서드' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC.FUNC_ID IS '기능ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC.MTD_ID IS '메서드ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC.MTD_NM IS '메서드명'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC.CLZZ_ID IS '클래스ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC.MTD_URL IS '메서드URL'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC.WORKER_ID IS '입력자ID'; ").append("\n");
			/* <테이블-TB_TBL> */
			ORACLE_CREATE.append("CREATE TABLE TB_TBL ( ").append("\n");
			ORACLE_CREATE.append("  TBL_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  TBL_OWNER VARCHAR2(100), ").append("\n");
			ORACLE_CREATE.append("  TBL_NM VARCHAR2(200), ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (TBL_ID) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_TBL IS '테이블' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_TBL.TBL_ID IS '테이블ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_TBL.TBL_OWNER IS '테이블오너'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_TBL.TBL_NM IS '테이블명'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_TBL.WORKER_ID IS '입력자ID'; ").append("\n");
			/* <기능간맵핑-TB_FUNC_FUNC_MAPPING> */
			ORACLE_CREATE.append("CREATE TABLE TB_FUNC_FUNC_MAPPING ( ").append("\n");
			ORACLE_CREATE.append("  FUNC_ID VARCHAR2(200) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  CALL_FUNC_ID VARCHAR2(200) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (FUNC_ID, CALL_FUNC_ID) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_FUNC_FUNC_MAPPING IS '기능간맵핑' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_FUNC_MAPPING.FUNC_ID IS '기능ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_FUNC_MAPPING.CALL_FUNC_ID IS '호출기능ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_FUNC_MAPPING.WORKER_ID IS '입력자ID'; ").append("\n");
			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			ORACLE_CREATE.append("CREATE TABLE TB_FUNC_TBL_MAPPING ( ").append("\n");
			ORACLE_CREATE.append("  FUNC_ID VARCHAR2(200) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  TBL_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  JOB_KIND VARCHAR2(10), ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (FUNC_ID, TBL_ID, JOB_KIND) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_FUNC_TBL_MAPPING IS '테이블맵핑' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_TBL_MAPPING.FUNC_ID IS '기능ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_TBL_MAPPING.TBL_ID IS '테이블ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_TBL_MAPPING.JOB_KIND IS '작업종류'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_TBL_MAPPING.WORKER_ID IS '입력자ID'; ").append("\n");
			/* <화면-TB_UI> */
			ORACLE_CREATE.append("CREATE TABLE TB_UI ( ").append("\n");
			ORACLE_CREATE.append("  UI_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  UI_NM VARCHAR2(200), ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (UI_ID) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_UI IS '화면' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_UI.UI_ID IS '화면ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_UI.UI_NM IS '화면명'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_UI.WORKER_ID IS '입력자ID'; ").append("\n");
			/* <화면기능맵핑-TB_UI_FUNC_MAPPING> */
			ORACLE_CREATE.append("CREATE TABLE TB_UI_FUNC_MAPPING ( ").append("\n");
			ORACLE_CREATE.append("  UI_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  MTD_URL VARCHAR2(200) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (UI_ID, MTD_URL) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_UI_FUNC_MAPPING IS '화면기능맵핑' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_UI_FUNC_MAPPING.UI_ID IS '화면ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_UI_FUNC_MAPPING.MTD_URL IS '메서드URL'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_UI_FUNC_MAPPING.WORKER_ID IS '입력자ID'; ").append("\n");			
			/* <종합메트릭스-TB_METRIX> */
			ORACLE_CREATE.append("CREATE TABLE TB_METRIX ( ").append("\n");
			ORACLE_CREATE.append("  UI_ID VARCHAR2(100), ").append("\n");
			ORACLE_CREATE.append("  UI_NM VARCHAR2(200), ").append("\n");
			ORACLE_CREATE.append("  BASIC_URL VARCHAR2(200), ").append("\n");
			for(int i=1; i<=FUNC_DEPTH_CNT; i++) {
				ORACLE_CREATE.append("  FUNCTION_ID_"+i+" VARCHAR2(200), ").append("\n");
				ORACLE_CREATE.append("  FUNCTION_NAME_"+i+" VARCHAR2(200), ").append("\n");
				ORACLE_CREATE.append("  CLASS_KIND_"+i+" VARCHAR2(2), ").append("\n");
			}
			ORACLE_CREATE.append("  CALL_TBL VARCHAR2(4000), ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_METRIX IS '종합메트릭스' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_METRIX.UI_ID IS '화면ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_METRIX.UI_NM IS '화면명'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_METRIX.BASIC_URL IS '기준URL'; ").append("\n");
			for(int i=1; i<=FUNC_DEPTH_CNT; i++) {
				ORACLE_CREATE.append("COMMENT ON COLUMN TB_METRIX.FUNCTION_ID_"+i+" IS '기능ID"+i+"'; ").append("\n");
				ORACLE_CREATE.append("COMMENT ON COLUMN TB_METRIX.FUNCTION_NAME_"+i+" IS '기능명"+i+"'; ").append("\n");
				ORACLE_CREATE.append("COMMENT ON COLUMN TB_METRIX.CLASS_KIND_"+i+" IS '클래스종류"+i+"'; ").append("\n");
			}
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_METRIX.WORKER_ID IS '입력자ID'; ").append("\n");			

			/* <펑션-FN_FUNC_FUNC_MAPPING> */
			ORACLE_FUNCTION.append("CREATE OR REPLACE FUNCTION FN_FUNC_FUNC_MAPPING(V_FUNC_ID IN VARCHAR2, V_RECURSIVE_YN IN VARCHAR2) RETURN VARCHAR2").append("\n");
			ORACLE_FUNCTION.append("IS").append("\n");
			ORACLE_FUNCTION.append("	RETURN_CALL_CHAIN VARCHAR2(4000) DEFAULT '';").append("\n");
			ORACLE_FUNCTION.append("	CALL_CHAIN_UNIT VARCHAR2(200);		").append("\n");
			ORACLE_FUNCTION.append("BEGIN").append("\n");
			ORACLE_FUNCTION.append("	FOR CURSOR_FUNC_FUNC_MAPPING IN (").append("\n");
			ORACLE_FUNCTION.append("		SELECT ").append("\n");
			ORACLE_FUNCTION.append("			A.FUNC_ID, A.CALL_FUNC_ID, A.LVL, A.RNUM").append("\n");
			ORACLE_FUNCTION.append("		FROM").append("\n");
			ORACLE_FUNCTION.append("			(").append("\n");
			ORACLE_FUNCTION.append("			SELECT ").append("\n");
			ORACLE_FUNCTION.append("				B.FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("				, B.CALL_FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("				, LEVEL LVL").append("\n");
			ORACLE_FUNCTION.append("                , ROWNUM RNUM").append("\n");
			ORACLE_FUNCTION.append("			FROM ").append("\n");
			ORACLE_FUNCTION.append("				TB_FUNC_FUNC_MAPPING B        ").append("\n");
			ORACLE_FUNCTION.append("			START WITH FUNC_ID = V_FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("			CONNECT BY PRIOR CALL_FUNC_ID = FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("			) A").append("\n");
			ORACLE_FUNCTION.append("		WHERE 1=1").append("\n");
			ORACLE_FUNCTION.append("			AND LVL = DECODE( 'Y', NVL(V_RECURSIVE_YN, 'Y'), LVL, 1 )").append("\n");
			ORACLE_FUNCTION.append("		ORDER BY RNUM").append("\n");
			ORACLE_FUNCTION.append("	) LOOP").append("\n");
			ORACLE_FUNCTION.append("		CALL_CHAIN_UNIT := CURSOR_FUNC_FUNC_MAPPING.FUNC_ID || '->' || CURSOR_FUNC_FUNC_MAPPING.CALL_FUNC_ID || '|';				").append("\n");
			ORACLE_FUNCTION.append("		IF RETURN_CALL_CHAIN IS NULL OR INSTR(RETURN_CALL_CHAIN, CALL_CHAIN_UNIT) = 0 THEN").append("\n");
			ORACLE_FUNCTION.append("			RETURN_CALL_CHAIN := RETURN_CALL_CHAIN || CALL_CHAIN_UNIT;").append("\n");
			ORACLE_FUNCTION.append("		END IF;			").append("\n");
			ORACLE_FUNCTION.append("	END LOOP;").append("\n");
			ORACLE_FUNCTION.append("	RETURN RETURN_CALL_CHAIN;").append("\n");
			ORACLE_FUNCTION.append("EXCEPTION").append("\n");
			ORACLE_FUNCTION.append("WHEN NO_DATA_FOUND THEN").append("\n");
			ORACLE_FUNCTION.append("    RETURN('');").append("\n");
			ORACLE_FUNCTION.append("END;").append("\n");
			/* <펑션-FN_FUNC_TBL_MAPPING> */
			ORACLE_FUNCTION.append("CREATE OR REPLACE FUNCTION FN_FUNC_TBL_MAPPING(V_FUNC_ID IN VARCHAR2, V_RECURSIVE_YN IN VARCHAR2) RETURN VARCHAR2").append("\n");
			ORACLE_FUNCTION.append("IS").append("\n");
			ORACLE_FUNCTION.append("	RETURN_CALL_CHAIN VARCHAR2(4000) DEFAULT '';").append("\n");
			ORACLE_FUNCTION.append("	CALL_CHAIN_UNIT VARCHAR2(200);		").append("\n");
			ORACLE_FUNCTION.append("BEGIN").append("\n");
			ORACLE_FUNCTION.append("	FOR CURSOR_FUNC_TBL_MAPPING IN (").append("\n");
			ORACLE_FUNCTION.append("		SELECT").append("\n");
			ORACLE_FUNCTION.append("			M.FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("			, M.TBL_ID").append("\n");
			ORACLE_FUNCTION.append("			, M.JOB_KIND").append("\n");
			ORACLE_FUNCTION.append("		FROM").append("\n");
			ORACLE_FUNCTION.append("			TB_FUNC_TBL_MAPPING M").append("\n");
			ORACLE_FUNCTION.append("			, (").append("\n");
			ORACLE_FUNCTION.append("			SELECT").append("\n");
			ORACLE_FUNCTION.append("				V_FUNC_ID FUNC_ID, 0 LVL, 0 RNUM").append("\n");
			ORACLE_FUNCTION.append("            FROM DUAL    ").append("\n");
			ORACLE_FUNCTION.append("			UNION ALL            ").append("\n");
			ORACLE_FUNCTION.append("			SELECT ").append("\n");
			ORACLE_FUNCTION.append("				A.CALL_FUNC_ID FUNC_ID, A.LVL, A.RNUM").append("\n");
			ORACLE_FUNCTION.append("			FROM").append("\n");
			ORACLE_FUNCTION.append("				(").append("\n");
			ORACLE_FUNCTION.append("				SELECT ").append("\n");
			ORACLE_FUNCTION.append("					B.FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("					, B.CALL_FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("					, LEVEL LVL").append("\n");
			ORACLE_FUNCTION.append("                    , ROWNUM RNUM").append("\n");
			ORACLE_FUNCTION.append("				FROM ").append("\n");
			ORACLE_FUNCTION.append("					TB_FUNC_FUNC_MAPPING B        ").append("\n");
			ORACLE_FUNCTION.append("				START WITH FUNC_ID = V_FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("				CONNECT BY PRIOR CALL_FUNC_ID = FUNC_ID").append("\n");
			ORACLE_FUNCTION.append("				) A").append("\n");
			ORACLE_FUNCTION.append("			WHERE 1=1").append("\n");
			ORACLE_FUNCTION.append("				AND LVL = DECODE( 'Y', NVL(V_RECURSIVE_YN, 'Y'), LVL, 1 )").append("\n");
			ORACLE_FUNCTION.append("			ORDER BY RNUM").append("\n");
			ORACLE_FUNCTION.append("			) A").append("\n");
			ORACLE_FUNCTION.append("		WHERE 1=1").append("\n");
			ORACLE_FUNCTION.append("			AND A.FUNC_ID = M.FUNC_ID	").append("\n");
			ORACLE_FUNCTION.append("	) LOOP").append("\n");
			ORACLE_FUNCTION.append("		CALL_CHAIN_UNIT := CURSOR_FUNC_TBL_MAPPING.TBL_ID || ':' || CURSOR_FUNC_TBL_MAPPING.JOB_KIND || '|';			").append("\n");
			ORACLE_FUNCTION.append("		IF RETURN_CALL_CHAIN IS NULL OR INSTR(RETURN_CALL_CHAIN, CALL_CHAIN_UNIT) = 0 THEN").append("\n");
			ORACLE_FUNCTION.append("			RETURN_CALL_CHAIN := RETURN_CALL_CHAIN || CALL_CHAIN_UNIT;").append("\n");
			ORACLE_FUNCTION.append("		END IF;		").append("\n");
			ORACLE_FUNCTION.append("	END LOOP;").append("\n");
			ORACLE_FUNCTION.append("	RETURN RETURN_CALL_CHAIN;").append("\n");
			ORACLE_FUNCTION.append("EXCEPTION").append("\n");
			ORACLE_FUNCTION.append("WHEN NO_DATA_FOUND THEN").append("\n");
			ORACLE_FUNCTION.append("    RETURN('');").append("\n");
			ORACLE_FUNCTION.append("END;").append("\n");
			
			/* <클래스-TB_CLZZ> */
			DROP.append("DROP TABLE TB_CLZZ;").append("\n");
			/* <기능메서드-TB_FUNC> */
			DROP.append("DROP TABLE TB_FUNC; ").append("\n");
			/* <테이블-TB_TBL> */
			DROP.append("DROP TABLE TB_TBL; ").append("\n");
			/* <기능간맵핑-TB_FUNC_FUNC_MAPPING> */
			DROP.append("DROP TABLE TB_FUNC_FUNC_MAPPING; ").append("\n");
			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			DROP.append("DROP TABLE TB_FUNC_TBL_MAPPING; ").append("\n");
			/* <화면-TB_UI> */
			DROP.append("DROP TABLE TB_UI; ").append("\n");
			/* <화면기능맵핑-TB_UI_FUNC_MAPPING> */
			DROP.append("DROP TABLE TB_UI_FUNC_MAPPING; ").append("\n");
			/* <종합메트릭스-TB_METRIX> */
			DROP.append("DROP TABLE TB_METRIX; ").append("\n");
			/* <펑션-FN_FUNC_FUNC_MAPPING> */
			DROP.append("DROP FUNCTION FN_FUNC_FUNC_MAPPING; ").append("\n");
			/* <펑션-FN_FUNC_TBL_MAPPING> */
			DROP.append("DROP FUNCTION FN_FUNC_TBL_MAPPING; ").append("\n");
			
		}
	}
	
	public static class QUERY {
		
		public static StringBuffer INSERT_TB_CLZZ = new StringBuffer();
		public static StringBuffer INSERT_TB_FUNC = new StringBuffer();
		public static StringBuffer INSERT_TB_TBL = new StringBuffer();
		public static StringBuffer INSERT_TB_FUNC_FUNC_MAPPING = new StringBuffer();
		public static StringBuffer INSERT_TB_FUNC_TBL_MAPPING = new StringBuffer();
		public static StringBuffer INSERT_TB_UI = new StringBuffer();
		public static StringBuffer INSERT_TB_UI_FUNC_MAPPING = new StringBuffer();
		public static StringBuffer INSERT_TB_METRIX = new StringBuffer();

		public static StringBuffer DELETE_TB_CLZZ = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC = new StringBuffer();
		public static StringBuffer DELETE_TB_TBL = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC_FUNC_MAPPING = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC_TBL_MAPPING = new StringBuffer();
		public static StringBuffer DELETE_TB_UI = new StringBuffer();
		public static StringBuffer DELETE_TB_UI_FUNC_MAPPING = new StringBuffer();
		public static StringBuffer DELETE_TB_METRIX = new StringBuffer();
		
		public static StringBuffer SELECT_TB_FUNC_ALL = new StringBuffer();
		
		static {
			
			/* <클래스-TB_CLZZ> */
			INSERT_TB_CLZZ.append("INSERT INTO TB_CLZZ ( ").append("\n");
			INSERT_TB_CLZZ.append("	CLZZ_ID /* 클래스ID */ ").append("\n");
			INSERT_TB_CLZZ.append("	, PKG_ID /* 패키지ID */ ").append("\n");
			INSERT_TB_CLZZ.append("	, CLZZ_NM /* 클래스명 */ ").append("\n");
			INSERT_TB_CLZZ.append("	, CLZZ_KIND /* 클래스종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */ ").append("\n");
			INSERT_TB_CLZZ.append("	, WORKER_ID /* 입력자ID */ ").append("\n");
			INSERT_TB_CLZZ.append(") VALUES ( ").append("\n");
			INSERT_TB_CLZZ.append("	? /* 클래스ID */ ").append("\n");
			INSERT_TB_CLZZ.append("	, ? /* 패키지ID */ ").append("\n");
			INSERT_TB_CLZZ.append("	, ? /* 클래스명 */ ").append("\n");
			INSERT_TB_CLZZ.append("	, ? /* 클래스종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */ ").append("\n");
			INSERT_TB_CLZZ.append("	, 'SYSTEM' /* 입력자ID */ ").append("\n");
			INSERT_TB_CLZZ.append(") ").append("\n");
			
			/* <기능메서드-TB_FUNC> */
			INSERT_TB_FUNC.append("INSERT INTO TB_FUNC ( ").append("\n");
			INSERT_TB_FUNC.append("	FUNC_ID /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, MTD_ID /* 메서드ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, MTD_NM /* 메서드명 */ ").append("\n");
			INSERT_TB_FUNC.append("	, CLZZ_ID /* 클래스ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, MTD_URL /* 메서드URL */ ").append("\n");
			INSERT_TB_FUNC.append("	, WORKER_ID /* 입력자ID */ ").append("\n");
			INSERT_TB_FUNC.append(") VALUES ( ").append("\n");
			INSERT_TB_FUNC.append("	? /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, ? /* 메서드ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, ? /* 메서드명 */ ").append("\n");
			INSERT_TB_FUNC.append("	, ? /* 클래스ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, ? /* 메서드URL */ ").append("\n");
			INSERT_TB_FUNC.append("	, 'SYSTEM' /* 입력자ID */ ").append("\n");
			INSERT_TB_FUNC.append(") ").append("\n");
			
			/* <테이블-TB_TBL> */
			INSERT_TB_TBL.append("INSERT INTO TB_TBL ( ").append("\n");
			INSERT_TB_TBL.append("	TBL_ID /* 테이블ID */ ").append("\n");
			INSERT_TB_TBL.append("	, TBL_OWNER /* 테이블오너 */ ").append("\n");
			INSERT_TB_TBL.append("	, TBL_NM /* 테이블명 */ ").append("\n");
			INSERT_TB_TBL.append("	, WORKER_ID /* 입력자ID */ ").append("\n");
			INSERT_TB_TBL.append(") VALUES ( ").append("\n");
			INSERT_TB_TBL.append("	? /* 테이블ID */ ").append("\n");
			INSERT_TB_TBL.append("	, ? /* 테이블오너 */ ").append("\n");
			INSERT_TB_TBL.append("	, ? /* 테이블명 */ ").append("\n");
			INSERT_TB_TBL.append("	, 'SYSTEM' /* 입력자ID */ ").append("\n");
			INSERT_TB_TBL.append(") ").append("\n");

			/* <기능간맵핑-TB_FUNC_FUNC_MAPPING> */
			INSERT_TB_FUNC_FUNC_MAPPING.append("INSERT INTO TB_FUNC_FUNC_MAPPING ( ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append("	FUNC_ID /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append("	, CALL_FUNC_ID /* 호출기능ID */ ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append("	, WORKER_ID /* 입력자ID */ ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append(") VALUES ( ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append("	? /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append("	, ? /* 호출기능ID */ ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append("	, 'SYSTEM' /* 입력자ID */ ").append("\n");
			INSERT_TB_FUNC_FUNC_MAPPING.append(") ").append("\n");

			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			INSERT_TB_FUNC_TBL_MAPPING.append("INSERT INTO TB_FUNC_TBL_MAPPING ( ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	FUNC_ID /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, TBL_ID /* 테이블ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, JOB_KIND /* 작업종류 */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, WORKER_ID /* 입력자ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append(") VALUES ( ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	? /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, ? /* 테이블ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, ? /* 작업종류 */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, 'SYSTEM' /* 입력자ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append(") ").append("\n");

			/* <화면-TB_UI> */
			INSERT_TB_UI.append("INSERT INTO TB_UI ( ").append("\n");
			INSERT_TB_UI.append("	UI_ID /* 화면ID */ ").append("\n");
			INSERT_TB_UI.append("	, UI_NM /* 화면명 */ ").append("\n");
			INSERT_TB_UI.append("	, WORKER_ID /* 입력자ID */ ").append("\n");
			INSERT_TB_UI.append(") VALUES ( ").append("\n");
			INSERT_TB_UI.append("	? /* 화면ID */ ").append("\n");
			INSERT_TB_UI.append("	, ? /* 화면명 */ ").append("\n");
			INSERT_TB_UI.append("	, 'SYSTEM' /* 입력자ID */ ").append("\n");
			INSERT_TB_UI.append(") ").append("\n");

			/* <화면기능맵핑-TB_UI_FUNC_MAPPING> */
			INSERT_TB_UI_FUNC_MAPPING.append("INSERT INTO TB_UI_FUNC_MAPPING ( ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append("	UI_ID /* 화면ID */ ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append("	, MTD_URL /* 메서드URL */ ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append("	, WORKER_ID /* 입력자ID */ ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append(") VALUES ( ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append("	? /* 화면ID */ ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append("	, ? /* 메서드URL */ ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append("	, 'SYSTEM' /* 입력자ID */ ").append("\n");
			INSERT_TB_UI_FUNC_MAPPING.append(") ").append("\n");

			/* <종합메트릭스-TB_METRIX> */
			INSERT_TB_METRIX.append("INSERT INTO TB_METRIX (").append("\n");
			INSERT_TB_METRIX.append("	UI_ID /* 화면ID */").append("\n");
			INSERT_TB_METRIX.append("	, UI_NM /* 화면명 */").append("\n");
			INSERT_TB_METRIX.append("	, BASIC_URL /* 기준URL */").append("\n");
			for(int i=1; i<=FUNC_DEPTH_CNT; i++) {
				INSERT_TB_METRIX.append("	, FUNCTION_ID_"+i+" /* 기능ID_"+i+" */").append("\n");
				INSERT_TB_METRIX.append("	, FUNCTION_NAME_"+i+" /* 기능명_"+i+" */").append("\n");
				INSERT_TB_METRIX.append("	, CLASS_KIND_"+i+" /* 클래스종류"+i+"(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */").append("\n");
			}
			INSERT_TB_METRIX.append("	, CALL_TBL /* 호출테이블 */").append("\n");
			INSERT_TB_METRIX.append("	, WORKER_ID /* 입력자ID */").append("\n");
			INSERT_TB_METRIX.append(") VALUES (").append("\n");
			INSERT_TB_METRIX.append("	? /* 화면ID */").append("\n");
			INSERT_TB_METRIX.append("	, ? /* 화면명 */").append("\n");
			INSERT_TB_METRIX.append("	, ? /* 기준URL */").append("\n");
			for(int i=1; i<=FUNC_DEPTH_CNT; i++) {
				INSERT_TB_METRIX.append("	, ? /* 기능ID_"+i+" */").append("\n");
				INSERT_TB_METRIX.append("	, ? /* 기능명_"+i+" */").append("\n");
				INSERT_TB_METRIX.append("	, ? /* 클래스종류"+i+"(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */").append("\n");
			}
			INSERT_TB_METRIX.append("	, ? /* 호출테이블 */").append("\n");
			INSERT_TB_METRIX.append("	, 'SYSTEM' /* 입력자ID */").append("\n");
			INSERT_TB_METRIX.append(")").append("\n");
			
			/* <클래스-TB_CLZZ> */
			DELETE_TB_CLZZ.append("DELETE FROM TB_CLZZ WHERE WORKER_ID = 'SYSTEM' ").append("\n");
			
			/* <기능메서드-TB_FUNC> */
			DELETE_TB_FUNC.append("DELETE FROM TB_FUNC WHERE WORKER_ID = 'SYSTEM' ").append("\n");
			
			/* <테이블-TB_TBL> */
			DELETE_TB_TBL.append("DELETE FROM TB_TBL WHERE WORKER_ID = 'SYSTEM' ").append("\n");

			/* <기능간맵핑-TB_FUNC_FUNC_MAPPING> */
			DELETE_TB_FUNC_FUNC_MAPPING.append("DELETE FROM TB_FUNC_FUNC_MAPPING WHERE WORKER_ID = 'SYSTEM' ").append("\n");

			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			DELETE_TB_FUNC_TBL_MAPPING.append("DELETE FROM TB_FUNC_TBL_MAPPING WHERE WORKER_ID = 'SYSTEM' ").append("\n");

			/* <화면-TB_UI> */
			DELETE_TB_UI.append("DELETE FROM TB_UI WHERE WORKER_ID = 'SYSTEM' ").append("\n");

			/* <화면기능맵핑-TB_UI_FUNC_MAPPING> */
			DELETE_TB_UI_FUNC_MAPPING.append("DELETE FROM TB_UI_FUNC_MAPPING WHERE WORKER_ID = 'SYSTEM' ").append("\n");

			/* <종합메트릭스-TB_METRIX> */
			DELETE_TB_METRIX.append("DELETE FROM TB_METRIX WHERE WORKER_ID = 'SYSTEM' ").append("\n");

			/* <SELECT-기능조회ALL> */
			SELECT_TB_FUNC_ALL.append("SELECT").append("\n");
			SELECT_TB_FUNC_ALL.append("	A.FUNC_ID /* 기능ID */").append("\n");
			SELECT_TB_FUNC_ALL.append("	, A.CLZZ_ID /* 클래스ID */").append("\n");
			SELECT_TB_FUNC_ALL.append("	, A.MTD_ID /* 메서드ID */").append("\n");
			SELECT_TB_FUNC_ALL.append("	, A.MTD_NM /* 메서드명 */").append("\n");
			SELECT_TB_FUNC_ALL.append("	, A.CLZZ_KIND /* 기능종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */").append("\n");
			SELECT_TB_FUNC_ALL.append("	, A.MTD_URL /* 메서드URL */").append("\n");
			SELECT_TB_FUNC_ALL.append("	, A.FUNC_FUNC_MAPPING").append("\n");
			SELECT_TB_FUNC_ALL.append("	, A.FUNC_TBL_MAPPING").append("\n");
			SELECT_TB_FUNC_ALL.append("FROM").append("\n");
			SELECT_TB_FUNC_ALL.append("	(").append("\n");
			SELECT_TB_FUNC_ALL.append("	SELECT ").append("\n");
			SELECT_TB_FUNC_ALL.append("		C.FUNC_ID /* 기능ID */").append("\n");
			SELECT_TB_FUNC_ALL.append("		, C.CLZZ_ID /* 클래스ID */").append("\n");
			SELECT_TB_FUNC_ALL.append("		, C.MTD_ID /* 메서드ID */").append("\n");
			SELECT_TB_FUNC_ALL.append("		, C.MTD_NM /* 메서드명 */").append("\n");
			SELECT_TB_FUNC_ALL.append("		, (SELECT CLZZ_KIND FROM TB_CLZZ A WHERE A.CLZZ_ID = C.CLZZ_ID) CLZZ_KIND /* 기능종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */").append("\n");
			SELECT_TB_FUNC_ALL.append("		, C.MTD_URL /* 메서드URL */").append("\n");
			SELECT_TB_FUNC_ALL.append("		, FN_FUNC_FUNC_MAPPING(C.FUNC_ID, ?) FUNC_FUNC_MAPPING").append("\n");
			SELECT_TB_FUNC_ALL.append("		, FN_FUNC_TBL_MAPPING(C.FUNC_ID, ?) FUNC_TBL_MAPPING").append("\n");
			SELECT_TB_FUNC_ALL.append("	FROM	 ").append("\n");
			SELECT_TB_FUNC_ALL.append("		TB_FUNC C").append("\n");
			SELECT_TB_FUNC_ALL.append("	WHERE 1=1").append("\n");
			SELECT_TB_FUNC_ALL.append("	) A").append("\n");
			SELECT_TB_FUNC_ALL.append("WHERE 1=1	").append("\n");
			SELECT_TB_FUNC_ALL.append("	AND A.FUNC_ID LIKE CONCAT( ? , '%' )	").append("\n");
			SELECT_TB_FUNC_ALL.append("	AND A.CLZZ_KIND LIKE CONCAT( ? , '%' )	").append("\n");
			SELECT_TB_FUNC_ALL.append("ORDER BY").append("\n");
			SELECT_TB_FUNC_ALL.append("	A.FUNC_ID").append("\n");
		}
	}
	
	public static String getDdlQuery(String DB_KIND, String JOB_KIND) {
		StringBuffer ddl = new StringBuffer();
		if( "DROP".equals(JOB_KIND) ) {
			ddl.append(DDL.DROP);
		}else {
			if( "MYSQL".equals(DB_KIND) ) {
				ddl.append(DDL.MYSQL_CREATE);
				ddl.append("\n");
				ddl.append(DDL.MYSQL_FUNCTION);
			}else if( "ORACLE".equals(DB_KIND) ) {
				ddl.append(DDL.ORACLE_CREATE);
				ddl.append("\n");
				ddl.append(DDL.ORACLE_FUNCTION);
			}
		}
		return ddl.toString();
	}
	
	public static String getDeleteQuery() {
		StringBuffer DELETE_ALL = new StringBuffer();
		DELETE_ALL.append(QUERY.DELETE_TB_CLZZ).append(";").append("\n");
		DELETE_ALL.append(QUERY.DELETE_TB_FUNC).append(";").append("\n");
		DELETE_ALL.append(QUERY.DELETE_TB_TBL).append(";").append("\n");
		DELETE_ALL.append(QUERY.DELETE_TB_FUNC_FUNC_MAPPING).append(";").append("\n");
		DELETE_ALL.append(QUERY.DELETE_TB_FUNC_TBL_MAPPING).append(";").append("\n");
		DELETE_ALL.append(QUERY.DELETE_TB_UI).append(";").append("\n");
		DELETE_ALL.append(QUERY.DELETE_TB_UI_FUNC_MAPPING).append(";").append("\n");
		DELETE_ALL.append("COMMIT;").append("\n");
		return DELETE_ALL.toString();
	}
	
	private static int setParam(LoggableStatement pstmt, int parameterIndex, String input ) throws Exception {
		parameterIndex = parameterIndex + 1;
		if(StringUtil.isEmpty(input)) {
			pstmt.setNull(parameterIndex, java.sql.Types.NULL);
		}else {
			pstmt.setString(parameterIndex, input);
		}
		return parameterIndex;
	}
	
	public static void insertTB_CLZZ(String DBID, String[] fileList) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		
		ClzzVo clzzVo = null;
		String subPath = AppAnalyzer.WRITE_PATH + "/class";
		int chunkSize = 500;
		
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			/* <클래스-TB_CLZZ> */
			db.setQuery(QUERY.INSERT_TB_CLZZ.toString());
			
			for(int i=0; i<fileList.length; i++) {
				String file = fileList[i];
				clzzVo = ParseUtil.readClassVo(file, subPath);

				if(StringUtil.isEmpty(clzzVo.getClassId())) {
					continue;
				}
				
				parameterIndex = 0;
				parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getClassId());	/* 클래스ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getPackageId());	/* 패키지ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getClassName());	/* 클래스명 */
				parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getClassKind().getClzzKindCd());	/* 클래스종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */
				
				db.pstmt.addBatch();
				if(i > 0 && i%chunkSize==0 ) {
					db.pstmt.executeBatch();
				}
			}
			
			db.pstmt.executeBatch();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC(String DBID, String[] fileList) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		MtdVo mtdVo = null;
		String subPath = AppAnalyzer.WRITE_PATH + "/method";
		int chunkSize = 500;
		
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			/* <기능메서드-TB_FUNC> */
			db.setQuery(QUERY.INSERT_TB_FUNC.toString());

			for(int i=0; i<fileList.length; i++) {
				String file = fileList[i];
				mtdVo = ParseUtil.readMethodVo(file, subPath);
				String classId = StringUtil.replace(mtdVo.getFunctionId(), "." + mtdVo.getMethodId(), "") ;

				if(StringUtil.isEmpty(mtdVo.getFunctionId())) {
					continue;
				}
				
				parameterIndex = 0;
				parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getFunctionId());	/* 기능ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getMethodId());	/* 메서드ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getMethodName());	/* 메서드명 */
				parameterIndex = setParam(db.pstmt, parameterIndex, classId);	/* 클래스ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getMethodUrl());	/* 메서드URL */
				
				db.pstmt.addBatch();
				if(i > 0 && i%chunkSize==0 ) {
					db.pstmt.executeBatch();
				}
			}
			db.pstmt.executeBatch();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_TBL(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		
		int chunkSize = 500;
		
		try {
			
			net.dstone.common.utils.DataSet dsTblList = net.dstone.common.utils.DbUtil.getTabs(DBID);
			net.dstone.common.utils.DataSet dsTblRow = null;

			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();

			/* <테이블-TB_TBL> */
			db.setQuery(QUERY.INSERT_TB_TBL.toString());
			
			for(int i=0; i<dsTblList.getDataSetRowCount("TBL_LIST") ; i++) {
				dsTblRow = dsTblList.getDataSet("TBL_LIST", i);

				if(StringUtil.isEmpty(dsTblRow.getDatum("TABLE_NAME"))) {
					continue;
				}
				
				parameterIndex = 0;
				parameterIndex = setParam(db.pstmt, parameterIndex, dsTblRow.getDatum("TABLE_NAME").toUpperCase());		/* 테이블ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, dsTblRow.getDatum("TABLE_OWNER"));		/* 테이블오너 */
				parameterIndex = setParam(db.pstmt, parameterIndex, StringUtil.textTail(dsTblRow.getDatum("TABLE_COMMENT"), 50));	/* 테이블명 */
				
				db.pstmt.addBatch();
				if(i > 0 && i%chunkSize==0 ) {
					db.pstmt.executeBatch();
				}
			}
			db.pstmt.executeBatch();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC_FUNC_MAPPING(String DBID, String[] fileList) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		MtdVo mtdVo = null;
		String subPath = AppAnalyzer.WRITE_PATH + "/method";		
		int chunkSize = 500;
		
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();

			/* <기능간맵핑-TB_FUNC_FUNC_MAPPING> */
			db.setQuery(QUERY.INSERT_TB_FUNC_FUNC_MAPPING.toString());
			for(int i=0; i<fileList.length; i++) {
				String file = fileList[i];	
				mtdVo = ParseUtil.readMethodVo(file, subPath);
				
				if( mtdVo.getCallMtdVoList() != null && mtdVo.getCallMtdVoList().size()>0 ) {
					for(String callMtdFunctionId : mtdVo.getCallMtdVoList()) {

						if(StringUtil.isEmpty(mtdVo.getFunctionId()) || StringUtil.isEmpty(callMtdFunctionId)) {
							continue;
						}
						
						parameterIndex = 0;
						parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getFunctionId());	/* 기능ID */
						parameterIndex = setParam(db.pstmt, parameterIndex, callMtdFunctionId);	/* 호출기능ID */
						
						db.pstmt.addBatch();
						if(i > 0 && i%chunkSize==0 ) {
							db.pstmt.executeBatch();
						}
					}
				}
			}
			db.pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC_TBL_MAPPING(String DBID, String[] fileList) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;

		MtdVo mtdVo = null;
		String subPath = AppAnalyzer.WRITE_PATH + "/method";		
		int chunkSize = 500;
		
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();

			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			db.setQuery(QUERY.INSERT_TB_FUNC_TBL_MAPPING.toString());

			for(int i=0; i<fileList.length; i++) {
				String file = fileList[i];	
				mtdVo = ParseUtil.readMethodVo(file, subPath);
				
				if( mtdVo.getCallTblVoList() != null && mtdVo.getCallTblVoList().size()>0 ) {
					String[] words = null;
					String tblId = "";
					String jobKind = "";
					for(String callTbl : mtdVo.getCallTblVoList()) {
						if(StringUtil.isEmpty(callTbl)) {
							continue;
						}
						words = StringUtil.toStrArray(callTbl, "!");
						tblId = "";
						jobKind = "";
						if(words.length > 0) {
							tblId = words[0];
							if(tblId.indexOf(".")>-1) {
								tblId = tblId.substring(tblId.indexOf(".")+1);
							}
						}
						if(words.length > 1) {
							jobKind = words[1];
						}

						if(StringUtil.isEmpty(mtdVo.getFunctionId()) || StringUtil.isEmpty(tblId)) {
							continue;
						}
						
						parameterIndex = 0;
						parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getFunctionId());	/* 기능ID */
						parameterIndex = setParam(db.pstmt, parameterIndex, tblId);	/* 테이블ID */
						parameterIndex = setParam(db.pstmt, parameterIndex, jobKind);	/* 작업종류(SELECT/INSERT/UPDATE/DELETE/MERGE) */
						
						db.pstmt.addBatch();
						if(i > 0 && i%chunkSize==0 ) {
							db.pstmt.executeBatch();
						}
					}
				}
			}
			db.pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_UI(String DBID, String[] fileList) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;

		UiVo uiVo = null;
		String subPath = AppAnalyzer.WRITE_PATH + "/ui";
		int chunkSize = 500;
		
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			/* <화면-TB_UI> */
			db.setQuery(QUERY.INSERT_TB_UI.toString());

			for(int i=0; i<fileList.length; i++) {
				String file = fileList[i];	
				uiVo = ParseUtil.readUiVo(file, subPath);

				if(StringUtil.isEmpty(uiVo.getUiId())) {
					continue;
				}
				
				parameterIndex = 0;
				parameterIndex = setParam(db.pstmt, parameterIndex, uiVo.getUiId());	/* 화면ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, uiVo.getUiName());	/* 화면명 */
				db.pstmt.addBatch();
				
				if(i > 0 && i%chunkSize==0 ) {
					db.pstmt.executeBatch();
				}
			}
			db.pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_UI_FUNC_MAPPING(String DBID, String[] fileList) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;

		UiVo uiVo = null;
		String subPath = AppAnalyzer.WRITE_PATH + "/ui";
		int chunkSize = 500;

		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();

			/* <화면링크맵핑-TB_UI_FUNC_MAPPING> */
			db.setQuery(QUERY.INSERT_TB_UI_FUNC_MAPPING.toString());

			for(int i=0; i<fileList.length; i++) {
				String file = fileList[i];	
				uiVo = ParseUtil.readUiVo(file, subPath);

				if( uiVo.getLinkList() != null && uiVo.getLinkList().size()>0 ) {
					for(String link : uiVo.getLinkList()) {

						if(StringUtil.isEmpty(uiVo.getUiId())) {
							continue;
						}
						
						parameterIndex = 0;
						parameterIndex = setParam(db.pstmt, parameterIndex, uiVo.getUiId());	/* 화면ID */
						parameterIndex = setParam(db.pstmt, parameterIndex, link);	/* 링크 */
						
						db.pstmt.addBatch();
						if(i > 0 && i%chunkSize==0 ) {
							db.pstmt.executeBatch();
						}
					}
				}
			}
			db.pstmt.executeBatch();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_METRIX(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		int chunkSize = 500;
		
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();

			/* <화면링크맵핑-TB_UI_FUNC_MAPPING> */
			db.setQuery(QUERY.INSERT_TB_METRIX.toString());
			
			String[] lines = FileUtil.readFileByLines(AppAnalyzer.WRITE_PATH + "/AppMetrix.ouput");
			if(lines != null) {
				String line = "";
				String[] cols = null;
				String col = null;
				String[] colVals = null;
				String colVal = null;
				DataSet dsRow = null;
				int lineNum = 0;
				for(int i=0; i<lines.length; i++) {
					line = lines[i];
					if(StringUtil.isEmpty(line)) {continue;}
					if(lineNum == 0) {
						cols = StringUtil.toStrArray(line, "\t");
					}else {
						colVals = StringUtil.toStrArray(line, "\t");
						dsRow = new DataSet();
						for(int k=0; k<colVals.length; k++) {
							col = cols[k];
							colVal = colVals[k];
							dsRow.setDatum(col, colVal);
						}

						parameterIndex = 0;
						parameterIndex = setParam(db.pstmt, parameterIndex, dsRow.getDatum("UI_ID"));	/* 화면ID */
						parameterIndex = setParam(db.pstmt, parameterIndex, dsRow.getDatum("UI_NM"));	/* 화면명 */
						parameterIndex = setParam(db.pstmt, parameterIndex, dsRow.getDatum("BASIC_URL"));	/* 기준URL */
						for(int k=1; k<=FUNC_DEPTH_CNT; k++) {
							parameterIndex = setParam(db.pstmt, parameterIndex, dsRow.getDatum("FUNCTION_ID_"+k, ""));	/* 기능ID */
							parameterIndex = setParam(db.pstmt, parameterIndex, dsRow.getDatum("FUNCTION_NAME_"+k, ""));	/* 기능명 */
							parameterIndex = setParam(db.pstmt, parameterIndex, dsRow.getDatum("CLASS_KIND_"+k, ""));	/* 클래스종류 */
						}
						parameterIndex = setParam(db.pstmt, parameterIndex, dsRow.getDatum("CALL_TBL"));	/* 호출테이블 */
						
						db.pstmt.addBatch();
						if(i > 0 && i%chunkSize==0 ) {
							db.pstmt.executeBatch();
						}
					}
					lineNum++;
				}
			}
			db.pstmt.executeBatch();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void deleteAll(String DBID) throws Exception {
		deleteTB_UI_FUNC_MAPPING(DBID);
		deleteTB_UI(DBID);
		deleteTB_FUNC_TBL_MAPPING(DBID);
		deleteTB_FUNC_FUNC_MAPPING(DBID);
		deleteTB_TBL(DBID);
		deleteTB_FUNC(DBID);
		deleteTB_CLZZ(DBID);
		deleteTB_METRIX(DBID);
	}
	
	private static void deleteTB_CLZZ(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_CLZZ.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	private static void deleteTB_FUNC(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_FUNC.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	private static void deleteTB_TBL(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_TBL.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}

	private static void deleteTB_FUNC_FUNC_MAPPING(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_FUNC_FUNC_MAPPING.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}

	private static void deleteTB_FUNC_TBL_MAPPING(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_FUNC_TBL_MAPPING.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}

	private static void deleteTB_UI(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_UI.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}

	private static void deleteTB_UI_FUNC_MAPPING(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_UI_FUNC_MAPPING.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	private static void deleteTB_METRIX(String DBID) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_METRIX.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	
	public static net.dstone.common.utils.DataSet selectTB_FUNC_ALL(String DBID, String FUNC_ID, String CLZZ_KIND, String FUNC_RECURSIVE_YN, String TBL_RECURSIVE_YN) throws Exception {
		net.dstone.common.utils.DataSet ds = new net.dstone.common.utils.DataSet();
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			/* <기능조회ALL> */
			db.setQuery(QUERY.SELECT_TB_FUNC_ALL.toString());
			
			parameterIndex = 0;
			parameterIndex = setParam(db.pstmt, parameterIndex, StringUtil.nullCheck(FUNC_RECURSIVE_YN, "N"));	/* 기능ID 재귀조회여부 */
			parameterIndex = setParam(db.pstmt, parameterIndex, StringUtil.nullCheck(TBL_RECURSIVE_YN, "N"));	/* 테이블ID 재귀조회여부 */
			db.pstmt.setString(++parameterIndex, StringUtil.nullCheck(FUNC_ID, ""));	/* 기능ID */
			db.pstmt.setString(++parameterIndex, StringUtil.nullCheck(CLZZ_KIND, ""));	/* 기능종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */

			ds.buildFromResultSet(db.select(), "FUNC_LIST");
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				LogUtil.sysout(db.getQuery());
				db.release();
			}
		}
		return ds;
	}
	
}
