package net.dstone.common.tools.analyzer.util;

import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.UiVo;
import net.dstone.common.utils.DbUtil.LoggableStatement;
import net.dstone.common.utils.StringUtil;

public class DbGen {

	public static class DDL {
		
		public static StringBuffer MYSQL_CREATE = new StringBuffer();
		public static StringBuffer ORACLE_CREATE = new StringBuffer();

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
			MYSQL_CREATE.append("  FUNC_ID VARCHAR(100) NOT NULL COMMENT '기능ID', ").append("\n");
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
			MYSQL_CREATE.append("  FUNC_ID VARCHAR(100) NOT NULL COMMENT '기능ID', ").append("\n");
			MYSQL_CREATE.append("  CALL_FUNC_ID VARCHAR(100) NOT NULL COMMENT '호출기능ID', ").append("\n");
			MYSQL_CREATE.append("  WORKER_ID VARCHAR(10) NOT NULL COMMENT '입력자ID', ").append("\n");
			MYSQL_CREATE.append("  PRIMARY KEY (FUNC_ID, CALL_FUNC_ID) ").append("\n");
			MYSQL_CREATE.append(") COMMENT '기능간맵핑'; ").append("\n");
			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			MYSQL_CREATE.append("CREATE TABLE TB_FUNC_TBL_MAPPING ( ").append("\n");
			MYSQL_CREATE.append("  FUNC_ID VARCHAR(100) NOT NULL COMMENT '기능ID', ").append("\n");
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
			ORACLE_CREATE.append("  FUNC_ID VARCHAR2(100) NOT NULL, ").append("\n");
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
			ORACLE_CREATE.append("  FUNC_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  CALL_FUNC_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  WORKER_ID VARCHAR2(10) NOT NULL, ").append("\n");
			ORACLE_CREATE.append("  PRIMARY KEY (FUNC_ID, CALL_FUNC_ID) ").append("\n");
			ORACLE_CREATE.append("); ").append("\n");			
			ORACLE_CREATE.append("COMMENT ON TABLE TB_FUNC_FUNC_MAPPING IS '기능간맵핑' ; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_FUNC_MAPPING.FUNC_ID IS '기능ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_FUNC_MAPPING.CALL_FUNC_ID IS '호출기능ID'; ").append("\n");
			ORACLE_CREATE.append("COMMENT ON COLUMN TB_FUNC_FUNC_MAPPING.WORKER_ID IS '입력자ID'; ").append("\n");
			/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
			ORACLE_CREATE.append("CREATE TABLE TB_FUNC_TBL_MAPPING ( ").append("\n");
			ORACLE_CREATE.append("  FUNC_ID VARCHAR2(100) NOT NULL, ").append("\n");
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


