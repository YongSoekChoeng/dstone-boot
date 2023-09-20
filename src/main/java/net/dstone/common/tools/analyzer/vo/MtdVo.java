package net.dstone.common.tools.analyzer.vo;

import java.util.List;

public class MtdVo {

	protected String functionId;
	protected String methodId;
	protected String methodName;
	protected String methodUrl;
	protected String methodBody;
	protected String fileName;

	protected List<String> callMtdVoList;
	protected List<String> callTblVoList;
	
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getMethodId() {
		return methodId;
	}
	public void setMethodId(String methodId) {
		this.methodId = methodId;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getMethodUrl() {
		return methodUrl;
	}
	public void setMethodUrl(String methodUrl) {
		this.methodUrl = methodUrl;
	}
	public String getMethodBody() {
		return methodBody;
	}
	public void setMethodBody(String methodBody) {
		this.methodBody = methodBody;
	}
	public List<String> getCallMtdVoList() {
		return callMtdVoList;
	}
	public void setCallMtdVoList(List<String> callsMtdVoList) {
		this.callMtdVoList = callsMtdVoList;
	}
	public List<String> getCallTblVoList() {
		return callTblVoList;
	}
	public void setCallTblVoList(List<String> callsTblVoList) {
		this.callTblVoList = callsTblVoList;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Override
	public String toString() {
		return "MtdVo [functionId=" + functionId + ", methodId=" + methodId + ", methodName=" + methodName
				+ ", methodUrl=" + methodUrl + ", methodBody=" + methodBody + ", fileName=" + fileName
				+ ", callMtdVoList=" + callMtdVoList + ", callTblVoList=" + callTblVoList + "]";
	}
	
}
