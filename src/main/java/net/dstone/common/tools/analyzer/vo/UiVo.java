package net.dstone.common.tools.analyzer.vo;

import java.util.List;

import net.dstone.common.biz.BaseVo;

public class UiVo extends BaseVo {
	private String uiId;
	private String uiName;
	protected String fileName;

	protected List<String> includeUiFileNameList;
	protected List<String> linkList;
	
	public String getUiId() {
		return uiId;
	}
	public void setUiId(String uiId) {
		this.uiId = uiId;
	}
	public String getUiName() {
		return uiName;
	}
	public void setUiName(String uiName) {
		this.uiName = uiName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<String> getIncludeUiFileNameList() {
		return includeUiFileNameList;
	}
	public void setIncludeUiFileNameList(List<String> includeUiFileNameList) {
		this.includeUiFileNameList = includeUiFileNameList;
	}
	public List<String> getLinkList() {
		return linkList;
	}
	public void setLinkList(List<String> linkList) {
		this.linkList = linkList;
	}
	@Override
	public String toString() {
		return "UiVo [uiId=" + uiId + ", uiName=" + uiName + ", fileName=" + fileName + ", includeUiFileNameList="
				+ includeUiFileNameList + ", linkList=" + linkList + "]";
	}
}
