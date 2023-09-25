package net.dstone.common.tools.analyzer.svc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.svc.clzz.Clzz;
import net.dstone.common.tools.analyzer.svc.clzz.impl.DefaultClzz;
import net.dstone.common.tools.analyzer.svc.mtd.Mtd;
import net.dstone.common.tools.analyzer.svc.mtd.impl.DefaultMtd;
import net.dstone.common.tools.analyzer.svc.query.impl.DefaultQuery;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.QueryVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.LogUtil;
import net.dstone.common.utils.StringUtil;

public class SvcAnalyzer extends BaseObject{

	private static ArrayList<String> SRC_FILTER = new ArrayList<String>();
	static {
		SRC_FILTER.add("java");
	}
	private static ArrayList<String> QUERY_FILTER = new ArrayList<String>();
	static {
		QUERY_FILTER.add("xml");
	}
	private static ArrayList<String> WEB_FILTER = new ArrayList<String>();
	static {
		WEB_FILTER.add("jsp");
		WEB_FILTER.add("js");
	}

	private static boolean isValidSvcFile(String file) {
		boolean isValid = false;
		if( FileUtil.isFileExist(file) ) {
			String ext = FileUtil.getFileExt(file);
			if(SRC_FILTER.contains(ext)) {
				isValid = true;
			}
		}
		return isValid;
	}

	private static boolean isValidSvcPackage(String packageId) {
		boolean isValid = false;
		for(String packageRoot : AppAnalyzer.INCLUDE_PACKAGE_ROOT) {
			if( packageId.startsWith(packageRoot) ) {
				isValid = true;
				break;
			}
		}
		if( isValid ) {
			for(String packagePattern : AppAnalyzer.EXCLUDE_PACKAGE_PATTERN) {
				if( packageId.indexOf(packagePattern) > -1) {
					isValid = false;
					break;
				}
			}
		}
		return isValid;
	}
	
	private static boolean isValidQueryFile(String file) {
		boolean isValid = false;
		if( FileUtil.isFileExist(file) ) {
			String ext = FileUtil.getFileExt(file);
			if(QUERY_FILTER.contains(ext)) {
				isValid = true;
			}
		}
		return isValid;
	}

	
	/*********************** Factory 시작 ***********************/
	/**
	 * 클래스 분석 팩토리 클래스
	 * @author jysn007
	 *
	 */
	private static class ClassFactory {
		/**
		 * 패키지ID 추출
		 * @param classFile 클래스파일
		 * @return
		 */
		static String getPackageId(String classFile) throws Exception {
			Clzz clzz = new DefaultClzz();
			return clzz.getPackageId(classFile);
		}
		/**
		 * 클래스ID 추출
		 * @param classFile
		 * @return
		 */
		static String getClassId(String classFile) throws Exception {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassId(classFile);
		}
		/**
		 * 클래스명 추출
		 * @param classFile
		 * @return
		 */
		static String getClassName(String classFile) throws Exception {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassName(classFile);
		}
		/**
		 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
		 * @param classFile
		 * @return
		 */
		static ClzzKind getClassKind(String classFile) throws Exception {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassKind(classFile);
		}
		/**
		 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
		 * @param classFile
		 * @param otherClassFileList
		 * @return
		 */
		static List<Map<String, String>> getCallClassAlias(String classFile, String[] otherClassFileList) throws Exception {
			Clzz clzz = new DefaultClzz();
			return clzz.getCallClassAlias(classFile, otherClassFileList);
		}
	}
	
	/**
	 * 메서드 분석 팩토리 클래스
	 * @author jysn007
	 */
	private static class MethodFactory {
		
		/**
		 * 메서드ID/메서드명/메서드URL/메서드내용 추출
		 * @param classFile
		 * @return
		 */
		static List<Map<String, String>> getMtdInfoList(String classFile) throws Exception {
			Mtd mtd = new DefaultMtd();
			return mtd.getMtdInfoList(classFile);
		}
		
		/**
		 * 호출메소드 목록 추출
		 * @param analyzedMethodFile
		 * @return
		 */
		static List<String> getCallMtdList(String analyzedMethodFile) throws Exception {
			Mtd mtd = new DefaultMtd();
			return mtd.getCallMtdList(analyzedMethodFile);
		}

