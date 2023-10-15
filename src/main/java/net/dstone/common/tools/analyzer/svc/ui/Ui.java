package net.dstone.common.tools.analyzer.svc.ui;

import java.util.List;

public interface Ui {
	
	/**
	 * UI파일로부터 UI명 추출
	 * @param uiFile
	 * @return
	 */
	public String getUiName(String uiFile) throws Exception ;

	/**
	 * UI파일로부터 링크목록 추출
	 * @param uiFile
	 * @return
	 */
	public List<String> getUiLinkList(String uiFile) throws Exception ;
}
