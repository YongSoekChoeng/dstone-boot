package net.dstone.common.tools.analyzer.svc.clzz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.svc.clzz.Clzz;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class DefaultClzz extends BaseObject implements Clzz {

	/**
	 * 패키지ID 추출
	 * @param classFile 클래스파일
	 * @return
	 */
	@Override
	public String getPackageId(String classFile) throws Exception {
		String pkg = "";
		String[] lines = FileUtil.readFileByLines(classFile);
		if(lines != null) {
			String keyword = "package";
			for(String line : lines) {
				line = line.trim();
				if(line.startsWith(keyword)) {
					pkg = line.substring(keyword.length()+1);
					pkg = StringUtil.replace(pkg, ";", "");
					pkg = StringUtil.replace(pkg, " ", "");
					break;
				}
			}
		}
		return pkg;
	}

	/**
	 * 클래스ID 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getClassId(String classFile) throws Exception  {
		String classId = "";
		classId = this.getPackageId(classFile) + "." + FileUtil.getFileName(classFile, false);
		return classId;
	}

	/**
	 * 클래스명 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getClassName(String classFile) throws Exception  {
		String className = "";
		String fileExt = FileUtil.getFileExt(classFile);
		String fileConts = FileUtil.readFile(classFile);
		
		if("java".equals(fileExt)) {
			if(fileConts.indexOf("public class") != -1) {
				fileConts = fileConts.substring(0, fileConts.indexOf("public class"));
				className = ParseUtil.getFnNameFromComment(fileConts);
			}
		}
		return className;
	}

	/**
	 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public ClzzKind getClassKind(String classFile) throws Exception  {
		ClzzKind classKind = ClzzKind.OT;
		String fileConts = FileUtil.readFile(classFile);
		fileConts = ParseUtil.adjustConts(fileConts);
		if(fileConts.indexOf("@Controller") != -1 || fileConts.indexOf("@RestController") != -1) {
			classKind = ClzzKind.CT;
		}else if(fileConts.indexOf("@Service") != -1) {
			classKind = ClzzKind.SV;
		}else if(fileConts.indexOf("@Repository") != -1) {
			classKind = ClzzKind.DA;
		}
		return classKind;
	}
	
	/**
	 * 리소스ID 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getResourceId(String classFile) throws Exception {
		String resourceId = "";
		String[] lines = FileUtil.readFileByLines(classFile);
		if(lines != null) {
			String searchLine = "";
			for(String line : lines) {
				line = line.trim();
				line = ParseUtil.adjustConts(line);
				if(line.indexOf("@Service") != -1 || line.indexOf("@Repository") != -1 || line.indexOf("@Resource") != -1) {
					searchLine = line;
				}
				if(!StringUtil.isEmpty(searchLine)) {
					resourceId = ParseUtil.getValueFromAnnotationLine(searchLine);
					break;
				}
				if(line.indexOf(" class ") != -1 || line.indexOf(" interface ") != -1) {
					break;
				}
			}
		}
		return resourceId;
	}
	
	/**
	 * 클래스or인터페이스(C:클래스/I:인터페이스) 추출
	 * @param classFile
	 * @return
	 */
	public String getClassOrInterface(String classFile) throws Exception{
		String classOrInterface = "";
		String fileConts = FileUtil.readFile(classFile);
		fileConts = ParseUtil.adjustConts(fileConts);
		if(fileConts.indexOf(" interface ")>-1) {
			classOrInterface = "I";
		}else {
			classOrInterface = "C";
		}
		return classOrInterface;
	}
	
	/**
	 * 인터페이스ID 추출.(인터페이스를 구현한 경우에만 존재)
	 * @param classFile
	 * @return
	 */
	public String getInterfaceId(String classFile) throws Exception{
		String interfaceId = "";
		String interfaceClass = "";
		String[] lines = FileUtil.readFileByLines(classFile);
		ArrayList<String> importList = new ArrayList<String>();
		if(lines != null) {
			String importLine = "";
			String searchLine = "";
			for(String line : lines) {
				line = line.trim();
				line = ParseUtil.adjustConts(line);
				if(line.indexOf("import ") != -1) {
					importLine = line;
					importLine = StringUtil.replace(importLine, "import ", "");
					importLine = StringUtil.replace(importLine, ";", "");
					importList.add(importLine);
				}
				if(line.indexOf(" implements ") != -1) {
					searchLine = line;
				}
				if(!StringUtil.isEmpty(searchLine)) {
					String[] div = {"{"};
					interfaceClass = StringUtil.nextWord(searchLine, " implements ", div);
					break;
				}
			}
		}
		if(!StringUtil.isEmpty(interfaceClass)) {
			if(interfaceClass.indexOf(".")>-1) {
				interfaceId = interfaceClass;
			}else {
				for(String importLine : importList) {
					if(importLine.endsWith("." + interfaceClass)) {
						interfaceId = importLine;
						break;
					}
				}
			}
		}
		return interfaceId;
	}
	
	/**
	 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
	 * @param classFile
	 * @param analyzedClassFileList
	 * @return
	 */
	@Override
	public List<Map<String, String>> getCallClassAlias(String classFile, String[] analyzedClassFileList) throws Exception  {
		List<Map<String, String>> callClassAliasList = new ArrayList<Map<String, String>>();
		Map<String, String> callClassAlias = new HashMap<String, String>();

		String fileConts = FileUtil.readFile(classFile);
		fileConts = ParseUtil.adjustConts(fileConts);
		String[] lines = StringUtil.toStrArray(fileConts, "\n");

		boolean isAliasExists = false;
		boolean isUsed = false;
		String alias = "";
		String[] div = {" ", ";", "=", " ="};
		String selfPkg = this.getPackageId(classFile);
		String selfClassId = this.getClassId(classFile);
		int lineNum = 0;
		String beforeLine = "";
		
		for(String packageClassId : analyzedClassFileList) {
			
			isAliasExists = false;
			isUsed = false;
			String pkg = packageClassId.substring(0, packageClassId.lastIndexOf("."));
			String classId = packageClassId;
			String resourceId = "";

			if( pkg.equals(selfPkg) && classId.equals(selfClassId)) {
				continue;
			}
			// [Full클래스 알리아스] 형식으로 선언되어있을 경우
			if(!isUsed 
				&& fileConts.indexOf(packageClassId)>-1
			) {
				lineNum = 0;
				beforeLine = "";
				for(String line : lines) {
					lineNum++;
					if (!ParseUtil.isValidAliasLine(line)) { 
						continue;
					}
					if( line.indexOf(packageClassId + " ")>-1 ) {
						alias = StringUtil.nextWord(line, packageClassId, div);
						if(!StringUtil.isEmpty(alias)) {
							isAliasExists = true;
							resourceId = ParseUtil.getValueFromAnnotationLine(beforeLine);
							break;
						}
					}
					beforeLine = line;
				}
				if (isAliasExists) {
					//getLogger().sysout(classFile + " lineNum["+lineNum+"] packageClassId[" + packageClassId + "] alias1=============>>>[" + alias + "]");
					if (fileConts.indexOf(alias + ".")>-1) {
						isUsed = true;
					}
				}

			// import 패키지 이후 [클래스ID 알리아스] 형식으로 선언되어있을 경우
			}else if(!isUsed 
				&&( fileConts.indexOf( "import " + pkg + ".*;")>-1 || fileConts.indexOf( "import " + packageClassId + ";")>-1 || pkg.equals(selfPkg) )
			) {
				lineNum = 0;
				beforeLine = "";
				for(String line : lines) {
					lineNum++;
					if (!ParseUtil.isValidAliasLine(line)) { 
						continue;
					}
					if( line.indexOf(packageClassId + " ")>-1 || line.indexOf(classId + " ")>-1 ) {
						alias = StringUtil.nextWord(line, classId, div);
						if(!StringUtil.isEmpty(alias)) {
							isAliasExists = true;
							resourceId = ParseUtil.getValueFromAnnotationLine(beforeLine);
							break;
						}
					}
					
					if (isAliasExists) {
						//getLogger().sysout(classFile + " lineNum["+lineNum+"] packageClassId[" + packageClassId + "] alias2=============>>>[" + alias + "]");
						if (fileConts.indexOf(alias + ".")>-1) {
							isUsed = true;
						}
					}
					
					beforeLine = line;
				}
				if (isAliasExists) {
					//getLogger().sysout(classFile + " lineNum["+lineNum+"] packageClassId[" + packageClassId + "] alias2=============>>>[" + alias + "]");
					if (fileConts.indexOf(alias + ".")>-1) {
						isUsed = true;
					}
				}
			}
			
			if(isUsed) {
				//getLogger().sysout(classFile + " packageClassId["+packageClassId + "] alias3=============>>>[" + alias + "]");
				callClassAlias = new HashMap<String, String>();
				ClzzVo clzzVo = ParseUtil.readClassVo(packageClassId, AppAnalyzer.WRITE_PATH + "/class");
				if( "I".equals(clzzVo.getClassOrInterface()) ) {
//					packageClassId = ParseUtil.findImplClassId(clzzVo.getClassId(), resourceId, analyzedClassFileList);
				}
				callClassAlias.put("FULL_CLASS",packageClassId);
				callClassAlias.put("ALIAS",alias);
				if( !callClassAliasList.contains(callClassAlias) ) {
					callClassAliasList.add(callClassAlias);
				}
			}
		}
		
		return callClassAliasList;
	}



}
