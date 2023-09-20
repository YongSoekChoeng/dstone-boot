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
import net.dstone.common.utils.FileUtil;
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
	public static ArrayList<String> PACKAGE_LIST = new ArrayList<String>();
	public static ArrayList<String> CLASS_LIST = new ArrayList<String>();
	public static ArrayList<String> PACKAGE_CLASS_LIST = new ArrayList<String>();
	
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
		 * @param classFile
		 * @return
		 */
		static String getPackageId(String classFile) {
			Clzz clzz = new DefaultClzz();
			return clzz.getPackageId(classFile);
		}
		/**
		 * 클래스ID 추출
		 * @param classFile
		 * @return
		 */
		static String getClassId(String classFile) {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassId(classFile);
		}
		/**
		 * 클래스명 추출
		 * @param classFile
		 * @return
		 */
		static String getClassName(String classFile) {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassName(classFile);
		}
		/**
		 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
		 * @param classFile
		 * @return
		 */
		static ClzzKind getClassKind(String classFile) {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassKind(classFile);
		}
		/**
		 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
		 * @param classFile
		 * @return
		 */
		static List<Map<String, String>> getCallClassAlias(String classFile) {
			Clzz clzz = new DefaultClzz();
			return clzz.getCallClassAlias(classFile);
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
		static List<Map<String, String>> getMtdInfoList(String classFile) {
			Mtd mtd = new DefaultMtd();
			return mtd.getMtdInfoList(classFile);
		}
		
		/**
		 * 호출메소드 목록 추출
		 * @param methodFile
		 * @return
		 */
		static List<String> getCallMtdList(String methodFile){
			Mtd mtd = new DefaultMtd();
			return mtd.getCallMtdList(methodFile);
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
		static List<Map<String, String>> getQueryInfoList(String queryFile) {
			DefaultQuery query = new DefaultQuery();
			return query.getQueryInfoList(queryFile);
		}

		/**
		 * 쿼리정보파일로부터 테이블ID정보목록 추출
		 * @param queryInfoFile
		 * @return
		 */
		static List<String> getTblInfoList(String queryInfoFile) {
			DefaultQuery query = new DefaultQuery();
			return query.getTblInfoList(queryInfoFile);
		}
		
	}
	/*********************** Factory 끝 ***********************/
	
	public void analyze(String subPath) {
		String[] fileList = null;
		ArrayList<String> filteredFileList = null;
		try {

			/**************************************** 쿼리 소스 분석 시작 ****************************************/
			/*** 쿼리 파일추출 시작 ***/
			String[] queryFileList = null;
			fileList = FileUtil.readFileListAll(AppAnalyzer.QUERY_ROOT_PATH);
			filteredFileList = new ArrayList<String>();
			for(String file : fileList) {
				if( !isValidQueryFile(file) ) {
					continue;
				}
				filteredFileList.add(file);
			}
			queryFileList = new String[filteredFileList.size()];
			filteredFileList.toArray(queryFileList);
			filteredFileList.clear();
			filteredFileList = null;
			/*** 쿼리 파일추출 끝 ***/

			/**************************************** 쿼리 소스 분석 끝 ****************************************/

			/**************************************** SVC 소스 분석 시작 ****************************************/
			
			String filePath = AppAnalyzer.ROOT_PATH;
			if(!StringUtil.isEmpty(subPath)) {
				filePath = filePath + "/" + subPath;
			}
			//debug("filePath:" + filePath);
			if( FileUtil.isFileExist(filePath) && FileUtil.isDirectory(filePath) ) {
				
				/*** 클래스 파일추출 시작 ***/
				String[] classFileList = null;
				fileList = FileUtil.readFileListAll(filePath);
				filteredFileList = new ArrayList<String>();
				for(String file : fileList) {
					if( !isValidSvcPackage(ClassFactory.getPackageId(file)) ) {
						continue;
					}
					filteredFileList.add(file);
				}
				classFileList = new String[filteredFileList.size()];
				filteredFileList.toArray(classFileList);
				filteredFileList.clear();
				filteredFileList = null;
				/*** 클래스 파일추출 끝 ***/
				
				/*** 클래스 단위 정보 추출 시작 ***/
				// 패키지ID/클래스ID/클래스명/기능종류 추출
				this.analyzeClass(classFileList);
				// 호출알리아스 추출
				this.analyzeClassAlias(classFileList);
				/*** 클래스 단위 정보 추출 끝 ***/
				
				/*** 메소드 단위 정보 추출 시작 ***/
				String[] methodFileList = null;
				// 기능ID/메소드ID/메소드명/메소드URL/메소드내용 추출
				methodFileList = this.analyzeMtd(classFileList);
				if(methodFileList != null && methodFileList.length > 0) {
					// 메소드내 타 호출메소드 목록 추출
					this.analyzeMtdCallMtd(methodFileList);
				}
				/*** 메소드 단위 정보 추출 끝 ***/
			}
			/**************************************** SVC 소스 분석 끝 ****************************************/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 클래스의 패키지/클래스ID/클래스명/기능종류 추출
	 * @param fileList
	 */
	private void analyzeClass(String[] fileList) {
		ClzzVo clzzVo = null;
		String file= "";
		try {
			for(int i=0; i<fileList.length; i++) {
				file = fileList[i];
				if( isValidSvcFile(file) ) {
					
					clzzVo = new ClzzVo();
					
					// 패키지ID
					clzzVo.setPackageId(ClassFactory.getPackageId(file));
					
					// 클래스ID
					clzzVo.setClassId(ClassFactory.getClassId(file));
					
					// 클래스명
					clzzVo.setClassName(ClassFactory.getClassName(file));
					
					// 기능종류
					clzzVo.setClassKind(ClassFactory.getClassKind(file));
					
					// 파일명
					clzzVo.setFileName(file);
					
					// 파일저장			
					ParseUtil.writeClassVo(clzzVo, AppAnalyzer.WRITE_PATH + "/class");

					// 패키지/클래스/패키지+클래스 ID 목록 메모리에 저장
					if( !PACKAGE_LIST.contains(clzzVo.getPackageId()) ) {
						PACKAGE_LIST.add(clzzVo.getPackageId());
					}
					if( !CLASS_LIST.contains(clzzVo.getClassId()) ) {
						CLASS_LIST.add(clzzVo.getClassId());
					}
					if( !PACKAGE_CLASS_LIST.contains(clzzVo.getPackageId() + "." + clzzVo.getClassId()) ) {
						PACKAGE_CLASS_LIST.add(clzzVo.getPackageId() + "." + clzzVo.getClassId());
					}
					
				}
			}
		} catch (Exception e) {
			getLogger().sysout(this.getClass().getName() + ".analyzeClass()수행중 예외발생. file["+file+"]");
			e.printStackTrace();
			throw e;
		}

	}
	
	/**
	 * 클래스의 호출알리아스 추출
	 * @param fileList
	 */
	private void analyzeClassAlias(String[] fileList) {
		ClzzVo clzzVo = null;
		String pkgClassId = "";
		String file= "";
		try {
			for(int i=0; i<fileList.length; i++) {
				file = fileList[i];
				if( isValidSvcFile(file) ) {
					String fileNoExt = file.substring(0, file.lastIndexOf("."));
					pkgClassId = StringUtil.replace(fileNoExt, AppAnalyzer.CLASS_ROOT_PATH, "");
					if(pkgClassId.startsWith("/")) {
						pkgClassId = pkgClassId.substring(1);
					}
					pkgClassId = StringUtil.replace(pkgClassId, "/", ".");
					clzzVo = ParseUtil.readClassVo(pkgClassId, AppAnalyzer.WRITE_PATH + "/class");
					
					// 호출알리아스
					clzzVo.setCallClassAlias(ClassFactory.getCallClassAlias(file));
					
					// 파일저장	
					ParseUtil.writeClassVo(clzzVo, AppAnalyzer.WRITE_PATH + "/class");
				}
			}
		} catch (Exception e) {
			getLogger().sysout(this.getClass().getName() + ".analyzeClassAlias()수행중 예외발생. file["+file+"]");
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 메소드ID/메소드명/메소드URL/메소드내용 추출
	 * @param fileList
	 * @return 메소드파일목록
	 */
	private String[] analyzeMtd(String[] fileList) {
		String[] methodFileList = null;
		MtdVo mtdVo = null;
		List<Map<String, String>> methodInfoList = null;
		String file= "";
		String functionId = "";
		try {
			for(int i=0; i<fileList.length; i++) {
				file = fileList[i];
				if( isValidSvcFile(file) ) {
					// 메소드ID/메소드명/메소드URL/메소드내용 추출
					methodInfoList = MethodFactory.getMtdInfoList(file);
					if( methodInfoList != null ) {
						for(Map<String, String> methodInfo : methodInfoList) {
							mtdVo = new MtdVo();

							// 기능ID
							functionId = ClassFactory.getPackageId(file) + "." + ClassFactory.getClassId(file) + "." + methodInfo.get("METHOD_ID");
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
			methodFileList = FileUtil.readFileListAll(AppAnalyzer.WRITE_PATH + "/method");
		} catch (Exception e) {
			getLogger().sysout(this.getClass().getName() + ".analyzeMtd()수행중 예외발생. file["+file+"]");
			e.printStackTrace();
			throw e;
		}
		return methodFileList;
	}
	
	/**
	 * 메소드내 타 호출메소드 목록 추출
	 * @param fileList
	 */
	private void analyzeMtdCallMtd(String[] fileList) {
		MtdVo mtdVo = null;
		String functionId = "";
		String file = "";
		
		try {
			if(fileList != null) {
				for(int i=0; i<fileList.length; i++) {
					file = fileList[i];
					String fileNoExt = file.substring(0, file.lastIndexOf("."));
					functionId = StringUtil.replace(fileNoExt, AppAnalyzer.WRITE_PATH + "/method", "");
					if(functionId.startsWith("/")) {
						functionId = functionId.substring(1);
					}
					functionId = StringUtil.replace(functionId, "/", ".");
					mtdVo = ParseUtil.readMethodVo(functionId, AppAnalyzer.WRITE_PATH + "/method");
					
					// 호출메소드
					mtdVo.setCallMtdVoList(MethodFactory.getCallMtdList(file));
					
					// 파일저장	
					ParseUtil.writeMethodVo(mtdVo, AppAnalyzer.WRITE_PATH + "/method");
				}
			}

		} catch (Exception e) {
			getLogger().sysout(this.getClass().getName() + ".analyzeMtdCallMtd()수행중 예외발생. file["+file+"]");
			e.printStackTrace();
			throw e;
		}
	}
	
	private void analyzeQuery(String queryRootPath) {
		String[] fileList = null;

		if(!StringUtil.isEmpty(queryRootPath)) {
			if( FileUtil.isFileExist(queryRootPath) && FileUtil.isDirectory(queryRootPath) ) {
				
				/*** A. 파일추출 시작 ***/
				fileList = FileUtil.readFileListAll(queryRootPath);
				ArrayList<String> filteredFileList = new ArrayList<String>();
				for(String file : fileList) {
					if( !isValidQueryFile(file) ) {
						continue;
					}
					filteredFileList.add(file);
				}
				fileList = new String[filteredFileList.size()];
				filteredFileList.toArray(fileList);
				filteredFileList.clear();
				filteredFileList = null;
				/*** A. 파일추출 끝 ***/
				
			}
		}

		
		try {

		} catch (Exception e) {
//			getLogger().sysout(this.getClass().getName() + ".analyzeQuery()수행중 예외발생. file["+file+"]");
			e.printStackTrace();
			throw e;
		}
	}
	
}