		/**
		 * 호출테이블 목록 추출
		 * @param methodFile
		 * @return
		 */
		static List<String> getCallTblList(String methodFile) throws Exception {
			Mtd mtd = new DefaultMtd();
			return mtd.getCallTblList(methodFile);
		}

	}

	/**
	 * SQL 분석 팩토리 클래스
	 * @author jysn007
	 */
	private static class QueryFactory {
		
		/**
		 * 쿼리정보 추출
		 * @param classFile
		 * @return
		 */
		static List<Map<String, String>> getQueryInfoList(String queryFile) throws Exception {
			DefaultQuery query = new DefaultQuery();
			return query.getQueryInfoList(queryFile);
		}

		/**
		 * 쿼리정보파일로부터 테이블ID정보목록 추출
		 * @param queryInfoFile
		 * @return
		 */
		static List<String> getCallTblList(String queryInfoFile) throws Exception {
			DefaultQuery query = new DefaultQuery();
			return query.getTblInfoList(queryInfoFile);
		}
		
	}

	/*********************** Factory 끝 ***********************/
	
	public void analyze(int jobKind) {
		String[] 	classFileList = null;				/* 클래스파일리스트 */
		String[] 	queryFileList = null;				/* 쿼리파일리스트 */

		String[] 	analyzedClassFileList = null;		/* 클래스분석파일리스트 */
		String[] 	analyzedQueryFileList = null;		/* 쿼리파분석일리스트 */
		String[] 	analyzedMethodFileList = null;		/* 메소드분석파일리스트 */
		
		ArrayList<String> filteredFileList = null;
		try {

			getLogger().info("/**************************************** A.클래스 분석 시작 ****************************************/");
			getLogger().info("/*** A-1.클래스 파일추출 시작 ***/");
			classFileList = FileUtil.readFileListAll(AppAnalyzer.ROOT_PATH);
			filteredFileList = new ArrayList<String>();
			for(String file : classFileList) {
				if( !isValidSvcPackage(ClassFactory.getPackageId(file)) ) {
					continue;
				}
				filteredFileList.add(file);
			}
			classFileList = new String[filteredFileList.size()];
			filteredFileList.toArray(classFileList);
			filteredFileList.clear();
			filteredFileList = null;
			if(jobKind <= AppAnalyzer.JOB_KIND_11_ANALYZE_CLASS) {return;}
			getLogger().info("/*** A-2.클래스파일리스트 에서 패키지ID/클래스ID/클래스명/기능종류 등이 담긴 클래스분석파일리스트 추출");
			this.analyzeClass(classFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_12_ANALYZE_CLASS_ALIAS) {return;}
			getLogger().info("/*** A-3.클래스파일리스트 에서 호출알리아스 추출하여 클래스분석파일리스트에 추가");
			this.analyzeClassAlias(classFileList);
			getLogger().info("/**************************************** A.클래스 분석 끝 ****************************************/");

			getLogger().info("/**************************************** B.쿼리 분석 시작 ****************************************/");
			getLogger().info("/*** B-1.쿼리 파일추출 시작 ***/");
			queryFileList = FileUtil.readFileListAll(AppAnalyzer.QUERY_ROOT_PATH);
			filteredFileList = new ArrayList<String>();
			for(String file : queryFileList) {
				if( !isValidQueryFile(file) ) {
					continue;
				}
				filteredFileList.add(file);
			}
			queryFileList = new String[filteredFileList.size()];
			filteredFileList.toArray(queryFileList);
			filteredFileList.clear();
			filteredFileList = null;
			if(jobKind <= AppAnalyzer.JOB_KIND_21_ANALYZE_QUERY) {return;}
			getLogger().info("/*** B-2.쿼리파일리스트 에서 KEY/네임스페이스/쿼리ID/쿼리종류/쿼리내용 등이 담긴 쿼리분석파일리스트 추출");
			this.analyzeQuery(queryFileList);
			analyzedQueryFileList = FileUtil.readFileListAll(AppAnalyzer.WRITE_PATH + "/query");
			if(jobKind <= AppAnalyzer.JOB_KIND_22_ANALYZE_QUERY_CALLTBL) {return;}
			getLogger().info("/*** B-3.쿼리분석파일리스트 에 호출테이블ID정보목록 추가");
			this.analyzeQueryCallTbl(analyzedQueryFileList);
			getLogger().info("/**************************************** B.쿼리 분석 끝 ****************************************/");

			getLogger().info("/**************************************** C.메소드 분석 시작 ****************************************/");
			if(jobKind <= AppAnalyzer.JOB_KIND_31_ANALYZE_MTD) {return;}
			getLogger().info("/*** C-1.클래스파일리스트 에서 기능ID/메소드ID/메소드명/메소드URL/메소드내용 등이 담긴 메소드분석파일리스트 추출");
			this.analyzeMtd(classFileList);
			analyzedMethodFileList = FileUtil.readFileListAll(AppAnalyzer.WRITE_PATH + "/method");
			if(jobKind <= AppAnalyzer.JOB_KIND_32_ANALYZE_MTD_CALLMTD) {return;}
			getLogger().info("/*** C-2.메소드분석파일리스트 에 메소드내 타 호출메소드 목록 추가");
			this.analyzeMtdCallMtd(analyzedMethodFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_33_ANALYZE_MTD_CALLTBL) {return;}
			getLogger().info("/*** C-3.메소드분석파일리스트 에 메소드내 호출테이블 목록 추가");
			this.analyzeMtdCallTbl(analyzedMethodFileList);
			getLogger().info("/**************************************** C.메소드 분석 끝 ****************************************/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 클래스파일리스트 에서 패키지ID/클래스ID/클래스명/기능종류 등이 담긴 클래스분석파일리스트 추출
	 * @param classFileList	클래스파일리스트
	 */
	protected void analyzeClass(String[] classFileList)throws Exception {
		ClzzVo clzzVo = null;
		String classFile= "";
		try {
			for(int i=0; i<classFileList.length; i++) {
				classFile = classFileList[i];
				if( isValidSvcFile(classFile) ) {
					
					clzzVo = new ClzzVo();
					
					// 패키지ID
					clzzVo.setPackageId(ClassFactory.getPackageId(classFile));
					
					// 클래스ID
					clzzVo.setClassId(ClassFactory.getClassId(classFile));
					
					// 클래스명
					clzzVo.setClassName(ClassFactory.getClassName(classFile));
					
					// 기능종류
					clzzVo.setClassKind(ClassFactory.getClassKind(classFile));
					
					// 파일명
					clzzVo.setFileName(classFile);
					
					// 파일저장			
					ParseUtil.writeClassVo(clzzVo, AppAnalyzer.WRITE_PATH + "/class");

				}
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeClass()수행중 예외발생. classFile["+classFile+"]");
			e.printStackTrace();
			throw e;
		}

	}
	
	/**
	 * 클래스파일리스트 에서 호출알리아스 추출하여 클래스분석파일리스트에 추가
	 * @param classFileList 클래스파일리스트
	 */
	protected void analyzeClassAlias(String[] classFileList) throws Exception {
		ClzzVo clzzVo = null;
		String pkgClassId = "";
		String classFile= "";
		String[] analyzedClassFileList = null;
		try {
			analyzedClassFileList = FileUtil.readFileList(AppAnalyzer.WRITE_PATH + "/class", false);
			for(int i=0; i<classFileList.length; i++) {
				classFile = classFileList[i];
				if( isValidSvcFile(classFile) ) {
					String fileNoExt = classFile.substring(0, classFile.lastIndexOf("."));
					pkgClassId = StringUtil.replace(fileNoExt, AppAnalyzer.CLASS_ROOT_PATH, "");
					if(pkgClassId.startsWith("/")) {
						pkgClassId = pkgClassId.substring(1);
					}
					pkgClassId = StringUtil.replace(pkgClassId, "/", ".");
					clzzVo = ParseUtil.readClassVo(pkgClassId, AppAnalyzer.WRITE_PATH + "/class");
					
					// 호출알리아스
					clzzVo.setCallClassAlias(ClassFactory.getCallClassAlias(classFile, analyzedClassFileList));
					
					// 파일저장	
					ParseUtil.writeClassVo(clzzVo, AppAnalyzer.WRITE_PATH + "/class");
				}
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeClassAlias()수행중 예외발생. classFile["+classFile+"]");
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 쿼리파일리스트 에서 KEY/네임스페이스/쿼리ID/쿼리종류/쿼리내용 등이 담긴 쿼리분석파일리스트 추출
	 * @param queryFileList 쿼리파일리스트
	 */
	protected void analyzeQuery(String[] queryFileList) throws Exception {
		QueryVo queryVo = null;
		List<Map<String, String>> queryInfoList = null;
		String file= "";
		String key = "";
		try {
			for(int i=0; i<queryFileList.length; i++) {
				file = queryFileList[i];
				if( isValidQueryFile(file) ) {
					// KEY/네임스페이스/쿼리ID/쿼리종류 추출
					queryInfoList = QueryFactory.getQueryInfoList(file);
					if( queryInfoList != null ) {
						for(Map<String, String> queryInfo : queryInfoList) {
							queryVo = new QueryVo();

							// KEY
							if(StringUtil.isEmpty(queryInfo.get("SQL_NAMESPACE"))) {
								key = "NO_NAMESPACE" + "_" + queryInfo.get("SQL_ID");
							}else {
								key = queryInfo.get("SQL_NAMESPACE") + "_" + queryInfo.get("SQL_ID");
							}
							queryVo.setKey(key);
							// 네임스페이스
							queryVo.setNamespace(queryInfo.get("SQL_NAMESPACE"));
							// 쿼리ID
							queryVo.setQueryId(queryInfo.get("SQL_ID"));
							// 쿼리종류
							queryVo.setQueryKind(queryInfo.get("SQL_KIND"));
							// 파일명
							queryVo.setFileName(AppAnalyzer.WRITE_PATH + "/query/" + key + ".txt");
							// 쿼리내용
							queryVo.setQueryBody(queryInfo.get("SQL_BODY"));

							// 파일저장			
							ParseUtil.writeQueryVo(queryVo, AppAnalyzer.WRITE_PATH + "/query");
						}
					}
					
				}
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeQuery()수행중 예외발생. file["+file+"]");
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 쿼리분석파일리스트 에 호출테이블ID정보목록 추출
	 * @param analyzedQueryFileList 쿼리분석파일리스트
	 */
	protected void analyzeQueryCallTbl(String[] analyzedQueryFileList) throws Exception {
		QueryVo queryVo = null;
		String key = "";
		String analyzedQueryFile= "";
		try {
			for(int i=0; i<analyzedQueryFileList.length; i++) {
				analyzedQueryFile = analyzedQueryFileList[i];
				key = FileUtil.getFileName(analyzedQueryFile, false);
				queryVo = ParseUtil.readQueryVo(key, AppAnalyzer.WRITE_PATH + "/query");
				
				// 테이블ID정보목록
				queryVo.setCallTblList(QueryFactory.getCallTblList(analyzedQueryFile));
				
				// 파일저장	
				ParseUtil.writeQueryVo(queryVo, AppAnalyzer.WRITE_PATH + "/query");
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeQueryCallTbl()수행중 예외발생. analyzedQueryFile["+analyzedQueryFile+"]");
			e.printStackTrace();
			throw e;
		}
	}
	

	/**
	 * 클래스파일리스트 에서 기능ID/메소드ID/메소드명/메소드URL/메소드내용 등이 담긴 메소드분석파일리스트 추출
	 * @param classFileList 클래스파일리스트
	 */
	protected void analyzeMtd(String[] classFileList) throws Exception {
		MtdVo mtdVo = null;
		List<Map<String, String>> methodInfoList = null;
		String classFile= "";
		String functionId = "";
		try {
			for(int i=0; i<classFileList.length; i++) {
				classFile = classFileList[i];
				if( isValidSvcFile(classFile) ) {
					// 메소드ID/메소드명/메소드URL/메소드내용 추출
					methodInfoList = MethodFactory.getMtdInfoList(classFile);
					if( methodInfoList != null ) {
						for(Map<String, String> methodInfo : methodInfoList) {
							mtdVo = new MtdVo();

							// 기능ID
							functionId = ClassFactory.getPackageId(classFile) + "." + ClassFactory.getClassId(classFile) + "." + methodInfo.get("METHOD_ID");
							mtdVo.setFunctionId(functionId);
							// 메소드ID
							mtdVo.setMethodId(methodInfo.get("METHOD_ID"));
							// 메소드명
							mtdVo.setMethodName(methodInfo.get("METHOD_NAME"));
							// 메소드URL
							mtdVo.setMethodUrl(methodInfo.get("METHOD_URL"));
							// 파일명
							mtdVo.setFileName(AppAnalyzer.WRITE_PATH + "/method/" + functionId + ".txt");
							// 메소드내용
							mtdVo.setMethodBody(methodInfo.get("METHOD_BODY"));

							// 파일저장			
							ParseUtil.writeMethodVo(mtdVo, AppAnalyzer.WRITE_PATH + "/method");
						}
					}
					
				}
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeMtd()수행중 예외발생. classFile["+classFile+"]");
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 메소드내 타 호출메소드 목록 추출
	 * @param analyzedMethodFileList 메소드분석파일리스트
	 */
	protected void analyzeMtdCallMtd(String[] analyzedMethodFileList) throws Exception {
		MtdVo mtdVo = null;
		String functionId = "";
		String analyzedMethodFile = "";
		
		try {
			if(analyzedMethodFileList != null) {
				for(int i=0; i<analyzedMethodFileList.length; i++) {
					analyzedMethodFile = analyzedMethodFileList[i];
					String fileNoExt = analyzedMethodFile.substring(0, analyzedMethodFile.lastIndexOf("."));
					functionId = StringUtil.replace(fileNoExt, AppAnalyzer.WRITE_PATH + "/method", "");
					if(functionId.startsWith("/")) {
						functionId = functionId.substring(1);
					}
					functionId = StringUtil.replace(functionId, "/", ".");
					mtdVo = ParseUtil.readMethodVo(functionId, AppAnalyzer.WRITE_PATH + "/method");
					
					// 호출메소드
					mtdVo.setCallMtdVoList(MethodFactory.getCallMtdList(analyzedMethodFile));
					
					// 파일저장	
					ParseUtil.writeMethodVo(mtdVo, AppAnalyzer.WRITE_PATH + "/method");
				}
			}

		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeMtdCallMtd()수행중 예외발생. analyzedMethodFile["+analyzedMethodFile+"]");
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * 메소드내 호출테이블 목록 추출
	 * @param analyzedMethodFileList 메소드분석파일리스트
	 */
	protected void analyzeMtdCallTbl(String[] analyzedMethodFileList) throws Exception {
		MtdVo mtdVo = null;
		String functionId = "";
		String analyzedMethodFile = "";
		
		try {
			if(analyzedMethodFileList != null) {
				for(int i=0; i<analyzedMethodFileList.length; i++) {
					analyzedMethodFile = analyzedMethodFileList[i];
					String fileNoExt = analyzedMethodFile.substring(0, analyzedMethodFile.lastIndexOf("."));
					functionId = StringUtil.replace(fileNoExt, AppAnalyzer.WRITE_PATH + "/method", "");
					if(functionId.startsWith("/")) {
						functionId = functionId.substring(1);
					}
					functionId = StringUtil.replace(functionId, "/", ".");
					mtdVo = ParseUtil.readMethodVo(functionId, AppAnalyzer.WRITE_PATH + "/method");
					
					// 호출테이블
					mtdVo.setCallTblVoList(MethodFactory.getCallTblList(analyzedMethodFile));
					
					// 파일저장	
					ParseUtil.writeMethodVo(mtdVo, AppAnalyzer.WRITE_PATH + "/method");
				}
			}

		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeMtdCallTbl()수행중 예외발생. analyzedMethodFile["+analyzedMethodFile+"]");
			e.printStackTrace();
			throw e;
		}
	}
	
}
