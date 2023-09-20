package net.dstone.common.tools.analyzer.util;

import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;

public class DbGen {

	public static class DDL {
		
		public static StringBuffer MYSQL = new StringBuffer();
		public static StringBuffer ORACLE = new StringBuffer();
		
		static {
			MYSQL.append("CREATE TABLE TB_FUNC ( ").append("\n");
			MYSQL.append("  FUNC_ID VARCHAR(100) NOT NULL COMMENT '기능ID', ").append("\n");
			MYSQL.append("  FUNC_NM VARCHAR(100) NOT NULL COMMENT '기능명', ").append("\n");
			MYSQL.append("  FUNC_KIND VARCHAR(2) COMMENT '기능종류(U:UI/C:컨트롤러/S:서비스/D:DAO)', ").append("\n");
			MYSQL.append("  PKG VARCHAR(100) NOT NULL COMMENT '패키지', ").append("\n");
			MYSQL.append("  CLZZ VARCHAR(100) NOT NULL COMMENT '클래스', ").append("\n");
			MYSQL.append("  CALL_URL VARCHAR(200) COMMENT '호출URL' ").append("\n");
			MYSQL.append(") COMMENT '기능'; ").append("\n");
			MYSQL.append("CREATE TABLE TB_FUNC_MAPPING ( ").append("\n");
			MYSQL.append("  FUNC_ID VARCHAR(100) NOT NULL COMMENT '기능ID', ").append("\n");
			MYSQL.append("  CALL_FUNC_ID VARCHAR(100) NOT NULL COMMENT '호출기능ID' ").append("\n");
			MYSQL.append(") COMMENT '기능맵핑'; ").append("\n");
			MYSQL.append("CREATE TABLE TB_FUNC_TBL_MAPPING ( ").append("\n");
			MYSQL.append("  FUNC_ID VARCHAR(100) NOT NULL COMMENT '기능ID', ").append("\n");
			MYSQL.append("  TBL_ID VARCHAR(100) NOT NULL COMMENT '테이블ID', ").append("\n");
			MYSQL.append("  TBL_NM VARCHAR(200) NOT NULL COMMENT '테이블명' ").append("\n");
			MYSQL.append(") COMMENT '테이블맵핑'; ").append("\n");
			
			ORACLE.append("CREATE TABLE TB_FUNC ( ").append("\n");
			ORACLE.append("  FUNC_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE.append("  FUNC_NM VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE.append("  FUNC_KIND VARCHAR2(2), ").append("\n");
			ORACLE.append("  PKG VARCHAR(100) NOT NULL, ").append("\n");
			ORACLE.append("  CLZZ VARCHAR(100) NOT NULL, ").append("\n");
			ORACLE.append("  CALL_URL VARCHAR2(200) ").append("\n");
			ORACLE.append("); ").append("\n");
			ORACLE.append("COMMENT ON TABLE TB_FUNC IS '기능' ; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC.FUNC_ID IS '기능ID'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC.FUNC_NM IS '기능명'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC.FUNC_KIND IS '기능종류(U:UI/C:컨트롤러/S:서비스/D:DAO)'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC.PKG IS '패키지'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC.CLZZ IS '클래스'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC.CALL_URL IS '호출URL'; ").append("\n");
			ORACLE.append("CREATE TABLE TB_FUNC_MAPPING ( ").append("\n");
			ORACLE.append("  FUNC_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE.append("  CALL_FUNC_ID VARCHAR2(100) NOT NULL ").append("\n");
			ORACLE.append("); ").append("\n");
			ORACLE.append("COMMENT ON TABLE TB_FUNC_MAPPING IS '기능맵핑' ; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC_MAPPING.FUNC_ID IS '기능ID'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC_MAPPING.CALL_FUNC_ID IS '호출기능ID'; ").append("\n");
			ORACLE.append("CREATE TABLE TB_FUNC_TBL_MAPPING ( ").append("\n");
			ORACLE.append("  FUNC_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE.append("  TBL_ID VARCHAR2(100) NOT NULL, ").append("\n");
			ORACLE.append("  TBL_NM VARCHAR2(200) NOT NULL ").append("\n");
			ORACLE.append("); ").append("\n");
			ORACLE.append("COMMENT ON TABLE TB_FUNC_TBL_MAPPING IS '테이블맵핑' ; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC_TBL_MAPPING.FUNC_ID IS '기능ID'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC_TBL_MAPPING.TBL_ID IS '테이블ID'; ").append("\n");
			ORACLE.append("COMMENT ON COLUMN TB_FUNC_TBL_MAPPING.TBL_NM IS '테이블명'; ").append("\n");
		}
	}
	
	public static class QUERY {
		public static StringBuffer INSERT_TB_FUNC = new StringBuffer();
		public static StringBuffer INSERT_TB_FUNC_MAPPING = new StringBuffer();
		public static StringBuffer INSERT_TB_FUNC_TBL_MAPPING = new StringBuffer();

		public static StringBuffer DELETE_TB_FUNC = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC_MAPPING = new StringBuffer();
		public static StringBuffer DELETE_TB_FUNC_TBL_MAPPING = new StringBuffer();
		
		static {
			/* INSERT_TB_FUNC */
			INSERT_TB_FUNC.append("INSERT INTO TB_FUNC ( ").append("\n");
			INSERT_TB_FUNC.append("	FUNC_ID /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, FUNC_NM /* 기능명 */ ").append("\n");
			INSERT_TB_FUNC.append("	, FUNC_KIND /* 기능종류(U:UI/C:컨트롤러/S:서비스/D:DAO) */ ").append("\n");
			INSERT_TB_FUNC.append("	, CALL_URL /* 호출URL */ ").append("\n");
			INSERT_TB_FUNC.append(") VALUES ( ").append("\n");
			INSERT_TB_FUNC.append("	? /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC.append("	, ? /* 기능명 */ ").append("\n");
			INSERT_TB_FUNC.append("	, ? /* 기능종류(U:UI/C:컨트롤러/S:서비스/D:DAO) */ ").append("\n");
			INSERT_TB_FUNC.append("	, ? /* 호출URL */ ").append("\n");
			INSERT_TB_FUNC.append(") ").append("\n");
			/* INSERT_TB_FUNC_MAPPING */
			INSERT_TB_FUNC_MAPPING.append("INSERT INTO TB_FUNC_MAPPING ( ").append("\n");
			INSERT_TB_FUNC_MAPPING.append("	FUNC_ID /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_MAPPING.append("	, CALL_FUNC_ID /* 호출기능ID */ ").append("\n");
			INSERT_TB_FUNC_MAPPING.append(") VALUES ( ").append("\n");
			INSERT_TB_FUNC_MAPPING.append("	? /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_MAPPING.append("	, ? /* 호출기능ID */ ").append("\n");
			INSERT_TB_FUNC_MAPPING.append(") ").append("\n");
			/* INSERT_TB_FUNC_TBL_MAPPING */
			INSERT_TB_FUNC_TBL_MAPPING.append("INSERT INTO TB_FUNC_TBL_MAPPING ( ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	FUNC_ID /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, TBL_ID /* 테이블ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, TBL_NM /* 테이블명 */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append(") VALUES ( ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	? /* 기능ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, ? /* 테이블ID */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append("	, ? /* 테이블명 */ ").append("\n");
			INSERT_TB_FUNC_TBL_MAPPING.append(") ").append("\n");

			/* DELETE_TB_FUNC */
			DELETE_TB_FUNC.append("DELETE FROM TB_FUNC").append("\n");
			/* DELETE_TB_FUNC_MAPPING */
			DELETE_TB_FUNC_MAPPING.append("DELETE FROM TB_FUNC_MAPPING").append("\n");
			/* DELETE_TB_FUNC_TBL_MAPPING */
			DELETE_TB_FUNC_TBL_MAPPING.append("DELETE FROM TB_FUNC_TBL_MAPPING").append("\n");
			
		}
	}
	
	public static String getDdl(String DB_KIND) {
		if( "MYSQL".equals(DB_KIND) ) {
			return DDL.MYSQL.toString();
		}else if( "ORACLE".equals(DB_KIND) ) {
			return DDL.ORACLE.toString();
		}else {
			return null;
		}
	}
	
	public static void insertTB_FUNC(String DBID, ClzzVo clzzVo, MtdVo mtdVo) {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			db.setQuery(QUERY.INSERT_TB_FUNC.toString());
			
			String funcId = "";
			
			if( clzzVo != null ) {
				funcId = funcId + (funcId.length() > 0 ? "." : "") + clzzVo.getPackageId();
			}
			if( clzzVo != null ) {
				funcId = funcId + (funcId.length() > 0 ? "." : "") + clzzVo.getClassId();
			}
			if( mtdVo != null ) {
				funcId = funcId + (funcId.length() > 0 ? "." : "") + mtdVo.getMethodId();
			}
			db.pstmt.setString(++parameterIndex, funcId); /* 기능ID */

			if( mtdVo.getMethodName() != null) {
				db.pstmt.setString(++parameterIndex, mtdVo.getMethodName()); /* 기능명 */
			}else {
				db.pstmt.setNull(++parameterIndex, java.sql.Types.NULL); /* 기능명 */
			}

			if( clzzVo != null && clzzVo.getClassKind() != null) {
				db.pstmt.setString(++parameterIndex, clzzVo.getClassKind().getClzzKindCd()); /* 기능종류(U:UI/C:컨트롤러/S:서비스/D:DAO) */
			}else {
				db.pstmt.setNull(++parameterIndex, java.sql.Types.NULL); /* 기능종류(U:UI/C:컨트롤러/S:서비스/D:DAO) */
			}

			if( clzzVo != null && clzzVo.getPackageId() != null) {
				db.pstmt.setString(++parameterIndex, clzzVo.getPackageId()); /* 패키지 */
			}else {
				db.pstmt.setNull(++parameterIndex, java.sql.Types.NULL); /* 패키지 */
			}

			if( clzzVo != null && clzzVo.getClassId() != null) {
				db.pstmt.setString(++parameterIndex, clzzVo.getClassId()); /* 클래스 */
			}else {
				db.pstmt.setNull(++parameterIndex, java.sql.Types.NULL); /* 클래스 */
			}

			db.insert();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC_MAPPING(String DBID, MtdVo mtdVo) {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			if( mtdVo.getMethodId() != null && mtdVo.getCallMtdVoList() != null ) {
//				List<CallsMtdVo> callsMtdVoList = mtdVo.getCallMtdVoList();
//				for(CallsMtdVo callsMtdVo : callsMtdVoList) {
//					db.setQuery(QUERY.INSERT_TB_FUNC_MAPPING.toString());					
//					parameterIndex = 0;
//					db.pstmt.setString(++parameterIndex, callsMtdVo.getFunctionId()); /* 기능ID */
//					db.pstmt.setString(++parameterIndex, callsMtdVo.getCallFunctionId()); /* 호출기능ID */
//					db.insert();
//				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void insertTB_FUNC_TBL_MAPPING(String DBID, MtdVo mtdVo) {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			
			if( mtdVo.getMethodId() != null && mtdVo.getCallTblVoList() != null ) {
//				List<CallsTblVo> callsMtdVoList = mtdVo.getCallTblVoList();
//				for(CallsTblVo tblVo : callsMtdVoList) {
//					db.setQuery(QUERY.INSERT_TB_FUNC_TBL_MAPPING.toString());					
//					parameterIndex = 0;
//					db.pstmt.setString(++parameterIndex, tblVo.getFunctionId()); /* 기능ID */
//					db.pstmt.setString(++parameterIndex, tblVo.getCallTblId()); /* 테이블ID */
//					/* 테이블명 ? */
//					db.insert();
//				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
	public static void deleteAll(String DBID) {
		deleteTB_FUNC(DBID);
		deleteTB_FUNC_MAPPING(DBID);
		deleteTB_FUNC_TBL_MAPPING(DBID);
	}
	
	private static void deleteTB_FUNC(String DBID) {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_FUNC.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}

	private static void deleteTB_FUNC_MAPPING(String DBID) {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_FUNC_MAPPING.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}

	private static void deleteTB_FUNC_TBL_MAPPING(String DBID) {
		net.dstone.common.utils.DbUtil db = null;
		int parameterIndex = 0;
		try {
			db = new net.dstone.common.utils.DbUtil(DBID);
			db.getConnection();
			db.setQuery(QUERY.DELETE_TB_FUNC_TBL_MAPPING.toString());
			db.delete();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(db != null) {
				db.release();
			}
		}
	}
	
}
