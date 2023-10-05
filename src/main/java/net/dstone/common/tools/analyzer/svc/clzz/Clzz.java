package net.dstone.common.tools.analyzer.svc.clzz;

import java.util.List;
import java.util.Map;

import net.dstone.common.tools.analyzer.consts.ClzzKind;

public interface Clzz {
	/**
	 * 패키지ID 추출
	 * @param classFile 클래스파일
	 * @return
	 */
	public String getPackageId(String classFile) throws Exception ;
	/**
	 * 클래스ID 추출
	 * @param classFile
	 * @return
	 */
	public String getClassId(String classFile) throws Exception ;
	/**
	 * 클래스명 추출
	 * @param classFile
	 * @return
	 */
	public String getClassName(String classFile) throws Exception ;
	/**
	 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
	 * @param classFile
	 * @return
	 */
	public ClzzKind getClassKind(String classFile) throws Exception ;
	/**
	 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
	 * @param classFile
	 * @param otherClassFileList
	 * @return
	 */
	public List<Map<String, String>> getCallClassAlias(String classFile, String[] otherClassFileList) throws Exception ;
}
