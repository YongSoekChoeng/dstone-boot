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

public class MybatisParseQuery implements ParseQuery {
	
	/**
	 * 파일로부터 쿼리KEY(아이디)를 추출. 쿼리KEY는 파일명으로 사용됨.
	 * @param queryInfo(쿼리KEY를 추출할 수 있는 각종 정보를 담은 맵)
	 * @return
	 */
	@Override
	public String getQueryKey(Map<String, String> queryInfo) throws Exception {
		String queryKey = "";
		if( queryInfo != null ) {
			// KEY
			if(StringUtil.isEmpty(queryInfo.get("SQL_NAMESPACE"))) {
				queryKey = "NO_NAMESPACE" + "_" + queryInfo.get("SQL_ID");
			}else {
				queryKey = queryInfo.get("SQL_NAMESPACE") + "_" + queryInfo.get("SQL_ID");
			}
		}
		return queryKey;
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
		List<Map<String, String>> qList = new ArrayList<Map<String, String>>();
		if(FileUtil.isFileExist(queryFile)) {
			XmlUtil xml = XmlUtil.getInstance(XmlUtil.XML_SOURCE_KIND_PATH, queryFile);
			String rootKeyword = "";
			List<String> queryKinds = new ArrayList<String>();
			queryKinds.add("select");
			queryKinds.add("insert");
			queryKinds.add("update");
			queryKinds.add("delete");
			queryKinds.add("sql");
			
			// Mibatis
			if(xml.hasNode("mapper")) {
				rootKeyword = "mapper";
			}else if(xml.hasNode("Mapper")) {
					rootKeyword = "Mapper";
			// Ibatis
			}else if(xml.hasNode("sqlMap")) {
				rootKeyword = "sqlMap";
			}else if(xml.hasNode("SqlMap")) {
				rootKeyword = "SqlMap";
			}
			if( xml.hasNode(rootKeyword) && xml.getNode(rootKeyword).getAttributes() != null ) {
				String namespace = xml.getNode(rootKeyword).getAttributes().getNamedItem("namespace").getTextContent();
				String nodeExp = "/"+rootKeyword+"/*";
				NodeList nodeList = xml.getNodeListByExp(nodeExp);
				if( nodeList != null ){
					Map<String, String> row = new HashMap<String, String>();
					String sqlBody = "";
					for(int i=0; i<nodeList.getLength(); i++){
						Node item =	nodeList.item(i);
						if( !queryKinds.contains(item.getNodeName()) ) {continue;}
						
						row = new HashMap<String, String>();
						row.put("SQL_NAMESPACE", namespace);
						row.put("SQL_ID", item.getAttributes().getNamedItem("id").getTextContent());
						row.put("SQL_KIND", item.getNodeName().toUpperCase());
						
						nodeExp = "/"+rootKeyword+"/" + item.getNodeName() + "[@id='" + item.getAttributes().getNamedItem("id").getTextContent() + "']";
						// Mybatis/Ibatis 쿼리의 내부 태그 제거.
						sqlBody = ParseUtil.removeMybatisTagFromSql(xml, nodeExp, true);
						// 테이블명을 파싱하기 좋게 SQL을 간소화.
						sqlBody = ParseUtil.removeBasicTagFromSql(sqlBody, row.get("SQL_KIND"));
						row.put("SQL_BODY", sqlBody.toUpperCase());

						if(!StringUtil.isEmpty(row.get("SQL_BODY"))) {
							qList.add(row);
						}
					}
				}
			}
		}
		return qList;
	}

	/**
	 * 쿼리정보파일로부터 테이블ID정보목록 추출
	 * @param queryInfoFile
	 * @param allTblList
	 * @return
	 */
	@Override
	public List<String> getTblInfoList(String queryInfoFile, List<String> allTblList) throws Exception {
		List<String> tblNameList = new ArrayList<String>();
		QueryVo queryVo = ParseUtil.readQueryVo(FileUtil.getFileName(queryInfoFile, false), AppAnalyzer.WRITE_PATH + "/query");
		if(queryVo != null) {
			if(!StringUtil.isEmpty(queryVo.getQueryBody())) {
				if(allTblList != null && allTblList.size() > 0) {
					tblNameList = SqlUtil.getTableNamesWithTblList(queryVo.getQueryBody(), allTblList);
				}else {
					tblNameList = SqlUtil.getTableNames(queryVo.getQueryBody());
				}
			}
			if(tblNameList.isEmpty()) {
				LogUtil.sysout("DefaultQuery.getTblInfoList :: 파일["+queryInfoFile+"] 을 분석하였으나 테이블명 조회 하지 못함.");
			}
		}
		return tblNameList;
	}

}
