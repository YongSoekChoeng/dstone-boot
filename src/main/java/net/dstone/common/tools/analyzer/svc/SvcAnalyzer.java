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
		for(String packageRoot : AppAnalyzer.ANALYSIS_PACKAGE_ROOT) {
			if( packageId.startsWith(packageRoot) ) {
				isValid = true;
				break;
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
		 * @param src
		 * @return
		 */
		static String getPackageId(String src) {
			Clzz clzz = new DefaultClzz();
			return clzz.getPackageId(src);
		}
		/**
		 * 클래스ID 추출
		 * @param src
		 * @return
		 */
		static String getClassId(String src) {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassId(src);
		}
		/**
		 * 클래스명 추출
		 * @param src
		 * @return
		 */
		static String getClassName(String src) {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassName(src);
		}
		/**
		 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
		 * @param src
		 * @return
		 */
		static ClzzKind getClassKind(String src) {
			Clzz clzz = new DefaultClzz();
			return clzz.getClassKind(src);
		}
		/**
		 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
		 * @param src
		 * @return
		 */
		static List<Map<String, String>> getCallClassAlias(String src) {
			Clzz clzz = new DefaultClzz();
			return clzz.getCallClassAlias(src);
		}
	}
	
	/**
	 * 메서드 분석 팩토리 클래스
	 * @author jysn007
	 */
	private static class MethodFactory {
		/**
		 * 메서드ID/메서드명/메서드내용 추출
		 * @param src
		 * @return
		 */
		static List<MtdVo> getMtdVoList(String src) {
			Mtd mtd = new DefaultMtd();
			return mtd.getMtdVoList(src);
		}
		/**
		 * 호출URL 추출. 기능종류가 CT:컨트롤러 일 경우에 한해서 추출.
		 * @param src
		 * @return
		 */
		static String getUrl(String src) {
			Mtd mtd = new DefaultMtd();
			return mtd.getUrl(src);
		}
	}
	/*********************** Factory 끝 ***********************/
	
	public void analyze(String subPath) {
		String[] fileList = null;
		String filePath = AppAnalyzer.ROOT_PATH;
		try {
			if(!StringUtil.isEmpty(subPath)) {
				filePath = filePath + "/" + subPath;
			}
			//debug("filePath:" + filePath);
			if( FileUtil.isFileExist(filePath) && FileUtil.isDirectory(filePath) ) {
				fileList = FileUtil.readFileListAll(filePath);

				/*** A. 클래스 단위 정보 추출 ***/
				// A-1. 패키지ID/클래스ID/클래스명/기능종류 추출
				this.analyzeClass(fileList);
				// A-2. 호출알리아스 추출
				this.analyzeClassAlias(fileList);
				
				/*** B. 메소드 단위 정보 추출 ***/
				// B-1. 기능ID/메소드ID/메소드명/메서드내용 추출
				this.analyzeMtd(fileList);

			}
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
					if( !isValidSvcPackage(clzzVo.getPackageId()) ) {
						continue;
					}
					if( !PACKAGE_LIST.contains(clzzVo.getPackageId()) ) {
						PACKAGE_LIST.add(clzzVo.getPackageId());
					}
					
					// 클래스ID
					clzzVo.setClassId(ClassFactory.getClassId(file));
					if( !CLASS_LIST.contains(clzzVo.getClassId()) ) {
						CLASS_LIST.add(clzzVo.getClassId());
					}
					if( !PACKAGE_CLASS_LIST.contains(clzzVo.getPackageId() + "." + clzzVo.getClassId()) ) {
						PACKAGE_CLASS_LIST.add(clzzVo.getPackageId() + "." + clzzVo.getClassId());
					}
					
					// 클래스명
					clzzVo.setClassName(ClassFactory.getClassName(file));
					
					// 기능종류
					clzzVo.setClassKind(ClassFactory.getClassKind(file));
					
					// 파일명
					clzzVo.setFileName(file);
					
					// 파일저장			
					ParseUtil.writeClassVo(clzzVo, AppAnalyzer.WRITE_PATH + "/class");
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
		String pkg = "";
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
					pkg = pkgClassId.substring(0, pkgClassId.lastIndexOf("."));
					if( !isValidSvcPackage(pkg) ) {
						continue;
					}
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
	 * @param file
	 */
	private void analyzeMtd(String[] fileList) {
		MtdVo mtdVo = null;
		String file= "";
		try {
			for(int i=0; i<fileList.length; i++) {
				file = fileList[i];
				if( isValidSvcFile(file) ) {
					mtdVo = new MtdVo();

					// 기능ID
					
					// 메소드ID/메소드명/메서드내용 추출
					
				}
			}
		} catch (Exception e) {
			getLogger().sysout(this.getClass().getName() + ".analyzeMtd()수행중 예외발생. file["+file+"]");
			e.printStackTrace();
			throw e;
		}

	}
	
}
