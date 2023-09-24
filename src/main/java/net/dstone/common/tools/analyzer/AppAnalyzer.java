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
	 * @return
	 */
	public static AppAnalyzer getInstance(String rootPath, String classRootPath, String webRootPath, String[] includePackageRoot, String[] excludePackagePattern, String queryRootPath){
		if(analizer == null){
			analizer = new AppAnalyzer();
			AppAnalyzer.ROOT_PATH = StringUtil.replace(rootPath, "\\", "/");
			AppAnalyzer.CLASS_ROOT_PATH = StringUtil.replace(classRootPath, "\\", "/");
			AppAnalyzer.WEB_ROOT_PATH = StringUtil.replace(webRootPath, "\\", "/");
			AppAnalyzer.INCLUDE_PACKAGE_ROOT = includePackageRoot;
			AppAnalyzer.EXCLUDE_PACKAGE_PATTERN = excludePackagePattern;
			AppAnalyzer.WRITE_PATH = "D:/Temp/anlaysis/" + DateUtil.getToDate("yyyyMMddHHmmss");
			AppAnalyzer.QUERY_ROOT_PATH = StringUtil.replace(queryRootPath, "\\", "/");
		}
		return analizer;
	}

	public void analyze() {
		try {
			
			/*** 1. 서버소스 분석 ***/
			svcAnalyzer.analyze();
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
