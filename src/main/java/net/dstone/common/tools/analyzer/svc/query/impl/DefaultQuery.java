package net.dstone.common.tools.analyzer.svc.query.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.svc.query.Query;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.QueryVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.LogUtil;
import net.dstone.common.utils.SqlUtil;
import net.dstone.common.utils.StringUtil;
import net.dstone.common.utils.XmlUtil;

public class DefaultQuery implements Query {
	
	/**
	 * 파일로부터 쿼리정보목록 추출
	 * @param file
	 * @return
	 */
	@Override
	public List<Map<String, String>> getQueryInfoList(String queryFile) {
		List<Map<String, String>> qList = new ArrayList<Map<String, String>>();
		if(FileUtil.isFileExist(queryFile)) {
			XmlUtil xml = XmlUtil.getInstance(XmlUtil.XML_SOURCE_KIND_PATH, queryFile);
			String namespace = xml.getNode("mapper").getAttributes().getNamedItem("namespace").getTextContent();
			String nodeExp = "/mapper/*";
			NodeList nodeList = xml.getNodeListByExp(nodeExp);
			StringBuffer sqlBuff = new StringBuffer();
			
			NodeList childNodeList = null;
			Node childNode = null;
			Node sqlChildNode = null;
			String keyword = "";
			String refid = "";
			
			if( nodeList != null ){
				Map<String, String> row = new HashMap<String, String>();
				String sqlBody = "";
				for(int i=0; i<nodeList.getLength(); i++){
					Node item =	nodeList.item(i);
					row = new HashMap<String, String>();
					
					row.put("SQL_NAMESPACE", namespace);
					row.put("SQL_ID", item.getAttributes().getNamedItem("id").getTextContent());
					row.put("SQL_KIND", item.getNodeName().toUpperCase());
					
					nodeExp = "/mapper/" + item.getNodeName() + "[@id='" + item.getAttributes().getNamedItem("id").getTextContent() + "']";
					sqlBody = xml.getNodeTextByExpForMybatis(nodeExp, true);
					sqlBody = ParseUtil.simplifySqlForTblNm(sqlBody, row.get("SQL_KIND"));
					row.put("SQL_BODY", sqlBody);

					if(!StringUtil.isEmpty(row.get("SQL_BODY"))) {
						qList.add(row);
					}
					
				}
			}
		}
		return qList;
	}

	/**
	 * 쿼리정보파일로부터 테이블ID정보목록 추출
	 * @param queryInfoFile
	 * @return
	 */
	@Override
	public List<String> getTblInfoList(String queryInfoFile) throws Exception {
		List<String> tblNameList = new ArrayList<String>();
		QueryVo queryVo = ParseUtil.readQueryVo(FileUtil.getFileName(queryInfoFile, false), AppAnalyzer.WRITE_PATH + "/query");
		if(queryVo != null) {
			if(!StringUtil.isEmpty(queryVo.getQueryBody())) {
				tblNameList = SqlUtil.getTableNames(queryVo.getQueryBody());
			}
			if(tblNameList.isEmpty()) {
				LogUtil.sysout(this.getClass().getName() + ".getTblInfoList :: 파일["+queryInfoFile+"] 을 분석하였으나 테이블명 조회 하지 못함.");
			}
		}
		return tblNameList;
	}

}
