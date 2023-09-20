package net.dstone.common.tools.analyzer.svc.clzz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dstone.common.core.BaseObject;
import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.svc.SvcAnalyzer;
import net.dstone.common.tools.analyzer.svc.clzz.Clzz;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class DefaultClzz extends BaseObject implements Clzz {

	/**
	 * 패키지ID 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getPackageId(String classFile) {
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
	public String getClassId(String classFile) {
		String classId = "";
		classId = FileUtil.getFileName(classFile, false);
		return classId;
	}

	/**
	 * 클래스명 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getClassName(String classFile) {
		String className = "";
		String fileExt = FileUtil.getFileExt(classFile);
		String fileConts = FileUtil.readFile(classFile);
		
		if("java".equals(fileExt)) {
			if(fileConts.indexOf("public class") != -1) {
				fileConts = fileConts.substring(0, fileConts.indexOf("public class"));
				String[] lines = StringUtil.toStrArray(fileConts, "\n");
				for(String line : lines) {
					if(line.toUpperCase().indexOf("@DESCRIPTION") != -1) {
						className = line;
						className = line.substring(line.toUpperCase().indexOf("@DESCRIPTION")+12);
						break;
					}else if(line.toUpperCase().indexOf("@DESC") != -1) {
						className = line;
						className = line.substring(line.toUpperCase().indexOf("@DESC")+5);
						break;
					}
				}
				if(!StringUtil.isEmpty(className)) {
					className = StringUtil.replace(className, ":", "");
					className = StringUtil.replace(className, ";", "");
					className = StringUtil.replace(className, " ", "");
				}
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
	public ClzzKind getClassKind(String classFile) {
		ClzzKind classKind = ClzzKind.OT;
		String fileExt = FileUtil.getFileExt(classFile);
		if("jsp".equals(fileExt)) {
			classKind = ClzzKind.UI;
		}else if("js".equals(fileExt)) {
			classKind = ClzzKind.JS;
		}else if("java".equals(fileExt)) {
			String fileConts = FileUtil.readFile(classFile);
			fileConts = ParseUtil.adjustConts(fileConts);
			if(fileConts.indexOf("@Controller") != -1 || fileConts.indexOf("@RestController") != -1) {
				classKind = ClzzKind.CT;
			}else if(fileConts.indexOf("@Service") != -1) {
				classKind = ClzzKind.SV;
			}else if(fileConts.indexOf("@Repository") != -1) {
				classKind = ClzzKind.DA;
			}
		}
		return classKind;
	}

	/**
	 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
	 * @param classFile
	 * @return
	 */
	@Override
	public List<Map<String, String>> getCallClassAlias(String classFile) {
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
		
		for(String packageClassId : SvcAnalyzer.PACKAGE_CLASS_LIST) {
			
			isAliasExists = false;
			isUsed = false;
			String pkg = packageClassId.substring(0, packageClassId.lastIndexOf("."));
			String classId = packageClassId.substring(packageClassId.lastIndexOf(".")+1);

			if( pkg.equals(selfPkg) && classId.equals(selfClassId)) {
				continue;
			}
			// [Full클래스 알리아스] 형식으로 선언되어있을 경우
			if(!isUsed 
				&& fileConts.indexOf(packageClassId)>-1
			) {
				lineNum = 0;
				for(String line : lines) {
					lineNum++;
					if (!ParseUtil.isValidAliasLine(line)) { 
						continue;
					}
					if( line.indexOf(packageClassId + " ")>-1 ) {
						alias = net.dstone.common.utils.StringUtil.nextWord(line, packageClassId, div);
						if(!StringUtil.isEmpty(alias)) {
							isAliasExists = true;
							break;
						}
					}
				}
				if (isAliasExists) {
					//getLogger().sysout(classFile + " lineNum["+lineNum+"] packageClassId[" + packageClassId + "] alias1=============>>>[" + alias + "]");
					if (fileConts.indexOf(alias + ".")>-1) {
						isUsed = true;
					}
				}

			// import 패키지 이후 [클래스ID 알리아스] 형식으로 선언되어있을 경우
			}else if(!isUsed 
				&&( fileConts.indexOf( "import " + pkg + ";")>-1 ||  pkg.equals(selfPkg) )
			) {
				lineNum = 0;
				for(String line : lines) {
					lineNum++;
					if (!ParseUtil.isValidAliasLine(line)) { 
						continue;
					}
					if( line.indexOf(packageClassId + " ")>-1 || line.indexOf(classId + " ")>-1 ) {
						alias = net.dstone.common.utils.StringUtil.nextWord(line, classId, div);
						if(!StringUtil.isEmpty(alias)) {
							isAliasExists = true;
						}
					}
					if (isAliasExists) {
						//getLogger().sysout(classFile + " lineNum["+lineNum+"] packageClassId[" + packageClassId + "] alias2=============>>>[" + alias + "]");
						if (fileConts.indexOf(alias + ".")>-1) {
							isUsed = true;
						}
					}
				}
			}
			
			if(isUsed) {
				//getLogger().sysout(classFile + " packageClassId["+packageClassId + "] alias3=============>>>[" + alias + "]");
				callClassAlias = new HashMap<String, String>();
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
