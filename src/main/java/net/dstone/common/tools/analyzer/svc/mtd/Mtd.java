package net.dstone.common.tools.analyzer.svc.mtd;

import java.util.List;
import java.util.Map;

import net.dstone.common.tools.analyzer.vo.ClzzVo;

public interface Mtd {
	/**
	 * 파일로부터 메소드ID/메소드명/메소드URL/메소드내용 이 담긴 메소드정보목록 추출
	 * @param file
	 * @return
	 */
	public List<Map<String, String>> getMtdInfoList(String classFile) throws Exception ;

	/**
	 * 호출메소드 목록 추출
	 * @param methodFile
	 * @return
	 */
	public List<String> getCallMtdList(String methodFile) throws Exception ;
	
	/**
	 * 호출테이블 목록 추출
	 * @param methodFile
	 * @return
	 */
	public List<String> getCallTblList(String methodFile) throws Exception ;
}
