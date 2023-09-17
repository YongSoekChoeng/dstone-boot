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
	 * 분석패키지 루트(분석대상 패키지 루트. 해당 패키지이하의 모듈만 분석한다.)
	 */
	public static String[] ANALYSIS_PACKAGE_ROOT;
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
	
	public static AppAnalyzer getInstance(String rootPath, String classRootPath, String webRootPath, String[] analPackageRoot){
		if(analizer == null){
			analizer = new AppAnalyzer();
			AppAnalyzer.ROOT_PATH = StringUtil.replace(rootPath, "\\", "/");
			AppAnalyzer.CLASS_ROOT_PATH = StringUtil.replace(classRootPath, "\\", "/");
			AppAnalyzer.WEB_ROOT_PATH = StringUtil.replace(webRootPath, "\\", "/");
			AppAnalyzer.ANALYSIS_PACKAGE_ROOT = analPackageRoot;
			AppAnalyzer.WRITE_PATH = "D:/Temp/anlaysis/" + DateUtil.getToDate("yyyyMMddHHmmss");
		}
		return analizer;
	}

	public void analyze(String subPath) {
		try {
			
			/*** 1. 서버소스 분석 ***/
			svcAnalyzer.analyze(subPath);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
//	private void analyzeSvc(String file) {
//		PackageVo packageVo = null;
//		ClzzVo clzzVo = null;
//		try {
//			//debug("file:" + file);
//			/*** 패키지 ***/
//			packageVo = svcAnalyzer.getPackageVo(file);
//			/*** 클래스 ***/
//			clzzVo = svcAnalyzer.getClassVo(file, packageVo);
//			/*** 메소드 ***/
//			List<MtdVo> mtdVoList = clzzVo.getMtdVoList();
//			if(mtdVoList != null) {
//				for(MtdVo mtdVo : mtdVoList) {
//					/*** 메소드-참조메소드 ***/
//				}
//			}
//			debug(clzzVo);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw e;
//		}
//	}
}
