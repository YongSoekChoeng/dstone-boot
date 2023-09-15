package net.dstone.common.tools.analyzer.vo;

public class TblVo {
	private String tblId;
	private String tblName;
	public String getTblId() {
		return tblId;
	}
	public void setTblId(String tblId) {
		this.tblId = tblId;
	}
	public String getTblName() {
		return tblName;
	}
	public void setTblName(String tblName) {
		this.tblName = tblName;
	}
	@Override
	public String toString() {
		return "TblVo [tblId=" + tblId + ", tblName=" + tblName + "]";
	}
}
