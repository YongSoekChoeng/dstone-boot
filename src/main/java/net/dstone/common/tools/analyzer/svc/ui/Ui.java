package net.dstone.common.tools.analyzer.svc.ui;

import java.util.List;
import java.util.Map;

public interface Ui {
	
	/**
	 * UI파일로부터 UI명 추출
	 * @param uiFile
	 * @return
	 */
	public String getUiName(String uiFile) throws Exception ;

	/**
	 * UI파일로부터 Include된 타 UI파일목록 추출
	 * @param uiFile
	 * @return
	 */
	public List<String> getIncludeUiList(String uiFile) throws Exception ;

	/**
	 * UI파일로부터 링크목록 추출
	 * @param uiFile
	 * @return
	 */
	public List<String> getUiLinkList(String uiFile) throws Exception ;
}