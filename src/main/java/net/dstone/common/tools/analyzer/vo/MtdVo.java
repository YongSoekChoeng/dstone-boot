package net.dstone.common.tools.analyzer.vo;

import java.util.List;

public class MtdVo {

	protected String functionId;
	protected String methodId;
	protected String methodName;
	protected String methodUrl;
	protected String methodBody;

	protected List<CallsMtdVo> callsMtdVoList;
	protected List<CallsTblVo> callsTblVoList;
	
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
	public List<CallsMtdVo> getCallsMtdVoList() {
		return callsMtdVoList;
	}
	public void setCallsMtdVoList(List<CallsMtdVo> callsMtdVoList) {
		this.callsMtdVoList = callsMtdVoList;
	}
	public List<CallsTblVo> getCallsTblVoList() {
		return callsTblVoList;
	}
	public void setCallsTblVoList(List<CallsTblVo> callsTblVoList) {
		this.callsTblVoList = callsTblVoList;
	}
	
	@Override
	public String toString() {
		return "MtdVo [functionId=" + functionId + ", methodId=" + methodId + ", methodName=" + methodName
				+ ", methodUrl=" + methodUrl + ", methodBody=" + methodBody + ", callsMtdVoList=" + callsMtdVoList
				+ ", callsTblVoList=" + callsTblVoList + "]";
	}
	
}
