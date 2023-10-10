package net.dstone.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.dstone.common.core.BaseObject;
import net.sf.jsqlparser.util.TablesNamesFinder;

public class SqlUtil extends BaseObject {
	
	/**
	 * 쿼리내의 테이블명 목록을 반환한다.
	 * @param sql
	 * @return
	 */
	public static List<String> getTableNames(String sql) throws Exception{
		List<String> tableNameList = new ArrayList<String>();
		try {
			
			Iterator<String> tableNames = TablesNamesFinder.findTables(sql).iterator();
			while(tableNames.hasNext()) {
				tableNameList.add(tableNames.next());
			}
		} catch (Exception e) {
			LogUtil.sysout("net.dstone.common.utils.SqlUtil.getTableNames() 수행중 예외발생. 쿼리:\n" + sql);
			e.printStackTrace();
			//throw e;
		}
		return tableNameList;
	}
	
	/**
	 * 쿼리내의 주석을 제거한다.
	 * @param sql
	 * @return
	 */
	public static String removeCommentsFromSql(String sql){
		String returnSql = sql; 
		String rexp = "";
		try {
			if(!StringUtil.isEmpty(sql)) {

//				rexp = "(/\\*([^*]|[\\r\\n]|(\\*+([^*/]|[\\r\\n])))*\\*+/)|'(?:[^']|'')*'|(--.*)";
//				rexp = "(?:/\\\\*[^;]*?\\\\*/)|(?:--[^;]*?$)";
				rexp = "(?s)(?:\\/\\*.*?\\*\\/|--[^\\n]*)";
				returnSql = returnSql.replaceAll(rexp, "");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnSql;
	}

	public static String getParamByType(String DATA_TYPE, String COLUMN_NAME, String dbKind){
		return getParamByType(DATA_TYPE, COLUMN_NAME, dbKind, "MYBATIS");
	}
	
	public static String getParamByType(String DATA_TYPE, String COLUMN_NAME, String dbKind, String queryKind){
		String outStr = "";
		String colType = DATA_TYPE.toUpperCase();
		if(colType.indexOf(".") > -1){
			colType = colType.substring(colType.lastIndexOf("."));
		}
		if ("ORACLE".equals(dbKind)) {
			if (colType.equals("FLOAT") || colType.equals("INT") || colType.equals("DOUBLE") || colType.equals("NUMBER") || colType.equals("NUMERIC")) {
				if("MYBATIS".equals(queryKind)) {
					outStr = "#{" + COLUMN_NAME + "}";
				}else {
					outStr = "?";
				}
			}else if (colType.equals("DATE") || colType.equals("TIME") ||  colType.equals("TIMESTAMP")) {
				if ( colType.equals("DATE") ) {
					if("MYBATIS".equals(queryKind)) {
						outStr = "TO_DATE( #{" + COLUMN_NAME + "}, 'YYYYMMDDHH24MISS')";
					}else {
						outStr = "TO_DATE( ?, 'YYYYMMDDHH24MISS')";
					}
				}else if ( colType.equals("Time") ) {
					if("MYBATIS".equals(queryKind)) {
						outStr = "TO_DATE( #{" + COLUMN_NAME + "}, 'YYYYMMDDHH24MISS')";
					}else {
						outStr = "TO_DATE( ?, 'YYYYMMDDHH24MISS')";
					}
				}else if ( colType.equals("Timestamp") ) {
					if("MYBATIS".equals(queryKind)) {
						outStr = "TO_DATE( #{" + COLUMN_NAME + "}, 'YYYYMMDDHH24MISSFF3')";
					}else {
						outStr = "TO_DATE( ?, 'YYYYMMDDHH24MISSFF3')";
					}
				}
			} else {
				if("MYBATIS".equals(queryKind)) {
					outStr = "#{" + COLUMN_NAME + "}";
				}else {
					outStr = "?";
				}
			}
		} else if ("MSSQL".equals(dbKind)) {
			if (colType.equals("FLOAT") || colType.equals("INT") || colType.equals("DOUBLE") || colType.equals("NUMBER") || colType.equals("NUMERIC")) {
				if("MYBATIS".equals(queryKind)) {
					outStr = "#{" + COLUMN_NAME + "}";
				}else {
					outStr = "?";
				}
			}else if (colType.equals("DATE") || colType.equals("TIME") ||  colType.equals("TIMESTAMP")) {
				if("MYBATIS".equals(queryKind)) {
					outStr = "CONVERT(DATETIME, #{" + COLUMN_NAME + "} )";
				}else {
					outStr = "CONVERT(DATETIME, ? )";
				}
			} else {
				if("MYBATIS".equals(queryKind)) {
					outStr = "#{" + COLUMN_NAME + "}";
				}else {
					outStr = "?";
				}
			}
		} else if ("MYSQL".equals(dbKind)) {
			if (colType.equals("FLOAT") || colType.equals("INT") || colType.equals("DOUBLE") || colType.equals("NUMBER") || colType.equals("NUMERIC")) {
				if("MYBATIS".equals(queryKind)) {
					outStr = "#{" + COLUMN_NAME + "}";
				}else {
					outStr = "?";
				}
			}else if (colType.equals("DATE") || colType.equals("TIME") ||  colType.equals("TIMESTAMP")) {
				if("MYBATIS".equals(queryKind)) {
					outStr = "STR_TO_DATE( #{" + COLUMN_NAME + "}, '%Y%m%d%H%i%s' )";
				}else {
					outStr = "STR_TO_DATE( ?, '%Y%m%d%H%i%s' )";
				}
			} else {
				if("MYBATIS".equals(queryKind)) {
					outStr = "#{" + COLUMN_NAME + "}";
				}else {
					outStr = "?";
				}
			}
		}
		return outStr;
	}
	public static String getPagingQuery(String dbKind, int upOrDown){
		String pagingQuery = "";
		StringBuffer pagingUpConts = new StringBuffer();
		StringBuffer pagingLowConts = new StringBuffer();
		
		if ("ORACLE".equals(dbKind)) {
			// 상단 쿼리
			if(upOrDown == 0){
				pagingUpConts.append("		SELECT  ").append("\n");
				pagingUpConts.append("			RNUM ").append("\n");
				pagingUpConts.append("			,P.* ").append("\n");
				pagingUpConts.append("		FROM  ").append("\n");
				pagingUpConts.append("		    (  ").append("\n");
				pagingUpConts.append("		    SELECT  ").append("\n");
				pagingUpConts.append("		        ROWNUM RNUM, P1.*  ").append("\n");
				pagingUpConts.append("		    FROM  ").append("\n");
				pagingUpConts.append("		        (  ").append("\n");
				pagingUpConts.append("                 /**********************************************************************************************************************************/ ").append("\n");
			// 하단 쿼리
			}else if(upOrDown == 1){
				pagingLowConts.append("                /**********************************************************************************************************************************/ ").append("\n");
				pagingLowConts.append("		        ) P1 ").append("\n");
				pagingLowConts.append("		    )   P ").append("\n");
				pagingLowConts.append("		WHERE 2>1 ").append("\n");
				pagingLowConts.append("		    AND RNUM <![CDATA[>=]]> #{INT_FROM} ").append("\n");
				pagingLowConts.append("		    AND RNUM <![CDATA[<=]]> #{INT_TO} ").append("\n");
				pagingLowConts.append("		    AND ROWNUM <![CDATA[<=]]> #{PAGE_SIZE} ");
			}
		} else if ("MSSQL".equals(dbKind)) {
			// 상단 쿼리
			if(upOrDown == 0){
				pagingUpConts.append("		SELECT  ").append("\n");
				pagingUpConts.append("			RNUM ").append("\n");
				pagingUpConts.append("			,P.* ").append("\n");
				pagingUpConts.append("		FROM  ").append("\n");
				pagingUpConts.append("		    (  ").append("\n");
				pagingUpConts.append("		    SELECT  ").append("\n");
				pagingUpConts.append("		        /* 페이징의 기준이 되는 KEY값을 반드시 넣어주시기 바랍니다. */  ").append("\n");
				pagingUpConts.append("		        ROW_NUMBER() OVER(ORDER BY @KEY값@ DESC) AS ROWNUM RNUM, P1.*  ").append("\n");
				pagingUpConts.append("		    FROM  ").append("\n");
				pagingUpConts.append("		        (  ").append("\n");
				pagingUpConts.append("                /**********************************************************************************************************************************/ ").append("\n");
			// 하단 쿼리
			}else if(upOrDown == 1){
				pagingLowConts.append("                /**********************************************************************************************************************************/ ").append("\n");
				pagingLowConts.append("		        ) P1 ").append("\n");
				pagingLowConts.append("		    )   P ").append("\n");
				pagingLowConts.append("		WHERE 2>1 ").append("\n");
				pagingLowConts.append("		    AND RNUM <![CDATA[>=]]> #{INT_FROM} ").append("\n");
				pagingLowConts.append("		    AND RNUM <![CDATA[<=]]> #{INT_TO} ").append("\n");
				pagingLowConts.append("		    AND ROWNUM <![CDATA[<=]]> #{PAGE_SIZE} ");
			}
		} else if ("MYSQL".equals(dbKind)) {
			// 상단 쿼리
			if(upOrDown == 0){
				pagingUpConts.append("		SELECT  ").append("\n");
				pagingUpConts.append("			P.* ").append("\n");
				pagingUpConts.append("		FROM  ").append("\n");
				pagingUpConts.append("		    (  ").append("\n");
				pagingUpConts.append("            /**********************************************************************************************************************************/ ").append("\n");
			// 하단 쿼리
			}else if(upOrDown == 1){
				pagingLowConts.append("            /**********************************************************************************************************************************/ ").append("\n");
				pagingLowConts.append("		    )   P ").append("\n");
				pagingLowConts.append("		WHERE 2>1 ").append("\n");
				pagingLowConts.append("		    /* 페이징의 기준이 되는 KEY값을 반드시 넣어주시기 바랍니다. */  ").append("\n");
				pagingLowConts.append("		ORDER BY @KEY값@ LIMIT #{INT_FROM}, #{INT_TO} ").append("\n");
			}
		}
		
		// 상단 쿼리
		if(upOrDown == 0){
			pagingQuery = pagingUpConts.toString();
		// 하단 쿼리
		}else if(upOrDown == 1){
			pagingQuery = pagingLowConts.toString();
		}

		return pagingQuery;
	}
	
	/**
	 * 테이블명으로 SELECT 쿼리를 생성하는 메소드.
	 * @param DBID
	 * @param TABLE_NAME
	 * @param queryKind (BASIC/MYBATIS)
	 * @param tableAlias
	 * @return
	 */
	public static String getSelectSql(String DBID, String TABLE_NAME, String queryKind, String tableAlias) {
		StringBuffer sql = new StringBuffer();
		net.dstone.common.utils.DataSet dsCols = null;
		net.dstone.common.utils.DataSet dsKeys = null;
		try {
			dsCols = net.dstone.common.utils.DbUtil.getCols(DBID, TABLE_NAME);
			dsKeys = net.dstone.common.utils.DbUtil.getKeys(DBID, TABLE_NAME);
			
			if(dsCols != null){
				String COLUMN_NAME = "";
				String COLUMN_COMMENT = "";
				String DATA_TYPE = "";
				int DATA_LENGTH = 0;
				sql.append("SELECT ").append("\n");
				if (dsCols.getDataSetRowCount("COL_LIST") > 0) {
					for (int i = 0; i < dsCols.getDataSetRowCount("COL_LIST"); i++) {
						COLUMN_NAME = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsCols.getDataSet("COL_LIST", i).getDatum("DATA_TYPE");
						if(!StringUtil.isEmpty(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH"))){
							DATA_LENGTH = Integer.parseInt(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH", "0"));
						}else {
							DATA_LENGTH = 0;
						}
						sql.append("\t");
						if(i > 0 ) {
							sql.append(", ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append("FROM").append("\n");
				sql.append("\t").append(TABLE_NAME).append( (StringUtil.isEmpty(tableAlias)?"":" " + tableAlias) ).append("\n");
				sql.append("WHERE").append("\n");
				if (dsKeys.getDataSetRowCount("KEY_LIST") > 0) {
					for (int i = 0; i < dsKeys.getDataSetRowCount("KEY_LIST"); i++) {
						COLUMN_NAME = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsKeys.getDataSet("KEY_LIST", i).getDatum("DATA_TYPE");
						sql.append("\t");
						if(i > 0 ) {
							sql.append("AND ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" = ").append(getParamByType(DATA_TYPE, COLUMN_NAME, dsKeys.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append("\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	
	
	
	/**
	 * 테이블명으로 MERGE 쿼리를 생성하는 메소드.
	 * @param DBID
	 * @param TABLE_NAME
	 * @param queryKind (BASIC/MYBATIS)
	 * @param tableAlias
	 * @return
	 */
	public static String getMergeSql(String DBID, String TABLE_NAME, String queryKind, String tableAlias) {
		StringBuffer sql = new StringBuffer();
		net.dstone.common.utils.DataSet dsCols = null;
		net.dstone.common.utils.DataSet dsKeys = null;
		try {
			dsCols = net.dstone.common.utils.DbUtil.getCols(DBID, TABLE_NAME);
			dsKeys = net.dstone.common.utils.DbUtil.getKeys(DBID, TABLE_NAME);
			
			if(dsCols != null){
				String COLUMN_NAME = "";
				String COLUMN_COMMENT = "";
				String DATA_TYPE = "";
				int DATA_LENGTH = 0;
				
				String div = "\n";
				sql.append("MERGE INTO ").append(TABLE_NAME).append( (StringUtil.isEmpty(tableAlias)?"":" " + tableAlias) ).append(div);
				sql.append("USING DUAL").append(div);
				sql.append("ON (").append(div);
				if (dsKeys.getDataSetRowCount("KEY_LIST") > 0) {
					for (int i = 0; i < dsKeys.getDataSetRowCount("KEY_LIST"); i++) {
						COLUMN_NAME = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsKeys.getDataSet("KEY_LIST", i).getDatum("DATA_TYPE");
						sql.append("\t");
						if(i > 0 ) {
							sql.append("AND ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" = ").append(getParamByType(DATA_TYPE, COLUMN_NAME, dsKeys.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append(")").append(div);
				sql.append("WHEN MATCH THEN ").append(div);
				sql.append("UPDATE SET ").append(div);
				if (dsCols.getDataSetRowCount("COL_LIST") > 0) {
					for (int i = 0; i < dsCols.getDataSetRowCount("COL_LIST"); i++) {
						COLUMN_NAME = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsCols.getDataSet("COL_LIST", i).getDatum("DATA_TYPE");
						if(!StringUtil.isEmpty(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH"))){
							DATA_LENGTH = Integer.parseInt(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH", "0"));
						}else {
							DATA_LENGTH = 0;
						}
						sql.append("\t");
						if(i > 0 ) {
							sql.append(", ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" = ").append(getParamByType(DATA_TYPE, COLUMN_NAME, dsCols.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}

				sql.append("WHEN NOT MATCH THEN ").append(div);
				sql.append("INSERT (").append(div);
				if (dsCols.getDataSetRowCount("COL_LIST") > 0) {
					for (int i = 0; i < dsCols.getDataSetRowCount("COL_LIST"); i++) {
						COLUMN_NAME = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsCols.getDataSet("COL_LIST", i).getDatum("DATA_TYPE");
						if(!StringUtil.isEmpty(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH"))){
							DATA_LENGTH = Integer.parseInt(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH", "0"));
						}else {
							DATA_LENGTH = 0;
						}
						sql.append("\t");
						if(i > 0 ) {
							sql.append(", ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append(") VALUES (").append(div);
				if (dsCols.getDataSetRowCount("COL_LIST") > 0) {
					for (int i = 0; i < dsCols.getDataSetRowCount("COL_LIST"); i++) {
						COLUMN_NAME = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsCols.getDataSet("COL_LIST", i).getDatum("DATA_TYPE");
						sql.append("\t");
						if(i > 0 ) {
							sql.append(", ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(getParamByType(DATA_TYPE, COLUMN_NAME, dsCols.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append(");").append(div);
				sql.append(div);
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	
	/**
	 * 테이블명으로 INSERT 쿼리를 생성하는 메소드.
	 * @param DBID
	 * @param TABLE_NAME
	 * @param queryKind (BASIC/MYBATIS)
	 * @param tableAlias
	 * @return
	 */
	public static String getInsertSql(String DBID, String TABLE_NAME, String queryKind, String tableAlias) {
		StringBuffer sql = new StringBuffer();
		net.dstone.common.utils.DataSet dsCols = null;
		net.dstone.common.utils.DataSet dsKeys = null;
		try {
			dsCols = net.dstone.common.utils.DbUtil.getCols(DBID, TABLE_NAME);
			dsKeys = net.dstone.common.utils.DbUtil.getKeys(DBID, TABLE_NAME);
			
			if(dsCols != null){
				String COLUMN_NAME = "";
				String COLUMN_COMMENT = "";
				String DATA_TYPE = "";
				int DATA_LENGTH = 0;
				sql.append("INSERT INTO "+TABLE_NAME + (StringUtil.isEmpty(tableAlias)?"":" " + tableAlias) +" (").append("\n");
				if (dsCols.getDataSetRowCount("COL_LIST") > 0) {
					for (int i = 0; i < dsCols.getDataSetRowCount("COL_LIST"); i++) {
						COLUMN_NAME = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsCols.getDataSet("COL_LIST", i).getDatum("DATA_TYPE");
						if(!StringUtil.isEmpty(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH"))){
							DATA_LENGTH = Integer.parseInt(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH", "0"));
						}else {
							DATA_LENGTH = 0;
						}
						sql.append("\t");
						if(i > 0 ) {
							sql.append(", ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append(") VALUES (").append("\n");
				if (dsCols.getDataSetRowCount("COL_LIST") > 0) {
					for (int i = 0; i < dsCols.getDataSetRowCount("COL_LIST"); i++) {
						COLUMN_NAME = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsCols.getDataSet("COL_LIST", i).getDatum("DATA_TYPE");
						sql.append("\t");
						if(i > 0 ) {
							sql.append(", ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(getParamByType(DATA_TYPE, COLUMN_NAME, dsCols.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append(");").append("\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	
	/**
	 * 테이블명으로 UPDATE 쿼리를 생성하는 메소드.
	 * @param DBID
	 * @param TABLE_NAME
	 * @param queryKind (BASIC/MYBATIS)
	 * @param tableAlias
	 * @return
	 */
	public static String getUpdateSql(String DBID, String TABLE_NAME, String queryKind, String tableAlias) {
		StringBuffer sql = new StringBuffer();
		net.dstone.common.utils.DataSet dsCols = null;
		net.dstone.common.utils.DataSet dsKeys = null;
		try {
			dsCols = net.dstone.common.utils.DbUtil.getCols(DBID, TABLE_NAME);
			dsKeys = net.dstone.common.utils.DbUtil.getKeys(DBID, TABLE_NAME);
			
			if(dsCols != null){
				String COLUMN_NAME = "";
				String COLUMN_COMMENT = "";
				String DATA_TYPE = "";
				int DATA_LENGTH = 0;
				sql.append("UPDATE "+TABLE_NAME).append( (StringUtil.isEmpty(tableAlias)?"":" " + tableAlias) ).append("\n");
				sql.append("SET ").append("\n");
				if (dsCols.getDataSetRowCount("COL_LIST") > 0) {
					for (int i = 0; i < dsCols.getDataSetRowCount("COL_LIST"); i++) {
						COLUMN_NAME = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsCols.getDataSet("COL_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsCols.getDataSet("COL_LIST", i).getDatum("DATA_TYPE");
						if(!StringUtil.isEmpty(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH"))){
							DATA_LENGTH = Integer.parseInt(dsCols.getDataSet("COL_LIST", i).getDatum("DATA_LENGTH", "0"));
						}else {
							DATA_LENGTH = 0;
						}
						sql.append("\t");
						if(i > 0 ) {
							sql.append(", ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" = ").append(getParamByType(DATA_TYPE, COLUMN_NAME, dsCols.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append("WHERE").append("\n");
				if (dsKeys.getDataSetRowCount("KEY_LIST") > 0) {
					for (int i = 0; i < dsKeys.getDataSetRowCount("KEY_LIST"); i++) {
						COLUMN_NAME = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsKeys.getDataSet("KEY_LIST", i).getDatum("DATA_TYPE");
						sql.append("\t");
						if(i > 0 ) {
							sql.append("AND ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" = ").append(getParamByType(DATA_TYPE, COLUMN_NAME, dsKeys.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append("\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	/**
	 * 테이블명으로 DELETE 쿼리를 생성하는 메소드.
	 * @param DBID
	 * @param TABLE_NAME
	 * @param queryKind (BASIC/MYBATIS)
	 * @param tableAlias
	 * @return
	 */
	public static String getDeleteSql(String DBID, String TABLE_NAME, String queryKind, String tableAlias) {
		StringBuffer sql = new StringBuffer();
		net.dstone.common.utils.DataSet dsCols = null;
		net.dstone.common.utils.DataSet dsKeys = null;
		try {
			
			dsCols = net.dstone.common.utils.DbUtil.getCols(DBID, TABLE_NAME);
			dsKeys = net.dstone.common.utils.DbUtil.getKeys(DBID, TABLE_NAME);
			
			if(dsCols != null){
				String COLUMN_NAME = "";
				String COLUMN_COMMENT = "";
				String DATA_TYPE = "";
				int DATA_LENGTH = 0;
				sql.append("DELETE FROM "+TABLE_NAME).append( (StringUtil.isEmpty(tableAlias)?"":" " + tableAlias) ).append("\n");
				sql.append("WHERE").append("\n");
				if (dsKeys.getDataSetRowCount("KEY_LIST") > 0) {
					for (int i = 0; i < dsKeys.getDataSetRowCount("KEY_LIST"); i++) {
						COLUMN_NAME = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_NAME");
						COLUMN_COMMENT = dsKeys.getDataSet("KEY_LIST", i).getDatum("COLUMN_COMMENT");
						DATA_TYPE = dsKeys.getDataSet("KEY_LIST", i).getDatum("DATA_TYPE");
						sql.append("\t");
						if(i > 0 ) {
							sql.append("AND ");
						}
						if(!StringUtil.isEmpty(tableAlias)) {
							sql.append(tableAlias+".");
						}
						sql.append(COLUMN_NAME).append(" = ").append(getParamByType(DATA_TYPE, COLUMN_NAME, dsKeys.getDatum("DB_KIND"), queryKind)).append(" /* "+COLUMN_COMMENT+" */").append("\n");
					}
				}
				sql.append("\n");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql.toString();
	}
	

	/**
	 * 테이블명으로 CRUD 쿼리를 생성하는 메소드.
	 * @param DBID
	 * @param TABLE_NAME
	 * @param queryKind (BASIC/MYBATIS)
	 * @param tableAlias
	 * @return
	 */
	public static String getCrudSql(String DBID, String TABLE_NAME, String queryKind, String tableAlias) {
		
		StringBuffer sql = new StringBuffer();

		try {
			
			String div = "\n";

			sql.append("/********* 1. SELECT *********/").append(div);
			sql.append(getSelectSql(DBID, TABLE_NAME, queryKind, tableAlias)).append(div);

			sql.append("/********* 2. MERGE *********/").append(div);
			sql.append(getMergeSql(DBID, TABLE_NAME, queryKind, tableAlias)).append(div);
		
			sql.append("/********* 3. INSERT *********/").append(div);
			sql.append(getInsertSql(DBID, TABLE_NAME, queryKind, tableAlias)).append(div);
		
			sql.append("/********* 4. UPDATE *********/").append(div);
			sql.append(getUpdateSql(DBID, TABLE_NAME, queryKind, tableAlias)).append(div);
		
			sql.append("/********* 5. DELETE *********/").append(div);
			sql.append(getDeleteSql(DBID, TABLE_NAME, queryKind, tableAlias)).append(div);
		
		}catch(Exception e){
			e.printStackTrace();
		}
		return sql.toString();

	}
}
