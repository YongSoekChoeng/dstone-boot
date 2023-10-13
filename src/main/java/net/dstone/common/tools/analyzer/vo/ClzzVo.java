package net.dstone.common.tools.analyzer.vo;

import java.util.List;
import java.util.Map;

import net.dstone.common.tools.analyzer.consts.ClzzKind;

public class ClzzVo {
	
	protected String packageId;
	protected String classId;
	protected String className;
	protected ClzzKind classKind; /* 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) */
	protected String resourceId; /* 어노테이션으로 표현된 리소스ID */
	protected String classOrInterface; /* 클래스or인터페이스(C:클래스/I:인터페이스) */
	protected String interfaceId; /* 인터페이스ID. 인터페이스를 구현한 경우에만 존재. */
	protected String fileName;
	private List<Map<String, String>> callClassAlias;
	
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
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
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getClassOrInterface() {
		return classOrInterface;
	}
	public void setClassOrInterface(String classOrInterface) {
		this.classOrInterface = classOrInterface;
	}
	public String getInterfaceId() {
		return interfaceId;
	}
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
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
		return "ClzzVo [packageId=" + packageId + ", classId=" + classId + ", className=" + className + ", classKind="
				+ classKind + ", resourceId=" + resourceId + ", classOrInterface=" + classOrInterface + ", interfaceId="
				+ interfaceId + ", fileName=" + fileName + ", callClassAlias=" + callClassAlias + "]";
	}
	
}
