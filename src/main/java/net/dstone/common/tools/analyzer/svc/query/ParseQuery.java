package net.dstone.common.tools.analyzer.svc.query;

import java.util.List;
import java.util.Map;

public interface ParseQuery {
	/**
	 * 파일로부터 쿼리정보목록 추출
	 * @param file
	 * @return
	 */
	public List<Map<String, String>> getQueryInfoList(String queryFile) throws Exception ;
	/**
	 * 쿼리정보파일로부터 테이블ID정보목록 추출
	 * @param file
	 * @return
	 */
	public List<String> getTblInfoList(String queryInfoFile, List<String> allTblList) throws Exception ;
	
}
