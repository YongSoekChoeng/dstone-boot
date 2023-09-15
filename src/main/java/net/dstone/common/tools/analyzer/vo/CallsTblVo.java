package net.dstone.common.tools.analyzer.vo;

public class CallsTblVo {

	protected String functionId;
	protected String callTblId;
	public String getFunctionId() {
		return functionId;
	}
	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}
	public String getCallTblId() {
		return callTblId;
	}
	public void setCallTblId(String callTblId) {
		this.callTblId = callTblId;
	}
	@Override
	public String toString() {
		return "CallsTblVo [functionId=" + functionId + ", callTblId=" + callTblId + "]";
	}
	
}