		public static StringBuffer DELETE_TB_CLZZ = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC = new StringBuffer();
		public static StringBuffer DELETE_TB_TBL = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC_FUNC_MAPPING = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC_TBL_MAPPING = new StringBuffer();
		public static StringBuffer DELETE_TB_UI = new StringBuffer();
		public static StringBuffer DELETE_TB_UI_FUNC_MAPPING = new StringBuffer();
		
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

			
		}
	}
	
	public static String getDdlQuery(String DB_KIND, String JOB_KIND) {
		if( "DROP".equals(JOB_KIND) ) {
			return DDL.DROP.toString();
		}else {
			if( "MYSQL".equals(DB_KIND) ) {
				return DDL.MYSQL_CREATE.toString();
			}else if( "ORACLE".equals(DB_KIND) ) {
				return DDL.ORACLE_CREATE.toString();
			}else {
				return null;
			}
		}
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
	
	public static void insertTB_CLZZ(String DBID, ClzzVo clzzVo) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			/* <클래스-TB_CLZZ> */
			db.setQuery(QUERY.INSERT_TB_CLZZ.toString());
			
			parameterIndex = 0;
			parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getClassId());	/* 클래스ID */
			parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getPackageId());	/* 패키지ID */
			parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getClassName());	/* 클래스명 */
			parameterIndex = setParam(db.pstmt, parameterIndex, clzzVo.getClassKind().getClzzKindCd());	/* 클래스종류(CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */
			
			db.insert();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC(String DBID, MtdVo mtdVo) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			/* <기능메서드-TB_FUNC> */
			db.setQuery(QUERY.INSERT_TB_FUNC.toString());

			String classId = StringUtil.replace(mtdVo.getFunctionId(), "." + mtdVo.getMethodId(), "") ;
			
			parameterIndex = 0;
			parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getFunctionId());	/* 기능ID */
			parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getMethodId());	/* 메서드ID */
			parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getMethodName());	/* 메서드명 */
			parameterIndex = setParam(db.pstmt, parameterIndex, classId);	/* 클래스ID */
			parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getMethodUrl());	/* 메서드URL */
			
			db.insert();
			
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
		try {
			
			net.dstone.common.utils.DataSet dsTblList = net.dstone.common.utils.DbUtil.getTabs(DBID);
			net.dstone.common.utils.DataSet dsTblRow = null;

			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			for(int i=0; i<dsTblList.getDataSetRowCount("TBL_LIST") ; i++) {
				
				dsTblRow = dsTblList.getDataSet("TBL_LIST", i);
				
				/* <테이블-TB_TBL> */
				db.setQuery(QUERY.INSERT_TB_TBL.toString());
				
				parameterIndex = 0;
				parameterIndex = setParam(db.pstmt, parameterIndex, dsTblRow.getDatum("TABLE_NAME").toUpperCase());		/* 테이블ID */
				parameterIndex = setParam(db.pstmt, parameterIndex, dsTblRow.getDatum("TABLE_OWNER"));		/* 테이블오너 */
				parameterIndex = setParam(db.pstmt, parameterIndex, StringUtil.textTail(dsTblRow.getDatum("TABLE_COMMENT"), 50));	/* 테이블명 */
				
				db.insert();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC_FUNC_MAPPING(String DBID, MtdVo mtdVo) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			if( mtdVo.getCallMtdVoList() != null && mtdVo.getCallMtdVoList().size()>0 ) {
				for(String callMtdFunctionId : mtdVo.getCallMtdVoList()) {
					/* <기능간맵핑-TB_FUNC_FUNC_MAPPING> */
					db.setQuery(QUERY.INSERT_TB_FUNC_FUNC_MAPPING.toString());
					
					parameterIndex = 0;
					parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getFunctionId());	/* 기능ID */
					parameterIndex = setParam(db.pstmt, parameterIndex, callMtdFunctionId);	/* 호출기능ID */
					
					db.insert();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC_TBL_MAPPING(String DBID, MtdVo mtdVo) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
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
					
					/* <테이블맵핑-TB_FUNC_TBL_MAPPING> */
					db.setQuery(QUERY.INSERT_TB_FUNC_TBL_MAPPING.toString());
					
					parameterIndex = 0;
					parameterIndex = setParam(db.pstmt, parameterIndex, mtdVo.getFunctionId());	/* 기능ID */
					parameterIndex = setParam(db.pstmt, parameterIndex, tblId);	/* 테이블ID */
					parameterIndex = setParam(db.pstmt, parameterIndex, jobKind);	/* 작업종류(SELECT/INSERT/UPDATE/DELETE/MERGE) */
					
					db.insert();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_UI(String DBID, UiVo uiVo) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			/* <화면-TB_UI> */
			db.setQuery(QUERY.INSERT_TB_UI.toString());
			
			parameterIndex = 0;
			parameterIndex = setParam(db.pstmt, parameterIndex, uiVo.getUiId());	/* 화면ID */
			parameterIndex = setParam(db.pstmt, parameterIndex, uiVo.getUiName());	/* 화면명 */
			
			db.insert();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_UI_FUNC_MAPPING(String DBID, UiVo uiVo) throws Exception {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			if( uiVo.getLinkList() != null && uiVo.getLinkList().size()>0 ) {
				for(String link : uiVo.getLinkList()) {
					
					/* <화면링크맵핑-TB_UI_FUNC_MAPPING> */
					db.setQuery(QUERY.INSERT_TB_UI_FUNC_MAPPING.toString());
					
					parameterIndex = 0;
					parameterIndex = setParam(db.pstmt, parameterIndex, uiVo.getUiId());	/* 화면ID */
					parameterIndex = setParam(db.pstmt, parameterIndex, link);	/* 링크 */
					
					db.insert();
				}
			}
			
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
	
}
