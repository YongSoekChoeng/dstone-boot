package net.dstone.common.tools.analyzer.svc.ui.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.dstone.common.tools.analyzer.svc.ui.Ui;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class DefaultUi implements Ui {
	
	/**
	 * UI파일로부터 UI명 추출
	 * @param uiFile
	 * @return
	 */
	public String getUiName(String uiFile) throws Exception {
		String uiName = "";
		String[] lines = FileUtil.readFileByLines(uiFile);
		List<String> keywordList = new ArrayList<String>();
		keywordList.add("DESCRIPTION");
		keywordList.add("DESC");
		keywordList.add("PROGRAM NAME");
		
		if(lines!=null) {
			for(String line : lines) {
				for(String keyword : keywordList) {
					if(line.toUpperCase().indexOf(keyword) != -1) {
						uiName = line;
						uiName = line.substring(line.toUpperCase().indexOf(keyword)+keyword.length());
						break;
					}
				}
				if(!StringUtil.isEmpty(uiName)) {
					uiName = StringUtil.replace(uiName, ":", "");
					uiName = StringUtil.replace(uiName, ";", "");
					uiName = StringUtil.replace(uiName, " ", "");
					break;
				}
			}
		}
		return uiName;
	}
	
	/**
	 * UI파일로부터 링크목록 추출
	 * @param uiFile
	 * @return
	 */
	@Override
	public List<String> getUiLinkList(String uiFile) throws Exception {
		List<String> uiLinkList = new ArrayList<String>();
		
		try {
			if(FileUtil.isFileExist(uiFile)) {
				
				// A태그 로부터 링크를 추출.
				uiLinkList.addAll(ParseUtil.extrackLinksFromAtag(uiFile));
				
				// 폼.action 으로부터 링크를 추출.
				uiLinkList.addAll(ParseUtil.extrackLinksFromAction(uiFile));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uiLinkList.stream().distinct().collect(Collectors.toList());
	}

}
