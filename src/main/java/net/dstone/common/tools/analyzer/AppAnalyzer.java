package net.dstone.common.tools.analyzer;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.svc.SvcAnalyzer;
import net.dstone.common.utils.DateUtil;
import net.dstone.common.utils.StringUtil;

public class AppAnalyzer extends BaseObject{

	private static AppAnalyzer analizer = null;
	
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
	
	public static int JOB_KIND_99_ANALYZE_ALL          	 = 99;	/* 전체작업 */
	public static int JOB_KIND_11_ANALYZE_CLASS          = 11;	/* 클래스파일리스트 에서 패키지ID/클래스ID/클래스명/기능종류 등이 담긴 클래스분석파일리스트 추출 */
	public static int JOB_KIND_12_ANALYZE_CLASS_ALIAS    = 12;  /* 클래스파일리스트 에서 호출알리아스 추출하여 클래스분석파일리스트에 추가 */
	public static int JOB_KIND_21_ANALYZE_QUERY          = 21;  /* 쿼리파일리스트 에서 KEY/네임스페이스/쿼리ID/쿼리종류/쿼리내용 등이 담긴 쿼리분석파일리스트 추출 */
	public static int JOB_KIND_22_ANALYZE_QUERY_CALLTBL  = 22;  /* 쿼리분석파일리스트 에 호출테이블ID정보목록 추가 */
	public static int JOB_KIND_31_ANALYZE_MTD            = 31;  /* 클래스파일리스트 에서 기능ID/메소드ID/메소드명/메소드URL/메소드내용 등이 담긴 메소드분석파일리스트 추출 */
	public static int JOB_KIND_32_ANALYZE_MTD_CALLMTD    = 32;  /* 메소드분석파일리스트 에 메소드내 타 호출메소드 목록 추가 */
	public static int JOB_KIND_33_ANALYZE_MTD_CALLTBL    = 33;  /* 메소드분석파일리스트 에 메소드내 호출테이블 목록 추가 */
	
	private SvcAnalyzer svcAnalyzer = new SvcAnalyzer();
	
	private AppAnalyzer(){
	}
	
	private static void debug(Object o) {
		System.out.println(o);
	}
	
	/**
	 * @param rootPath - 프로젝트 루트 디렉토리
	 * @param classRootPath - 클래스 루트 디렉토리
	 * @param webRootPath - 웹 루트 디렉토리
	 * @param includePackageRoot - 분석패키지루트 목록(분석대상 패키지 루트. 해당 패키지이하의 모듈만 분석한다.)
	 * @param excludePackagePattern - 분석제외패키지패턴 목록(분석제외대상 패키지 패턴. 해당 패키지명이 속하는 패키지는 분석제외한다.)
	 * @param queryRootPath - 쿼리 루트 디렉토리
	 * @param writePath - 중간산출물 저장디렉토리
	 * @return
	 */
	public static AppAnalyzer getInstance(String rootPath, String classRootPath, String webRootPath, String[] includePackageRoot, String[] excludePackagePattern, String queryRootPath, String writePath){
		if(analizer == null){
			analizer = new AppAnalyzer();
			AppAnalyzer.ROOT_PATH = StringUtil.replace(rootPath, "\\", "/");
			AppAnalyzer.CLASS_ROOT_PATH = StringUtil.replace(classRootPath, "\\", "/");
			AppAnalyzer.WEB_ROOT_PATH = StringUtil.replace(webRootPath, "\\", "/");
			AppAnalyzer.INCLUDE_PACKAGE_ROOT = includePackageRoot;
			AppAnalyzer.EXCLUDE_PACKAGE_PATTERN = excludePackagePattern;
			AppAnalyzer.QUERY_ROOT_PATH = StringUtil.replace(queryRootPath, "\\", "/");
			AppAnalyzer.WRITE_PATH = writePath;
		}
		return analizer;
	}

	public void analyze(int jobKind) {
		try {
			
			/*** 1. 서버소스 분석 ***/
			svcAnalyzer.analyze(jobKind);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
