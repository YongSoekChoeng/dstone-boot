package net.dstone.common.tools.analyzer.svc.query.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.svc.query.ParseQuery;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.QueryVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.LogUtil;
import net.dstone.common.utils.SqlUtil;
import net.dstone.common.utils.StringUtil;
import net.dstone.common.utils.XmlUtil;

public class TossParseQuery extends MybatisParseQuery implements ParseQuery {
	
	/**
	 * 파일로부터 쿼리KEY(아이디)를 추출. 쿼리KEY는 파일명으로 사용됨.
	 * @param queryInfo(쿼리KEY를 추출할 수 있는 각종 정보를 담은 맵)
	 * @return
	 */
	@Override
	public String getQueryKey(Map<String, String> queryInfo) throws Exception {
		return super.getQueryKey(queryInfo);
	}
	
	/**
	 * 파일로부터 쿼리정보목록 추출. 쿼리정보는 아래와 같은 항목을 추출해야 한다.
	 * SQL_NAMESPACE - 네임스페이스
	 * SQL_ID - SQL아이디
	 * SQL_KIND - SQL종류(SELECT/INSERT/UPDATE/DELETE)
	 * SQL_BODY - SQL구문
	 * @param file
	 * @return
	 */
	@Override
	public List<Map<String, String>> getQueryInfoList(String queryFile) {
		return super.getQueryInfoList(queryFile);
	}

	/**
	 * 쿼리정보파일로부터 테이블ID정보목록 추출
	 * @param queryInfoFile
	 * @param allTblList
	 * @return
	 */
	@Override
	public List<String> getTblInfoList(String queryInfoFile, List<String> allTblList) throws Exception {
		return super.getTblInfoList(queryInfoFile, allTblList);
	}

}
