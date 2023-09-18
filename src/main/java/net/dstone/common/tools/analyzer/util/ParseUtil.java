package net.dstone.common.tools.analyzer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class ParseUtil {
	
	public static String getFnNameFromComment(String comment) {
		String fnName = "";
		if(!StringUtil.isEmpty(comment)) {

			/*** 메서드명 세팅 ***/
			String[] mlines = StringUtil.toStrArray(comment, "\n") ;
			String keyword = "";
			for(String mline : mlines) {
				mline = mline.toUpperCase();
				if(StringUtil.isEmpty(fnName)) {
					keyword = "@DESCRIPTION";
					if(mline.indexOf(keyword) > -1) {
						fnName = mline;
						fnName = StringUtil.replace(fnName, keyword, "");
						fnName = StringUtil.replace(fnName, "/*", "");
						fnName = StringUtil.replace(fnName, "*", "");
						fnName = StringUtil.replace(fnName, " ", "");
						break;
					}
				}
				if(StringUtil.isEmpty(fnName)) {
					keyword = "@DESC";
					if(mline.indexOf(keyword) > -1) {
						fnName = mline;
						fnName = StringUtil.replace(fnName, keyword, "");
						fnName = StringUtil.replace(fnName, "/*", "");
						fnName = StringUtil.replace(fnName, "*", "");
						fnName = StringUtil.replace(fnName, " ", "");
						break;
					}
				}
				if(StringUtil.isEmpty(fnName)) {
					keyword = "@NAME";
					if(mline.indexOf(keyword) > -1) {
						fnName = mline;
						fnName = StringUtil.replace(fnName, keyword, "");
						fnName = StringUtil.replace(fnName, "/*", "");
						fnName = StringUtil.replace(fnName, "*", "");
						fnName = StringUtil.replace(fnName, " ", "");
						break;
					}
				}
			}
			if(StringUtil.isEmpty(fnName)) {
				for(String mline : mlines) {
					// 주석 첫줄이 아니고, 비어있는 주석라인이 아니고, @문자가들어가 있지 않다면 
					if( ( !mline.trim().startsWith("/*") ) && ( !mline.trim().equals("*") ) && ( mline.trim().startsWith("*") ) && (mline.indexOf("@") == -1) ) {
						fnName = mline;
						fnName = StringUtil.replace(fnName, "/*", "");
						fnName = StringUtil.replace(fnName, "*", "");
						fnName = StringUtil.replace(fnName, " ", "");
						break;
					}
				}
			}
			if(!StringUtil.isEmpty(fnName)) {
				if( fnName.indexOf("<") > -1 ) {
					fnName = fnName.substring(0, fnName.indexOf("<"));
				}
			}
		}
		return fnName;
	}

	public static List<Map<String, String>> getMtdListFromJava(String fileConts){
		List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
		
		boolean isCommentStart = false;
		boolean isCommentEnd = false;
		StringBuffer comment = new StringBuffer();

		boolean isSignitureStart = false;
		boolean isSignitureEnd = false;
		StringBuffer signiture = new StringBuffer();

		boolean isBodyStart = false;
		boolean isBodyEnd = false;
		StringBuffer body = new StringBuffer();
		
		int level = -1;
		int tgtLevel = 1;
		String startDiv = "{";
		String endDiv = "}";
		
		String line = "";
		
		try {
			fileConts = StringUtil.replace(fileConts, "\t", " ");
			String[] lines = StringUtil.toStrArray(fileConts, "\n", true);

			for(int i=0; i<lines.length; i++) {
				line = lines[i];
				
				/*** 레벨조정 시작 ***/
				// A.구분자 레벨 UP
				if(line.indexOf(startDiv) > -1) {
					level = level + StringUtil.countString(line, startDiv);
				}
				// B.구분자 레벨 DOWN
				if(line.indexOf(endDiv) > -1) {
					level = level - StringUtil.countString(line, endDiv);
				}
				//System.out.println("level["+level+"] line["+line+"]");
				/*** 레벨조정 끝 ***/
				
				/*** C.주석관련 수집 시작 ***/
				if(level == (tgtLevel-1)){ // tgtLevel 바로위의 레벨에 있는 내용을 수집.
					if(line.startsWith("/*")) {
						isCommentStart = true;
						isCommentEnd = false;
						comment.setLength(0);
					}
				}
				if(isCommentStart && !isCommentEnd) {
					comment.append(line).append("\n");
				}
				if(level == (tgtLevel-1)){ // tgtLevel 바로위의 레벨에 있는 내용을 수집.
					if(isCommentStart && line.endsWith("*/")) {
						isCommentEnd = true;
					}
				}
				/*** C.주석관련 수집 끝 ***/

				/*** D.시그니쳐 수집 시작 ***/
				// 메서드ID 는 startDiv과 동일한 라인에 존재할 수도 있고 다음라인에 존재할 수도 있기 때문에 level로 검색할 수 없음.
				if(line.indexOf("(")>-1 && (line.startsWith("public ") || line.startsWith("protected ") || line.startsWith("private "))) {
					isSignitureStart = true;
					isSignitureEnd = false;
					signiture.setLength(0);
				}
				if(isSignitureStart && !isSignitureEnd) {
					String signLine = line;
					signLine = StringUtil.replace(signLine, "public", "");
					signLine = StringUtil.replace(signLine, "protected", "");
					signLine = StringUtil.replace(signLine, "private", "");
					
					String[] div = {" "};
					String methodName = StringUtil.beforeWord(signLine, "(", div);
					signiture.append(methodName);
				}
				if(isSignitureStart && line.indexOf(")") > -1) {
					isSignitureEnd = true;
				}
				/*** D.시그니쳐 수집 끝 ***/

				/*** E.바디 수집 시작 ***/
				if(level == tgtLevel){ // tgtLevel 의 레벨에 있는 내용을 수집.
					if(line.indexOf(startDiv) > -1) {
						isBodyStart = true;
						isBodyEnd = false;
						body.setLength(0);
					}
				}
				if(isBodyStart && !isBodyEnd) {
					String bodyLine = line;
					if( bodyLine.indexOf(startDiv) >-1 && !bodyLine.startsWith(startDiv) ) {
						bodyLine = bodyLine.substring(bodyLine.indexOf(startDiv));
					}
					body.append(bodyLine).append("\n");
				}
				if(level == (tgtLevel-1)){ // B.구분자 레벨 DOWN을 통해서 level이 1->0이 되어있고 Body구문이 시작된 상태이며 endDiv가 발생했을 때 Body구문은 끝남.
					if(isBodyStart && line.indexOf(endDiv) > -1) {
						isBodyEnd = true;
					}
				}
				/*** E.바디 수집 끝 ***/
				
				if( (isSignitureEnd && signiture.length() > 0) && (isBodyEnd && body.length() > 0) ) {

					Map<String, String> item = new HashMap<String, String>();
					mList.add(item);

					/*** 메서드ID 세팅 ***/
					item.put("METHOD_ID", signiture.toString());
					signiture = new StringBuffer();
					/*** 메서드명 세팅 ***/
					item.put("METHOD_NAME", ParseUtil.getFnNameFromComment(comment.toString()));
					comment = new StringBuffer();
					/*** 메서드바디 세팅 ***/
					item.put("METHOD_BODY", body.toString());
					body = new StringBuffer();
					
				}

			}
		} catch (Exception e) {
			System.out.println("level["+level+"] line["+line+"] 수행중 예외발생.");
			e.printStackTrace();
		}		
		return mList;
	}
	
	public static void writeClassVo(ClzzVo vo, String writeFilePath) {
		String fileName = "";
		StringBuffer fileConts = new StringBuffer();
		StringBuffer callClassAliasConts = new StringBuffer();
		String div = "|";
		try {
			fileName = (StringUtil.isEmpty(vo.getPackageId()) ? "" : vo.getPackageId() + ".") + vo.getClassId()+ ".txt";
			fileConts.append("패키지" + div + StringUtil.nullCheck(vo.getPackageId(), "")).append("\n");
			fileConts.append("클래스ID" + div + StringUtil.nullCheck(vo.getClassId(), "")).append("\n");
			fileConts.append("클래스명" + div + StringUtil.nullCheck(vo.getClassName(), "")).append("\n");
			fileConts.append("기능종류" + div + StringUtil.nullCheck(vo.getClassKind(), "")).append("\n");
			fileConts.append("파일명" + div + StringUtil.nullCheck(vo.getFileName(), "")).append("\n");
			List<Map<String, String>> callClassAlias = vo.getCallClassAlias();
			if(callClassAlias != null) {
				for(Map<String, String> item : callClassAlias) {
					if(callClassAliasConts.length() > 0) {
						callClassAliasConts.append(",");
					}
					callClassAliasConts.append(item.get("FULL_CLASS")+"-"+item.get("ALIAS"));
				}
			}
			fileConts.append("호출알리아스" + div  + callClassAliasConts.toString() ).append("\n");
			FileUtil.writeFile(writeFilePath, fileName, fileConts.toString()); 
		} catch (Exception e) {
			System.out.println("fileName["+fileName+"] 수행중 예외발생.");	
			e.printStackTrace();
		}
	}
	
	public static ClzzVo readClassVo(String packageClassId, String readFilePath) {
		ClzzVo vo = new ClzzVo();
		String fileName = "";
		String div = "|";
		try {
			fileName = readFilePath + "/" + packageClassId+ ".txt";
			if(FileUtil.isFileExist(fileName)) {
				String[] lines = FileUtil.readFileByLines(fileName);
				for(String line : lines) {
					if(line.startsWith("패키지" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setPackageId(words[1]);
						}
					}
					if(line.startsWith("클래스ID" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setClassId(words[1]);
						}
					}
					if(line.startsWith("클래스명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setClassName(words[1]);
						}
					}
					if(line.startsWith("기능종류" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setClassKind(ClzzKind.getClzzKindCd(words[1]));
						}
					}
					if(line.startsWith("파일명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setFileName(words[1]);
						}
					}
					if(line.startsWith("호출알리아스" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							List<Map<String, String>> callClassAlias = new ArrayList<Map<String, String>>();
							String[] classAliasStrList = StringUtil.toStrArray(words[1], ",");
							for(String classAliasStr : classAliasStrList) {
								String[] classAliasStrPair = StringUtil.toStrArray(classAliasStr, "-");
								if(classAliasStrPair.length > 1) {
									Map<String, String> classAlias = new HashMap<String, String>();
									classAlias.put("FULL_CLASS", classAliasStrPair[0]);
									classAlias.put("ALIAS", classAliasStrPair[1]);
									callClassAlias.add(classAlias);
								}
							}
							vo.setCallClassAlias(callClassAlias);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	public static boolean isValidAliasLine(String line) {
		boolean isValid = true;
		ArrayList<String> inValidCaseList = new ArrayList<String>();
		inValidCaseList.add("class ");
		inValidCaseList.add("implements ");
		inValidCaseList.add("extends ");
		inValidCaseList.add("throws ");
		for(String word : inValidCaseList) {
			if(line.indexOf(word)>-1) {
				isValid = false;
				break;
			}
		}
		if(isValid) {
			if(line.trim().startsWith("//") || line.trim().startsWith("/*") || line.trim().startsWith("*")) {
				isValid = false;
			}
		}
		return isValid;
	}
	
	public static void writeMethodVo(MtdVo vo, String writeFilePath) {
		String fileName = "";
		StringBuffer fileConts = new StringBuffer();
		String div = "|";
		try {
			fileName = vo.getFunctionId() + ".txt";
			fileConts.append("기능ID" + div + StringUtil.nullCheck(vo.getFunctionId(), "")).append("\n");
			fileConts.append("메서드ID" + div + StringUtil.nullCheck(vo.getMethodId(), "")).append("\n");
			fileConts.append("메서드명" + div + StringUtil.nullCheck(vo.getMethodName(), "")).append("\n");
			fileConts.append("메서드URL" + div + StringUtil.nullCheck(vo.getMethodUrl(), "")).append("\n");
			fileConts.append("메서드내용" + div + StringUtil.nullCheck(vo.getMethodBody(), "")).append("\n");
			FileUtil.writeFile(writeFilePath, fileName, fileConts.toString()); 
		} catch (Exception e) {
			System.out.println("fileName["+fileName+"] 수행중 예외발생.");	
			e.printStackTrace();
		}
	}
	
	public static MtdVo readMethodVo(String functionId, String readFilePath) {
		MtdVo vo = new MtdVo();
		String fileName = "";
		String div = "|";
		try {
			fileName = readFilePath + "/" + functionId+ ".txt";
			if(FileUtil.isFileExist(fileName)) {
				String[] lines = FileUtil.readFileByLines(fileName);
				for(String line : lines) {
					if(line.startsWith("기능ID" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setFunctionId(words[1]);
						}
					}
					if(line.startsWith("메서드ID" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setMethodId(words[1]);
						}
					}
					if(line.startsWith("메서드명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setMethodName(words[1]);
						}
					}
					if(line.startsWith("메서드URL" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setMethodUrl(words[1]);
						}
					}
					if(line.startsWith("메서드내용" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setMethodBody(words[1]);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	

}
