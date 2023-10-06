package net.dstone.common.tools.analyzer;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.svc.SvcAnalyzer;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class AppAnalyzer extends BaseObject{

	private static AppAnalyzer analizer = null;

	/**
	 * DBID
	 */
	public static String DBID;
	/**
	 * 프로젝트 루트 디렉토리
	 */
	public static String ROOT_PATH;
	/**
	 * 클래스 루트 디렉토리
	 */
	public static String CLASS_ROOT_PATH;
	/**
	 * 분석패키지루트 목록(분석대상 패키지 루트. 해당 패키지이하의 모듈만 분석한다.)
	 */
	public static String[] INCLUDE_PACKAGE_ROOT;
	/**
	 * 분석제외패키지패턴 목록(분석제외대상 패키지 패턴. 해당 패키지명이 속하는 패키지는 분석제외한다.)
	 */
	public static String[] EXCLUDE_PACKAGE_PATTERN;
	/**
	 * 쿼리 루트 디렉토리
	 */
	public static String QUERY_ROOT_PATH;
	/**
	 * 웹 루트 디렉토리
	 */
	public static String WEB_ROOT_PATH;
	/**
	 * 중간산출물 저장디렉토리
	 */
	public static String WRITE_PATH;
	
	
	/**
	 * 작업종류 - 전체작업 진행
	 */
	public static int JOB_KIND_99_ANALYZE_ALL          	 = 99;
	/**
	 * 작업종류 - 클래스파일리스트 에서 패키지ID/클래스ID/클래스명/기능종류 등이 담긴 클래스분석파일리스트 추출
	 */
	public static int JOB_KIND_11_ANALYZE_CLASS          = 11;
	/**
	 * 작업종류 - 클래스파일리스트 에서 호출알리아스 추출하여 클래스분석파일리스트에 추가
	 */
	public static int JOB_KIND_12_ANALYZE_CLASS_ALIAS    = 12;
	/**
	 * 작업종류 - 쿼리파일리스트 에서 KEY/네임스페이스/쿼리ID/쿼리종류/쿼리내용 등이 담긴 쿼리분석파일리스트 추출
	 */
	public static int JOB_KIND_21_ANALYZE_QUERY          = 21;
	/**
	 * 작업종류 - 쿼리분석파일리스트 에 호출테이블ID정보목록 추가
	 */
	public static int JOB_KIND_22_ANALYZE_QUERY_CALLTBL  = 22;
	/**
	 * 작업종류 - 클래스파일리스트 에서 기능ID/메소드ID/메소드명/메소드URL/메소드내용 등이 담긴 메소드분석파일리스트 추출
	 */
	public static int JOB_KIND_31_ANALYZE_MTD            = 31;
	/**
	 * 작업종류 - 메소드분석파일리스트 에 메소드내 타 호출메소드 목록 추가
	 */
	public static int JOB_KIND_32_ANALYZE_MTD_CALLMTD    = 32;
	/**
	 * 작업종류 - 메소드분석파일리스트 에 메소드내 호출테이블 목록 추가
	 */
	public static int JOB_KIND_33_ANALYZE_MTD_CALLTBL    = 33;
	/**
	 * 작업종류 - UI파일로부터  UI아이디/UI명 등이 담긴 UI분석파일목록 추출
	 */
	public static int JOB_KIND_41_ANALYZE_UI		     = 41;
	/**
	 * 작업종류 - UI파일로부터 Include된 타 UI파일목록의 추출
	 */
	public static int JOB_KIND_42_ANALYZE_UI_INCLUDE     = 42;
	/**
	 * 작업종류 - UI파일로부터 링크 추출
	 */
	public static int JOB_KIND_43_ANALYZE_UI_LINK        = 43;
	
	private SvcAnalyzer svcAnalyzer = new SvcAnalyzer();
	
	private AppAnalyzer(){
	}
	
	/**
	 * @param DBID - DBID
	 * @param rootPath - 프로젝트 루트 디렉토리
	 * @param classRootPath - 클래스 루트 디렉토리
	 * @param webRootPath - 웹 루트 디렉토리
	 * @param includePackageRoot - 분석패키지루트 목록(분석대상 패키지 루트. 해당 패키지이하의 모듈만 분석한다.)
	 * @param excludePackagePattern - 분석제외패키지패턴 목록(분석제외대상 패키지 패턴. 해당 패키지명이 속하는 패키지는 분석제외한다.)
	 * @param queryRootPath - 쿼리 루트 디렉토리
	 * @param writePath - 중간산출물 저장디렉토리
	 * @return
	 */
	public static AppAnalyzer getInstance(String DBID, String rootPath, String classRootPath, String webRootPath, String[] includePackageRoot, String[] excludePackagePattern, String queryRootPath, String writePath){
		if(analizer == null){
			analizer = new AppAnalyzer();
			
			AppAnalyzer.DBID = DBID;

			rootPath = StringUtil.replace(rootPath, "\\", "/");
			if(rootPath.endsWith("/")) {rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));}
			AppAnalyzer.ROOT_PATH = rootPath;
			
			classRootPath = StringUtil.replace(classRootPath, "\\", "/");
			if(classRootPath.endsWith("/")) {classRootPath = classRootPath.substring(0, classRootPath.lastIndexOf("/"));}
			AppAnalyzer.CLASS_ROOT_PATH = classRootPath;
			
			webRootPath = StringUtil.replace(webRootPath, "\\", "/");
			if(webRootPath.endsWith("/")) {webRootPath = webRootPath.substring(0, webRootPath.lastIndexOf("/"));}
			AppAnalyzer.WEB_ROOT_PATH = webRootPath;
			
			AppAnalyzer.INCLUDE_PACKAGE_ROOT = includePackageRoot;
			
			AppAnalyzer.EXCLUDE_PACKAGE_PATTERN = excludePackagePattern;

			queryRootPath = StringUtil.replace(queryRootPath, "\\", "/");
			if(queryRootPath.endsWith("/")) {queryRootPath = queryRootPath.substring(0, queryRootPath.lastIndexOf("/"));}
			AppAnalyzer.QUERY_ROOT_PATH = queryRootPath;
			
			writePath = StringUtil.replace(writePath, "\\", "/");
			if(writePath.endsWith("/")) {writePath = writePath.substring(0, writePath.lastIndexOf("/"));}
			AppAnalyzer.WRITE_PATH = writePath;
		}
		return analizer;
	}

	public void initAll() {
		try {
			
			/*** DB 데이터 삭제 ***/
			svcAnalyzer.deleteFromDb(AppAnalyzer.DBID);

			/*** 파일정보 삭제 ***/
			getLogger().info("/**************************************** 디렉토리["+AppAnalyzer.WRITE_PATH+"] 삭제 시작 ****************************************/");
			FileUtil.deleteDir(AppAnalyzer.WRITE_PATH);
			getLogger().info("/**************************************** 디렉토리["+AppAnalyzer.WRITE_PATH+"] 생성 시작 ****************************************/");
			FileUtil.makeDir(AppAnalyzer.WRITE_PATH);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void analyze(int jobKind) {
		try {
			
			/*** 서버소스 분석 ***/
			svcAnalyzer.analyze(jobKind);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void save() {
		try {
			
			/*** DB저장 ***/
			svcAnalyzer.saveToDb(AppAnalyzer.DBID);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
