package net.dstone.common.tools.analyzer.svc.clzz;

import java.util.List;
import java.util.Map;

import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.vo.MtdVo;

public interface Clzz {
	/**
	 * 패키지ID 추출
	 * @param src
	 * @return
	 */
	public String getPackageId(String src);
	/**
	 * 클래스ID 추출
	 * @param src
	 * @return
	 */
	public String getClassId(String src);
	/**
	 * 클래스명 추출
	 * @param src
	 * @return
	 */
	public String getClassName(String src);
	/**
	 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
	 * @param src
	 * @return
	 */
	public ClzzKind getClassKind(String src);
	/**
	 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
	 * @param src
	 * @return
	 */
	public List<Map<String, String>> getCallClassAlias(String src);
}