package net.dstone.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jsqlparser.util.TablesNamesFinder;

public class SqlUtil {
	
	/**
	 * 쿼리내의 테이블명 목록을 반환한다.
	 * @param sql
	 * @return
	 */
	public static List<String> getTableNames(String sql){
		List<String> tableNameList = new ArrayList<String>();
		try {
			Iterator<String> tableNames = TablesNamesFinder.findTables(sql).iterator();
			while(tableNames.hasNext()) {
				tableNameList.add(tableNames.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tableNameList;
	}


}
