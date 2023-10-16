package net.dstone.common.tools.analyzer.svc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.svc.clzz.Clzz;
import net.dstone.common.tools.analyzer.svc.clzz.impl.JavaParserClzz;
import net.dstone.common.tools.analyzer.svc.mtd.Mtd;
import net.dstone.common.tools.analyzer.svc.mtd.impl.JavaParserMtd;
import net.dstone.common.tools.analyzer.svc.query.Query;
import net.dstone.common.tools.analyzer.svc.query.impl.DefaultQuery;
import net.dstone.common.tools.analyzer.svc.ui.Ui;
import net.dstone.common.tools.analyzer.svc.ui.impl.DefaultUi;
import net.dstone.common.tools.analyzer.util.DbGen;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.QueryVo;
import net.dstone.common.tools.analyzer.vo.UiVo;
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
	private static ArrayList<String> UI_FILTER = new ArrayList<String>();
	static {
		UI_FILTER.add("jsp");
		//UI_FILTER.add("js");
	}

	public static boolean isValidSvcFile(String file) {
		boolean isValid = false;
		if( FileUtil.isFileExist(file) ) {
			String ext = FileUtil.getFileExt(file);
			if(SRC_FILTER.contains(ext)) {
				isValid = true;
			}
		}
		return isValid;
	}

	public static boolean isValidSvcPackage(String packageId) {
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
	
	public static boolean isValidQueryFile(String file) {
		boolean isValid = false;
		if( FileUtil.isFileExist(file) ) {
			String ext = FileUtil.getFileExt(file);
			if(QUERY_FILTER.contains(ext)) {
				isValid = true;
			}
		}
		return isValid;
	}
	
	public static boolean isValidUiFile(String file) {
		boolean isValid = false;
		if( FileUtil.isFileExist(file) ) {
			String ext = FileUtil.getFileExt(file);
			if(UI_FILTER.contains(ext)) {
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
		
		static Clzz getClzz() {
			//return new DefaultClzz(); 
			return new JavaParserClzz(); 
		}
		
		/**
		 * 패키지ID 추출
		 * @param classFile 클래스파일
		 * @return
		 */
		static String getPackageId(String classFile) throws Exception {
			return getClzz().getPackageId(classFile);
		}
		/**
		 * 클래스ID 추출
		 * @param classFile
		 * @return
		 */
		static String getClassId(String classFile) throws Exception {
			return getClzz().getClassId(classFile);
		}
		/**
		 * 클래스명 추출
		 * @param classFile
		 * @return
		 */
		static String getClassName(String classFile) throws Exception {
			return getClzz().getClassName(classFile);
		}
		/**
		 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
		 * @param classFile
		 * @return
		 */
		static ClzzKind getClassKind(String classFile) throws Exception {
			return getClzz().getClassKind(classFile);
		}
		/**
		 * 리소스ID 추출
		 * @param classFile
		 * @return
		 */
		static String getResourceId(String classFile) throws Exception {
			return getClzz().getResourceId(classFile);
		}
		
		/**
		 * 클래스or인터페이스(C:클래스/I:인터페이스) 추출
		 * @param classFile
		 * @return
		 */
		static String getClassOrInterface(String classFile) throws Exception {
			return getClzz().getClassOrInterface(classFile);
		}

		/**
		 * 상위인터페이스ID 추출(인터페이스를 구현한 클래스의 경우에만 존재)
		 * @param classFile
		 * @return
		 */
		static String getInterfaceId(String classFile) throws Exception {
			return getClzz().getInterfaceId(classFile);
		}
		/**
		 * 인터페이스구현하위클래스ID목록(인터페이스인 경우에만 존재)
		 * @param selfClzzVo
		 * @param otherClassFileList
		 * @return
		 */
		static List<String> getImplClassIdList(ClzzVo selfClzzVo, String[] otherClassFileList) throws Exception {
			return getClzz().getImplClassIdList(selfClzzVo, otherClassFileList);
		}
		/**
		 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
		 * @param selfClzzVo
		 * @param otherClassFileList
		 * @return
		 */
		static List<Map<String, String>> getCallClassAlias(ClzzVo selfClzzVo, String[] otherClassFileList) throws Exception {
			return getClzz().getCallClassAlias(selfClzzVo, otherClassFileList);
		}
	}
	
	/**
	 * 메서드 분석 팩토리 클래스
	 * @author jysn007
	 */
	private static class MethodFactory {

		static Mtd getMethod() {
			//return new DefaultMtd(); 
			return new JavaParserMtd();
		}
		
		/**
		 * 메서드ID/메서드명/메서드URL/메서드내용 추출
		 * @param classFile
		 * @return
		 */
		static List<Map<String, String>> getMtdInfoList(String classFile) throws Exception {
			return getMethod().getMtdInfoList(classFile);
		}
		
		/**
		 * 호출메소드 목록 추출
		 * @param analyzedMethodFile
		 * @return
		 */
		static List<String> getCallMtdList(String analyzedMethodFile) throws Exception {
			return getMethod().getCallMtdList(analyzedMethodFile);
		}

		/**
		 * 호출테이블 목록 추출
		 * @param methodFile
		 * @return
		 */
		static List<String> getCallTblList(String methodFile) throws Exception {
			return getMethod().getCallTblList(methodFile);
		}

	}

	/**
	 * SQL 분석 팩토리 클래스
	 * @author jysn007
	 */
	private static class QueryFactory {

		static Query getQuery() {
			return new DefaultQuery(); 
		}
		
		/**
		 * 쿼리정보 추출
		 * @param classFile
		 * @return
		 */
		static List<Map<String, String>> getQueryInfoList(String queryFile) throws Exception {
			return getQuery().getQueryInfoList(queryFile);
		}

		/**
		 * 쿼리정보파일로부터 테이블ID정보목록 추출
		 * @param queryInfoFile
		 * @return
		 */
		static List<String> getCallTblList(String queryInfoFile) throws Exception {
			return getQuery().getTblInfoList(queryInfoFile);
		}
		
	}

	/**
	 * UI 분석 팩토리 클래스
	 * @author jysn007
	 */
	private static class UiFactory {

		static Ui getUi() {
			return new DefaultUi(); 
		}
		
		/**
		 * UI파일로부터 UI아이디 추출
		 * @param uiFile
		 * @return
		 */
		static String getUiId(String uiFile) throws Exception{
			uiFile = StringUtil.replace(uiFile, "\\", "/");
			String uiId = StringUtil.replace( FileUtil.getFileName(uiFile, false),  AppAnalyzer.CLASS_ROOT_PATH, "");
			if(uiId.startsWith("/")) {
				uiId = uiId.substring(1);
			}
			uiId = StringUtil.replace(uiId,  "/", ".");
			return uiId;
		}
		/**
		 * UI파일로부터 UI명 추출
		 * @param uiFile
		 * @return
		 */
		static String getUiName(String uiFile) throws Exception{
			return getUi().getUiName(uiFile);
		}
		/**
		 * UI파일로부터 링크목록 추출
		 * @param uiFile
		 * @return
		 */
		static List<String> getUiLinkList(String uiFile) throws Exception {
			List<String> composeLinkList = new ArrayList<String>();
			
			composeLinkList = getUi().getUiLinkList(uiFile);

			return composeLinkList.stream().distinct().collect(Collectors.toList());
		}

	}

	/*********************** Factory 끝 ***********************/
	
	@SuppressWarnings("unused")
	public void analyze(int jobKind) {
		String[] 	classFileList = null;				/* 클래스파일리스트 */
		String[] 	queryFileList = null;				/* 쿼리파일리스트 */
		String[] 	uiFileList = null;					/* UI파일리스트 */

		String[] 	analyzedClassFileList = null;		/* 클래스분석파일리스트 */
		String[] 	analyzedQueryFileList = null;		/* 쿼리파분석일리스트 */
		String[] 	analyzedMethodFileList = null;		/* 메소드분석파일리스트 */
		String[] 	analyzedUiFileList = null;			/* UI분석파일리스트 */
		
		ArrayList<String> filteredFileList = null;
		try {
			
			getLogger().info("/**************************************** A.클래스 분석 시작 ****************************************/");
			getLogger().info("/*** A-1.클래스 파일추출 시작 ***/");
			classFileList = FileUtil.readFileListAll(AppAnalyzer.ROOT_PATH);
			filteredFileList = new ArrayList<String>();
			for(String file : classFileList) {
				if( !isValidSvcFile(file) ||  !isValidSvcPackage(ClassFactory.getPackageId(file)) ) {
					continue;
				}
				filteredFileList.add(file);
			}
			classFileList = new String[filteredFileList.size()];
			filteredFileList.toArray(classFileList);
			filteredFileList.clear();
			filteredFileList = null;
			
			getLogger().info("/*** A-2.클래스파일리스트 에서 패키지ID/클래스ID/클래스명/기능종류 등이 담긴 클래스분석파일리스트 추출");
			this.analyzeClass(classFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_11_ANALYZE_CLASS) {return;}
			
			getLogger().info("/*** A-3.클래스파일리스트 에서 인터페이스구현하위클래스ID목록을 추출하여 클래스분석파일리스트에 추가");
			this.analyzeClassImpl(classFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_12_ANALYZE_CLASS_IMPL) {return;}
			
			getLogger().info("/*** A-4.클래스파일리스트 에서 호출알리아스를 추출하여 클래스분석파일리스트에 추가");
			this.analyzeClassAlias(classFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_13_ANALYZE_CLASS_ALIAS) {return;}
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
			
			getLogger().info("/*** B-2.쿼리파일리스트 에서 KEY/네임스페이스/쿼리ID/쿼리종류/쿼리내용 등이 담긴 쿼리분석파일리스트 추출");
			this.analyzeQuery(queryFileList);
			analyzedQueryFileList = FileUtil.readFileListAll(AppAnalyzer.WRITE_PATH + "/query");
			if(jobKind <= AppAnalyzer.JOB_KIND_21_ANALYZE_QUERY) {return;}
			
			getLogger().info("/*** B-3.쿼리분석파일리스트 에 호출테이블ID정보목록 추가");
			this.analyzeQueryCallTbl(analyzedQueryFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_22_ANALYZE_QUERY_CALLTBL) {return;}
			getLogger().info("/**************************************** B.쿼리 분석 끝 ****************************************/");
			
			getLogger().info("/**************************************** C.메소드 분석 시작 ****************************************/");
			getLogger().info("/*** C-1.클래스파일리스트 에서 기능ID/메소드ID/메소드명/메소드URL/메소드내용 등이 담긴 메소드분석파일리스트 추출");
			this.analyzeMtd(classFileList);
			analyzedMethodFileList = FileUtil.readFileListAll(AppAnalyzer.WRITE_PATH + "/method");
			if(jobKind <= AppAnalyzer.JOB_KIND_31_ANALYZE_MTD) {return;}
			
			getLogger().info("/*** C-2.메소드분석파일리스트 에 메소드내 타 호출메소드 목록 추가");
			this.analyzeMtdCallMtd(analyzedMethodFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_32_ANALYZE_MTD_CALLMTD) {return;}
			
			getLogger().info("/*** C-3.메소드분석파일리스트 에 메소드내 호출테이블 목록 추가");
			this.analyzeMtdCallTbl(analyzedMethodFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_33_ANALYZE_MTD_CALLTBL) {return;}
			getLogger().info("/**************************************** C.메소드 분석 끝 ****************************************/");
			
			getLogger().info("/**************************************** D.UI 분석 시작 ****************************************/");
			getLogger().info("/*** D-1.UI 파일추출 시작 ***/");
			uiFileList = FileUtil.readFileListAll(AppAnalyzer.ROOT_PATH);
			filteredFileList = new ArrayList<String>();
			for(String file : uiFileList) {
				if( !isValidUiFile(file) ) {
					continue;
				}
				filteredFileList.add(file);
			}
			uiFileList = new String[filteredFileList.size()];
			filteredFileList.toArray(uiFileList);
			filteredFileList.clear();
			filteredFileList = null;
			
			getLogger().info("/*** D-2.UI파일로부터 UI아이디/UI명 등이 담긴 UI분석파일목록 추출");
			this.analyzeUi(uiFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_41_ANALYZE_UI) {return;}
			
			getLogger().info("/*** D-3.UI파일로부터 링크 추출");
			this.analyzeUiLink(uiFileList);
			if(jobKind <= AppAnalyzer.JOB_KIND_42_ANALYZE_UI_LINK) {return;}
			getLogger().info("/**************************************** D.UI 분석 끝 ****************************************/");
			
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

					// 리소스ID
					clzzVo.setResourceId(ClassFactory.getResourceId(classFile));

					// 클래스or인터페이스
					clzzVo.setClassOrInterface(ClassFactory.getClassOrInterface(classFile));

					// 인터페이스ID
					clzzVo.setInterfaceId(ClassFactory.getInterfaceId(classFile));

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
	 * 클래스파일리스트 에서 해당클래스파일이 인터페이스인 경우 인터페이스구현하위클래스ID목록을 추출하여 클래스분석파일리스트에 추가
	 * @param classFileList 클래스파일리스트
	 */
	protected void analyzeClassImpl(String[] classFileList) throws Exception {
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
					
					if( "I".equals(clzzVo.getClassOrInterface()) ) {
						
						// 인터페이스구현하위클래스ID목록
						clzzVo.setImplClassIdList(ClassFactory.getImplClassIdList(clzzVo, analyzedClassFileList));
						
						// 파일저장	
						ParseUtil.writeClassVo(clzzVo, AppAnalyzer.WRITE_PATH + "/class");
					}

				}
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeClassAlias()수행중 예외발생. classFile["+classFile+"]");
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
					clzzVo.setCallClassAlias(ClassFactory.getCallClassAlias(clzzVo, analyzedClassFileList));
					
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
							functionId = ClassFactory.getClassId(classFile) + "." + methodInfo.get("METHOD_ID");
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
	
	/**
	 * UI파일리스트 에서 UI아이디/UI명 등이 담긴 UI분석파일리스트 추출
	 * @param uiFileList UI파일리스트
	 */
	protected void analyzeUi(String[] uiFileList) throws Exception {
		UiVo uiVo = null;
		String uiFile= "";
		try {
			for(int i=0; i<uiFileList.length; i++) {
				uiFile = StringUtil.replace(uiFileList[i], "\\", "/");
				if( isValidUiFile(uiFile) ) {
					
					// UI아이디/UI명/인크루드파일/링크 추출
					uiVo = new UiVo();
					
					// UI아이디
					uiVo.setUiId(UiFactory.getUiId(uiFile));
					
					// UI명
					uiVo.setUiName(UiFactory.getUiName(uiFile));

					// 파일명
					uiVo.setFileName(uiFile);
					
					// 파일저장			
					ParseUtil.writeUiVo(uiVo, AppAnalyzer.WRITE_PATH + "/ui");
					
				}
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeUi()수행중 예외발생. uiFile["+uiFile+"]");
			e.printStackTrace();
			throw e;
		}
	}
	/**
	 * UI파일리스트 에서 링크정보를 추출하여 UI분석파일리스트 에 추가
	 * @param uiFileList UI파일리스트
	 */
	protected void analyzeUiLink(String[] uiFileList) throws Exception {
		UiVo uiVo = null;
		String uiFile= "";
		try {
			for(int i=0; i<uiFileList.length; i++) {
				uiFile = StringUtil.replace(uiFileList[i], "\\", "/");
				if( isValidUiFile(uiFile) ) {
					
					// UI Vo
					uiVo = ParseUtil.readUiVo(UiFactory.getUiId(uiFile), AppAnalyzer.WRITE_PATH + "/ui");
					
					// 링크
					uiVo.setLinkList(UiFactory.getUiLinkList(uiFile));

					// 파일저장			
					ParseUtil.writeUiVo(uiVo, AppAnalyzer.WRITE_PATH + "/ui");
					
				}
			}
		} catch (Exception e) {
			LogUtil.sysout(this.getClass().getName() + ".analyzeUi()수행중 예외발생. uiFile["+uiFile+"]");
			e.printStackTrace();
			throw e;
		}
	}
	
	public void saveToDb(String DBID) {
		try {
			getLogger().info("/**************************************** F-1.기존데이터삭제 시작 ****************************************/");
			this.deleteFromDb(DBID);
			getLogger().info("/**************************************** F-1.기존데이터삭제 끝 ****************************************/");

			getLogger().info("/**************************************** F-2.데이터적재 시작 ****************************************/");
			this.insertToDb(DBID);
			getLogger().info("/**************************************** F-2.데이터적재 끝 ****************************************/");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void deleteFromDb(String DBID) throws Exception {
		DbGen.deleteAll(DBID);
	}

	private void insertToDb(String DBID) throws Exception {
		String[] fileList = null;
		String subPath = "";
		ClzzVo clzzVo = null;
		MtdVo mtdVo = null;
		UiVo uiVo = null;
		
		try {
			// 클래스
			getLogger().info("/*** F-2-1.클래스 데이터적재 시작 ***/");
			subPath = AppAnalyzer.WRITE_PATH + "/class";
			fileList = FileUtil.readFileList(subPath, false);
			if(fileList != null) {
				for(String file : fileList) {
					clzzVo = ParseUtil.readClassVo(file, subPath);
					DbGen.insertTB_CLZZ(DBID, clzzVo);
				}
			}

			// 기능메서드
			getLogger().info("/*** F-2-2.기능메서드 데이터적재 시작 ***/");
			subPath = AppAnalyzer.WRITE_PATH + "/method";
			fileList = FileUtil.readFileList(subPath, false);
			if(fileList != null) {
				for(String file : fileList) {
					mtdVo = ParseUtil.readMethodVo(file, subPath);
					DbGen.insertTB_FUNC(DBID, mtdVo);
				}
			}

			// 테이블
			getLogger().info("/*** F-2-3.테이블 데이터적재 시작 ***/");
			DbGen.insertTB_TBL(DBID);
			
			// 기능간맵핑
			getLogger().info("/*** F-2-4.기능간맵핑 데이터적재 시작 ***/");
			subPath = AppAnalyzer.WRITE_PATH + "/method";
			fileList = FileUtil.readFileList(subPath, false);
			if(fileList != null) {
				for(String file : fileList) {
					mtdVo = ParseUtil.readMethodVo(file, subPath);
					DbGen.insertTB_FUNC_FUNC_MAPPING(DBID, mtdVo);
				}
			}

			// 테이블맵핑
			getLogger().info("/*** F-2-5.테이블맵핑 데이터적재 시작 ***/");
			subPath = AppAnalyzer.WRITE_PATH + "/method";
			fileList = FileUtil.readFileList(subPath, false);
			if(fileList != null) {
				for(String file : fileList) {
					mtdVo = ParseUtil.readMethodVo(file, subPath);
					DbGen.insertTB_FUNC_TBL_MAPPING(DBID, mtdVo);
				}
			}

			// 화면
			getLogger().info("/*** F-2-6.화면 데이터적재 시작 ***/");
			subPath = AppAnalyzer.WRITE_PATH + "/ui";
			fileList = FileUtil.readFileList(subPath, false);
			if(fileList != null) {
				for(String file : fileList) {
					uiVo = ParseUtil.readUiVo(file, subPath);
					DbGen.insertTB_UI(DBID, uiVo);
				}
			}

			// 화면기능맵핑
			getLogger().info("/*** F-2-7.화면기능맵핑 데이터적재 시작 ***/");
			subPath = AppAnalyzer.WRITE_PATH + "/ui";
			fileList = FileUtil.readFileList(subPath, false);
			if(fileList != null) {
				for(String file : fileList) {
					uiVo = ParseUtil.readUiVo(file, subPath);
					DbGen.insertTB_UI_FUNC_MAPPING(DBID, uiVo);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	
	
	public void saveToFile(String fileFullPath) {
		StringBuffer conts = new StringBuffer();
				
		
		FileUtil.writeFile(FileUtil.getFilePath(fileFullPath), FileUtil.getFileName(fileFullPath, true), conts.toString());
	}
	
}
