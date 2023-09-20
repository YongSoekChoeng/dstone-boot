package net.dstone.common.tools.analyzer.svc.query.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dstone.common.tools.analyzer.svc.query.Query;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.utils.FileUtil;
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
			org.w3c.dom.NodeList nodeList = xml.getNodeListByExp("//mapper/*");
			if( nodeList != null ){
				Map<String, String> row = new HashMap<String, String>();
				for(int i=0; i<nodeList.getLength(); i++){
					org.w3c.dom.Node item =	nodeList.item(i);
					row = new HashMap<String, String>();
					
					row.put("SQL_NAMESPACE", namespace);
					row.put("SQL_ID", item.getAttributes().getNamedItem("id").getTextContent());
					row.put("SQL_KIND", item.getNodeName().toUpperCase());
					row.put("SQL_BODY", ParseUtil.simplifySql(item, row.get("SQL_KIND")));

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
	public List<String> getTblInfoList(String queryInfoFile) {
		String query = net.dstone.common.utils.FileUtil.readFile(queryInfoFile);
		return SqlUtil.getTableNames(query);
	}

}
