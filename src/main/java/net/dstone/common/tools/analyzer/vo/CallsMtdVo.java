package net.dstone.common.tools.analyzer.vo;

public class CallsMtdVo {

	protected String functionId;
	protected String callFunctionId;
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getCallFunctionId() {
		return callFunctionId;
	}
	public void setCallFunctionId(String callFunctionId) {
		this.callFunctionId = callFunctionId;
	}
	@Override
	public String toString() {
		return "CallsMtdVo [functionId=" + functionId + ", callFunctionId=" + callFunctionId + "]";
	}
	
}
