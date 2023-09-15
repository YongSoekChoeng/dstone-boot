package net.dstone.common.tools.analyzer.vo;

import java.util.List;
import java.util.Map;

import net.dstone.common.tools.analyzer.consts.ClzzKind;

public class ClzzVo {
	
	protected String pkg;
	protected String classId;
	protected String className;
	protected ClzzKind classKind; /* 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */
	protected String fileName;
	private List<Map<String, String>> callClassAlias;

	public String getPkg() {
		return pkg;
	}
	public void setPkg(String pkg) {
		this.pkg = pkg;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public ClzzKind getClassKind() {
		return classKind;
	}
	public void setClassKind(ClzzKind classKind) {
		this.classKind = classKind;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public List<Map<String, String>> getCallClassAlias() {
		return callClassAlias;
	}
	public void setCallClassAlias(List<Map<String, String>> callClassAlias) {
		this.callClassAlias = callClassAlias;
	}
	@Override
	public String toString() {
		return "ClzzVo [pkg=" + pkg + ", classId=" + classId + ", className=" + className + ", classKind=" + classKind + ", fileName="
				+ fileName + ", callClassAlias=" + callClassAlias + "]";
	}
}
