package net.dstone.common.tools.analyzer.svc.mtd.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.svc.mtd.Mtd;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.QueryVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class DefaultMtd extends BaseObject implements Mtd {

	/**
	 * 파일로부터 메소드ID/메소드명/메소드URL/메소드내용 이 담긴 메소드정보목록 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public List<Map<String, String>> getMtdInfoList(String classFile) {
		String fileConts = FileUtil.readFile(classFile);
		fileConts = ParseUtil.adjustConts(fileConts);
		return ParseUtil.getMtdListFromJava(fileConts);
	}

	/**
	 * 호출메소드 목록 추출
	 * @param clzzVo
	 * @param methodFile
	 * @return
	 */
	@Override
	public List<String> getCallMtdList(String methodFile) {
		List<String> callsMtdList = new ArrayList<String>();
		
		// 메소드VO 정보 획득
		String functionId = FileUtil.getFileName(methodFile, false);
		MtdVo mtdVo = ParseUtil.readMethodVo(functionId, AppAnalyzer.WRITE_PATH + "/method");

		// 클래스VO 정보 획득
		String packageClassId = functionId.substring(0, functionId.lastIndexOf("."));
		ClzzVo clzzVo = ParseUtil.readClassVo(packageClassId, AppAnalyzer.WRITE_PATH + "/class");
		
		// 클래스 호출알리아스 정보
		List<Map<String, String>> callClassAliasList = clzzVo.getCallClassAlias();
		
		String mtdBody = "";
		String callPackageClassId = "";
		String callAlias = "";
		String keyword = "";
		String callMtd = "";
		String[] div = {"("};
		
		if( !StringUtil.isEmpty(mtdVo.getMethodBody()) ) {
			mtdBody = mtdVo.getMethodBody();
			String[] lines = StringUtil.toStrArray(mtdBody, "\n");
			for(String line : lines) {
				keyword = "";
				callMtd = "";
				if(callClassAliasList != null) {
					for(Map<String, String> callClassAlias : callClassAliasList) {
						callPackageClassId = callClassAlias.get("FULL_CLASS");
						callAlias = callClassAlias.get("ALIAS");
						keyword =  callAlias+ "." ;
						// 클래스 호출알리아스+'.' 이후에 시작되는 메소드명 추출.(스페이스 이후에 키워드로 시작되거나 스페이스없이 키워드로 시작되는 라인이 있으면 검색)
						if( line.indexOf(" " + keyword)>-1 || line.startsWith(keyword) ) {					
							callMtd = callPackageClassId + "." + StringUtil.nextWord(line, keyword, div);
							break;
						}
					}
				}
				if( !StringUtil.isEmpty(callMtd) ) {
					callsMtdList.add(callMtd);
				}
			}
		}
		
		return callsMtdList;
	}

	/**
	 * 호출테이블 목록 추출
	 * @param methodFile
	 * @return
	 */
	@Override
	public List<String> getCallTblList(String methodFile) {
		List<String> callTblList = new ArrayList<String>();
		
		// 메소드VO 정보 획득
		String functionId = FileUtil.getFileName(methodFile, false);
		MtdVo mtdVo = ParseUtil.readMethodVo(functionId, AppAnalyzer.WRITE_PATH + "/method");

		// 쿼리목록 정보 획득
		String[] queryFileArr = FileUtil.readFileList(AppAnalyzer.WRITE_PATH + "/query", false);
		List<String> queryKeyList = new ArrayList<String>();
		if(queryFileArr != null) {
			for(String item : queryFileArr) {
				queryKeyList.add(item);
			}
		}

		QueryVo queryVo = null;
		String mtdBody = "";
		String keyword = "";
		
		if( !StringUtil.isEmpty(mtdVo.getMethodBody()) ) {
			mtdBody = mtdVo.getMethodBody();
			String[] lines = StringUtil.toStrArray(mtdBody, "\n");
			for(String line : lines) {
				keyword = "";
				for(String queryKey : queryKeyList) {
					keyword = queryKey;
					if( keyword.indexOf("_")>-1 ) {
						keyword = keyword.substring(0, keyword.lastIndexOf("_")) + "." + keyword.substring(keyword.lastIndexOf("_")+1);
					}
					keyword = "\"" + keyword + "\"";
					if( line.indexOf(keyword) > -1 ) {
						queryVo = ParseUtil.readQueryVo(queryKey, AppAnalyzer.WRITE_PATH + "/query");
						if(queryVo != null && queryVo.getCallTblList() != null && queryVo.getCallTblList().size() > 0) {
							for(String callTbl : queryVo.getCallTblList()) {
								if( !callTblList.contains(callTbl) ) {
									callTblList.add(callTbl);
								}
							}
						}
						break;
					}
				}
			}
		}

		return callTblList;
	}

}
