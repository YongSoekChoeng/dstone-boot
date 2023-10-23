package net.dstone.common.tools.analyzer.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.tools.analyzer.vo.QueryVo;
import net.dstone.common.tools.analyzer.vo.UiVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.SqlUtil;
import net.dstone.common.utils.StringUtil;
import net.dstone.common.utils.XmlUtil;

public class ParseUtil {

	/**
	 * 파일내용을 파싱하기 편하게 변환.(탭을 스페이스로 변환, 다중스페이스를 단일스페이스로 변환)
	 * @param conts
	 * @return
	 */
	public static String adjustConts(String conts) {
		conts = StringUtil.replace(conts, "\t", " ");
		conts = StringUtil.replace(conts, "   ", " ");
		conts = StringUtil.replace(conts, "  ", " ");
		conts = StringUtil.replace(conts, " ;", ";");
		conts = StringUtil.replace(conts, " (", "(");
		conts = StringUtil.replace(conts, " )", ")");
		conts = StringUtil.replace(conts, " {", "{");
		conts = StringUtil.replace(conts, " }", "}");
		return conts;
	}
	/**
	 * 테이블명을 파싱하기 위해 SQL을 간소화 하는 메소드(주석제거, XML의 CDATA 태그제거, MYBATIS의 파라메터 세팅부분 변환)
	 * @param sqlBody
	 * @param sqlKind
	 * @return
	 */
	public static String simplifySqlForTblNm(String inputSql, String sqlKind) {
		String sqlBody = inputSql;
		sqlBody = sqlBody.trim();
	
		// 주석제거 및 쿼리정리
		sqlBody = XmlUtil.removeCommentsFromXml(sqlBody);
		sqlBody = SqlUtil.removeCommentsFromSql(sqlBody);
		sqlBody = adjustConts(sqlBody);
		
		HashMap<String, String> replMap = new HashMap<String, String>();
		// XML의 CDATA 태그제거
		replMap.put("<![CDATA[", "");
		replMap.put("]]>", "");
		// MYBATIS의 파라메터 태그제거하고 스몰쿼테이션추가
		replMap.put("#{", "'");
		replMap.put("#", "'");
		replMap.put("${", "'");
		replMap.put("$", "'");
		replMap.put("}", "'");
		// 내용없는 연속쉼표 랜덤스트링값으로 치환
		replMap.put(", ,", ", 'AAA',");
		sqlBody = StringUtil.replace(sqlBody, replMap).trim();
		
		return sqlBody;
	}
	
	
	/**
	 * Mybatis 형식 일 경우 내부 태그 제거.
	 * @param xml
	 * @param nodeExp
	 * @param recursivelyYn
	 * @return
	 */
	public static String getNodeTextByExpForMybatis(XmlUtil xml, String nodeExp, boolean recursivelyYn) {
		StringBuffer outBuff = new StringBuffer();
		Node node = xml.getNodeByExp(nodeExp);
		if(node != null) {
			NodeList nodeList = node.getChildNodes();
			if(nodeList != null) {
				
				List<String> tagToBeRemovedList = new ArrayList<String>();
				/*
				tagToBeRemovedList.add("choose");
				tagToBeRemovedList.add("foreach");
				tagToBeRemovedList.add("isEqual");
				tagToBeRemovedList.add("isNull");
				tagToBeRemovedList.add("isNotNull");
				tagToBeRemovedList.add("isNotEqual");
				tagToBeRemovedList.add("isEmpty");
				tagToBeRemovedList.add("isNotEmpty");
				tagToBeRemovedList.add("isGreaterThan");
				tagToBeRemovedList.add("isGreaterEqual");
				tagToBeRemovedList.add("isLessEqual");
				tagToBeRemovedList.add("isPropertyAvailable");
				tagToBeRemovedList.add("isNotPropertyAvailable");
				tagToBeRemovedList.add("isParameterPresent");
				tagToBeRemovedList.add("isNotParameterPresent");
				tagToBeRemovedList.add("dynamic");
				*/
				
				Node cNode = null;
				String refid = "";
				String cNodeExp = "";
				String[] refidArr = null;
				for(int i=0; i<nodeList.getLength(); i++) {
					cNode = nodeList.item(i);
					
					/*******************************************************
					멤버필드이름							정수값	노드종류
					Node.ELEMENT_NODE					1		Element
					Node.ATTRIBUTE_NODE					2		Attr
					Node.TEXT_NODE						3		Text
					Node.CDATA_SECTION_NODE				4		CDATASection
					Node.ENTITY_REFERENCE_NODE			5		EntityReference
					Node.ENTITY_NODE					6		Entity
					Node.PROCESSING_INSTRUCTION_NODE	7		ProcessingInstruction
					Node.COMMENT_NODE					8		Comment
					Node.DOCUMENT_NODE					9		Document
					Node.DOCUMENT_TYPE_NODE				10		DocumentType
					Node.DOCUMENT_FRAGMENT_NODE			11		DocumentFragment
					Node.NOTATION_NODE					12		Notation
					*******************************************************/
					//System.out.println( "NodeName:" + cNode.getNodeName() + ", NodeType:" + cNode.getNodeType() );
					
					if (cNode.getNodeType() == Node.TEXT_NODE) {
						outBuff.append(cNode.getNodeValue());
					}else if( cNode.getNodeType() == Node.ELEMENT_NODE) {
						if("include".equals(cNode.getNodeName())) {
							if( recursivelyYn) {
								refid = cNode.getAttributes().getNamedItem("refid").getTextContent();
								if( xml.hasNodeById(refid)) {
									 cNodeExp = "//*[@id='"+refid+"']";
									 outBuff.append(getNodeTextByExpForMybatis(xml, cNodeExp, recursivelyYn));
								}else {
									if(refid.indexOf(".") > -1) {
										refidArr = StringUtil.toStrArray(refid, ".");
										refid = refidArr[refidArr.length-1];
										if( xml.hasNodeById(refid)) {
											cNodeExp = "//*[@id='"+refid+"']";
											outBuff.append(getNodeTextByExpForMybatis(xml, cNodeExp, recursivelyYn));
										}
									}
								}
							}else {
								outBuff.append(cNode.getTextContent());
							}
						}else if("where".equals(cNode.getNodeName())) {
							outBuff.append( " WHERE ");
							if( cNode.getTextContent().trim().toUpperCase().startsWith("AND") ) {
								outBuff.append( " 1=1 ");
							}
							outBuff.append( cNode.getTextContent());
						}else if("trim".equals(cNode.getNodeName())) {
							if( cNode.getAttributes().getNamedItem("prefix") != null && "WHERE".equalsIgnoreCase(cNode.getAttributes().getNamedItem("prefix").getTextContent())) {
								outBuff.append( " WHERE ");
								if( cNode.getTextContent().trim().toUpperCase().startsWith("AND") ) {
									outBuff.append( " 1=1 ");
								}
							}
						}else if(tagToBeRemovedList.contains(cNode.getNodeName())) {
							continue;
						}else {
							outBuff.append(cNode.getTextContent());
						}

					}else if(cNode.getNodeType() == Node.COMMENT_NODE) {
						outBuff.append("");
					}else {
						outBuff.append(cNode.getTextContent());
					}
				}
			}
		}
		return outBuff.toString();
	}
	
	/**
	 * 주석에서 기능명을 추출하는 메소드.
	 * @param comment
	 * @return
	 */
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
				fnName = StringUtil.replace(fnName, ":", "");
				fnName = StringUtil.replace(fnName, ";", "");
				fnName = StringUtil.replace(fnName, " ", "");
			}
		}
		return fnName;
	}

	/**
	 *  파일내용으로부터 메소드ID/메소드명/메소드URL/메소드내용 이 담긴 메소드정보목록 추출
	 * @param fileConts
	 * @return
	 */
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

		String classMappingUrl = "";
		String methodMappingUrl = "";
		
		String line = "";
		
		try {
			fileConts = StringUtil.replace(fileConts, "\t", " ");
			String[] lines = StringUtil.toStrArray(fileConts, "\n", true);

			for(int i=0; i<lines.length; i++) {
				line = lines[i];
				if(line.trim().startsWith("//")) {continue;}

				/*** A.레벨조정 시작 ***/
				// 구분자 레벨 UP
				if(line.indexOf(startDiv) > -1) {
					level = level + StringUtil.countString(line, startDiv);
				}
				// 구분자 레벨 DOWN
				if(line.indexOf(endDiv) > -1) {
					level = level - StringUtil.countString(line, endDiv);
				}
				//System.out.println("level["+level+"] line["+line+"]");
				/*** A.레벨조정 끝 ***/
				
				/*** B.주석관련 수집 시작 ***/
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
				/*** B.주석관련 수집 끝 ***/

				/*** C.맵핑URL 수집 시작 ***/
				if( line.indexOf("@")>-1 && line.indexOf("Mapping")>-1 ) {
					if(level == -1) {
						String signLine = line;					
						signLine = StringUtil.replace(signLine, " ", "");
						if( signLine.indexOf("value=\"")>-1  ) {
							String[] div = {"\""};
							classMappingUrl = StringUtil.nextWord(signLine, "value=\"", div);
						}else {
							String[] div = {"\""};
							classMappingUrl = StringUtil.nextWord(signLine, "(\"", div);
						}
						if(classMappingUrl.endsWith("*")) {
							classMappingUrl = classMappingUrl.substring(0, classMappingUrl.lastIndexOf("*"));
						}
						if(classMappingUrl.endsWith("/")) {
							classMappingUrl = classMappingUrl.substring(0, classMappingUrl.lastIndexOf("/"));
						}
					}
					if(level == 0) {
						methodMappingUrl = "";
						String signLine = line;					
						signLine = StringUtil.replace(signLine, " ", "");
						if( signLine.indexOf("value=\"")>-1  ) {
							String[] div = {"\""};
							methodMappingUrl = StringUtil.nextWord(signLine, "value=\"", div);
						}else {
							String[] div = {"\""};
							methodMappingUrl = StringUtil.nextWord(signLine, "(\"", div);
						}
						if(!StringUtil.isEmpty(classMappingUrl)) {
							methodMappingUrl = classMappingUrl + methodMappingUrl;
						}
					}
				}

				/*** C.맵핑URL 수집 끝 ***/
				
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
					/*** 메서드URL 세팅 ***/
					item.put("METHOD_URL", methodMappingUrl);
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
	/**
	 * 클래스ID의 알리아스를 추출하기 위해 해당 소스 라인이 적합한지 조사하는 메소드.(클래스 시작부분, 주석부분은 적합하지 않으므로 false반환)
	 * @param line
	 * @return
	 */
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
	
	/**
	 * 웹페이지의 A태그로부터 링크를 추출하는 메소드
	 * @param webPageFile
	 * @return
	 */
	public static List<String> extrackLinksFromAtag(String webPageFile){
		List<String> linkList = new ArrayList<String>();
		try {
			if(FileUtil.isFileExist(webPageFile)) {
				String keyword = "";
				String[] div = {"'"};
				String nextWord = "";
				
				String conts = FileUtil.readFile(webPageFile);
				conts = StringUtil.replace(conts, "\r\n", "");
				conts = StringUtil.replace(conts, "\n", "");
				conts = adjustConts(conts);
				conts = StringUtil.replace(conts, "\"", "'");
				
				String contsForAtag = new String(conts);
				
				// A태그 Link
				contsForAtag = StringUtil.replace(contsForAtag, "href =", "href=");
				contsForAtag = StringUtil.replace(contsForAtag, "href= '", "href='");
				contsForAtag = StringUtil.replace(contsForAtag, "href=''", "");
				keyword = "href='";
				nextWord = "";
				while(contsForAtag.indexOf(keyword) > -1) {
					if(contsForAtag.indexOf(keyword) > -1) {
						nextWord = StringUtil.nextWord(contsForAtag, keyword, div);
						if(nextWord.indexOf("?")>-1) {
							nextWord = nextWord.substring(0, nextWord.indexOf("?"));
						}
						linkList.add(nextWord);
						contsForAtag = contsForAtag.substring( contsForAtag.indexOf(nextWord) + (nextWord).length() );
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linkList;
	}
	
	/**
	 * 웹페이지의 A태그로부터 링크를 파싱(추출)하는 메소드
	 * @param webPageFile
	 * @return
	 */
	public static List<String> parseLinksFromAtag(String webPageFile){
		List<String> linkList = new ArrayList<String>();
		try {

			if(FileUtil.isFileExist(webPageFile)) {

				org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse( net.dstone.common.utils.FileUtil.readFile(webPageFile) );
				String elVal = "";
				org.jsoup.select.Elements links = doc.select("a[href]");
			    for (org.jsoup.nodes.Element link : links) {
			    	elVal = link.attr("abs:href");
			    	if(!StringUtil.isEmpty(elVal)) {
			    		if(!linkList.contains(elVal)) {
			    			linkList.add(elVal);
			    		}
			    	}
			    }

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linkList;
	}

	
	/**
	 * 웹페이지의 폼액션으로부터 링크를 추출하는 메소드
	 * @param webPageFile
	 * @return
	 */
	public static List<String> extrackLinksFromAction(String webPageFile){
		List<String> linkList = new ArrayList<String>();
		try {
			if(FileUtil.isFileExist(webPageFile)) {
				String keyword = "";
				String[] div = {"'"};
				String nextWord = "";
				
				String conts = FileUtil.readFile(webPageFile);
				conts = StringUtil.replace(conts, "\r\n", "");
				conts = StringUtil.replace(conts, "\n", "");
				conts = adjustConts(conts);
				conts = StringUtil.replace(conts, "\"", "'");
				
				String contsForAction = new String(conts);
				
				// Form Action
				contsForAction = StringUtil.replace(contsForAction, ".action =", ".action=");
				contsForAction = StringUtil.replace(contsForAction, ".action= '", ".action='");
				contsForAction = StringUtil.replace(contsForAction, "action =", "action=");
				contsForAction = StringUtil.replace(contsForAction, "action= '", "action='");
				contsForAction = StringUtil.replace(contsForAction, "action=''", "");
				keyword = "action='";
				nextWord = "";
				while(contsForAction.indexOf(keyword) > -1) {
					if(contsForAction.indexOf(keyword) > -1) {
						nextWord = StringUtil.nextWord(contsForAction, keyword, div);
						if(nextWord.indexOf("?")>-1) {
							nextWord = nextWord.substring(0, nextWord.indexOf("?"));
						}
						linkList.add(nextWord);
						contsForAction = contsForAction.substring( contsForAction.indexOf(nextWord) + (nextWord).length() );
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linkList;
	}
	
	/**
	 * 웹페이지의 폼액션으로부터 링크를 파싱(추출)하는 메소드
	 * @param webPageFile
	 * @return
	 */
	public static List<String> parseLinksFromAction(String webPageFile){
		List<String> linkList = new ArrayList<String>();
		try {
			if(FileUtil.isFileExist(webPageFile)) {

				org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse( net.dstone.common.utils.FileUtil.readFile(webPageFile) );
				String elVal = "";
				org.jsoup.select.Elements links = doc.getElementsByTag("form");
			    for (org.jsoup.nodes.Element el : links) {
			    	elVal = el.attr("action");
			    	if(!StringUtil.isEmpty(elVal)) {
			    		if(!linkList.contains(elVal)) {
			    			linkList.add(elVal);
			    		}
			    	}
			    }
			    
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return linkList;
	}
	
	/**
	 * 어노테이션이 있는 라인으로부터 어노테이션값을 추출하는 메소드.
	 * 예)@Resource(name = "customUserService") ==>> customUserService
	 * 예)@Service("customUserService") ==>> customUserService
	 * @param annotationLine
	 * @return
	 */
	public static String getValueFromAnnotationLine(String annotationLine) {
		String annotationValue = "";
		
		if(!StringUtil.isEmpty(annotationLine) && annotationLine.indexOf("@")>-1) {
			if(annotationLine.indexOf("(")>-1) {
				annotationLine = annotationLine.trim();
				annotationLine = adjustConts(annotationLine);
				annotationLine = annotationLine.substring(annotationLine.indexOf("("));
				annotationLine = StringUtil.replace(annotationLine, "(", "");
				annotationLine = StringUtil.replace(annotationLine, "value", "");
				annotationLine = StringUtil.replace(annotationLine, "name", "");
				annotationLine = StringUtil.replace(annotationLine, "=", "");
				annotationLine = StringUtil.replace(annotationLine, ")", "");
				annotationLine = StringUtil.replace(annotationLine, "\"", "");
				annotationLine = StringUtil.replace(annotationLine, " ", "");
				annotationValue = annotationLine;
			}
		}
		
		return annotationValue;
	}
	
	/**
	 * 인터페이스인 클래스ID로 분석클래스파일목록(analyzedClassFileList)에서 구현클래스의 클래스ID을 추출하는 메소드.
	 * resourceId 가 일치하는 구현클래스를 우선적으로 찾는다.
	 * @param interfaceId
	 * @param resourceId
	 * @return
	 */
	public static String findImplClassId(String interfaceId, String resourceId) {
		String implClassId = "";
		ClzzVo interfaceVo = ParseUtil.readClassVo(interfaceId, AppAnalyzer.WRITE_PATH + "/class");
		ClzzVo implClzzVo = null;
		if( "I".equals(interfaceVo.getClassOrInterface()) ) {
			List<String> implClassIdList = interfaceVo.getImplClassIdList();
			if(implClassIdList != null) {
				// 해당인터페이스 구현클래스 목록을 LOOP 돌리면서 인터페이스의 클래스ID 가 구현클래스의 인터페이스ID와 일치하는 구현클래스의 resourceId를 찾아서 비교한다.
				for(String packageClassId : implClassIdList) {
					implClzzVo = ParseUtil.readClassVo(packageClassId, AppAnalyzer.WRITE_PATH + "/class");
					if( interfaceVo.getClassId().equals(implClzzVo.getInterfaceId())) {
						// resourceId 로 찾고자 할 때
						if( !StringUtil.isEmpty(resourceId) ) {
							if( resourceId.equals(implClzzVo.getResourceId())) {
								implClassId = implClzzVo.getClassId();
								break;
							}
						// resourceId 로 찾지 않을 때	
						}else {
							implClassId = implClzzVo.getClassId();
							break;
						}
					}
				}
			}
		}else {
			implClassId = interfaceId;
		}
		return implClassId;
	}
	
	/**
	 * 클래스VO를 특정디렉토리에 파일로 저장하는 메소드
	 * @param vo
	 * @param writeFilePath
	 */
	public static void writeClassVo(ClzzVo vo, String writeFilePath) {
		String fileName = "";
		StringBuffer fileConts = new StringBuffer();
		StringBuffer implClassIdConts = new StringBuffer();
		StringBuffer callClassAliasConts = new StringBuffer();
		String div = "|";
		try {
			fileName = vo.getClassId()+ ".txt";
			fileConts.append("패키지" + div + StringUtil.nullCheck(vo.getPackageId(), "")).append("\n");
			fileConts.append("클래스ID" + div + StringUtil.nullCheck(vo.getClassId(), "")).append("\n");
			fileConts.append("클래스명" + div + StringUtil.nullCheck(vo.getClassName(), "")).append("\n");
			fileConts.append("기능종류" + div + StringUtil.nullCheck(vo.getClassKind(), "")).append("\n");
			fileConts.append("리소스ID" + div + StringUtil.nullCheck(vo.getResourceId(), "")).append("\n");
			fileConts.append("클래스or인터페이스" + div + StringUtil.nullCheck(vo.getClassOrInterface(), "")).append("\n");
			fileConts.append("상위인터페이스ID" + div + StringUtil.nullCheck(vo.getInterfaceId(), "")).append("\n");
			List<String> implClassIdList = vo.getImplClassIdList();
			if(implClassIdList != null) {
				for(String item : implClassIdList) {
					if(implClassIdConts.length() > 0) {
						implClassIdConts.append(",");
					}
					implClassIdConts.append(item);
				}
			}
			fileConts.append("인터페이스구현하위클래스ID목록" + div  + implClassIdConts.toString() ).append("\n");
			
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
			
			fileConts.append("파일명" + div + StringUtil.nullCheck(vo.getFileName(), "")).append("\n");
			FileUtil.writeFile(writeFilePath, fileName, fileConts.toString()); 
		} catch (Exception e) {
			System.out.println("fileName["+fileName+"] 수행중 예외발생.");	
			e.printStackTrace();
		}
	}
	
	/**
	 * 페키지ID+클래스ID로 특정디렉토리에서 클래스VO를 복원하는 메소드
	 * @param packageClassId
	 * @param readFilePath
	 * @return
	 */
	public static ClzzVo readClassVo(String packageClassId, String readFilePath) {
		ClzzVo vo = new ClzzVo();
		String fileName = "";
		String div = "|";
		try {
			fileName = readFilePath + "/" + packageClassId+ ".txt";
			if(FileUtil.isFileExist(fileName)) {
				String[] lines = FileUtil.readFileByLines(fileName);
				for(String line : lines) {
					if(StringUtil.isEmpty(line.trim())) {continue;}
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
					
					if(line.startsWith("리소스ID" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setResourceId(words[1]);
						}
					}

					if(line.startsWith("클래스or인터페이스" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setClassOrInterface(words[1]);
						}
					}
					if(line.startsWith("상위인터페이스ID" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setInterfaceId(words[1]);
						}
					}
					if(line.startsWith("인터페이스구현하위클래스ID목록" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							List<String> implClassIdList = new ArrayList<String>();
							String[] implClassIdStrList = StringUtil.toStrArray(words[1], ",");
							for(String implClassIdStr : implClassIdStrList) {
								implClassIdList.add(implClassIdStr);
							}
							vo.setImplClassIdList(implClassIdList);
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

					if(line.startsWith("파일명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setFileName(words[1]);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	

	
	/**
	 * 메소드VO를 특정디렉토리에 파일로 저장하는 메소드
	 * @param vo
	 * @param writeFilePath
	 */
	public static void writeMethodVo(MtdVo vo, String writeFilePath) {
		String fileName = "";
		StringBuffer fileConts = new StringBuffer();
		String div = "|";
		StringBuffer callTblVoListConts = new StringBuffer();
		StringBuffer callMtdVoListConts = new StringBuffer();
		try {
			fileName = vo.getFunctionId() + ".txt";
			fileConts.append("기능ID" + div + StringUtil.nullCheck(vo.getFunctionId(), "")).append("\n");
			fileConts.append("메서드ID" + div + StringUtil.nullCheck(vo.getMethodId(), "")).append("\n");
			fileConts.append("메서드명" + div + StringUtil.nullCheck(vo.getMethodName(), "")).append("\n");
			fileConts.append("메서드URL" + div + StringUtil.nullCheck(vo.getMethodUrl(), "")).append("\n");
			fileConts.append("파일명" + div + StringUtil.nullCheck(vo.getFileName(), "")).append("\n");
			List<String> callMtdVoList = vo.getCallMtdVoList();
			if(callMtdVoList != null) {
				for(String item : callMtdVoList) {
					if(callMtdVoListConts.length() > 0) {
						callMtdVoListConts.append(",");
					}
					callMtdVoListConts.append(item);
				}
			}
			fileConts.append("호출메서드" + div + StringUtil.nullCheck(callMtdVoListConts, "")).append("\n");
			List<String> callTblVoList = vo.getCallTblVoList();
			if(callTblVoList != null) {
				for(String item : callTblVoList) {
					if(callTblVoListConts.length() > 0) {
						callTblVoListConts.append(",");
					}
					callTblVoListConts.append(item);
				}
			}
			fileConts.append("호출테이블" + div + StringUtil.nullCheck(callTblVoListConts, "")).append("\n");
			fileConts.append("메서드내용" + div + StringUtil.nullCheck(vo.getMethodBody(), "")).append("\n");
			
			FileUtil.writeFile(writeFilePath, fileName, fileConts.toString()); 
		} catch (Exception e) {
			System.out.println("fileName["+fileName+"] 수행중 예외발생.");	
			e.printStackTrace();
		}
	}
	
	/**
	 * 기능ID(페키지ID+클래스ID+메소드ID) 로 특정디렉토리에서 메소드VO를 복원하는 메소드
	 * @param functionId
	 * @param readFilePath
	 * @return
	 */
	public static MtdVo readMethodVo(String functionId, String readFilePath) {
		MtdVo vo = new MtdVo();
		String fileName = "";
		String div = "|";
		try {
			fileName = readFilePath + "/" + functionId+ ".txt";
			if(FileUtil.isFileExist(fileName)) {
				String[] lines = FileUtil.readFileByLines(fileName);
				for(String line : lines) {
					if(StringUtil.isEmpty(line.trim())) {continue;}
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
					if(line.startsWith("파일명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setFileName(words[1]);
						}
					}
					if(line.startsWith("호출메서드" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							List<String> callMtdVoList = new ArrayList<String>();
							String[] callMtdVoStrList = StringUtil.toStrArray(words[1], ",");
							for(String callMtdVoStr : callMtdVoStrList) {
								callMtdVoList.add(callMtdVoStr);
							}
							vo.setCallMtdVoList(callMtdVoList);
						}
					}
					if(line.startsWith("호출테이블" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							List<String> callTblVoList = new ArrayList<String>();
							String[] callTblVoStrList = StringUtil.toStrArray(words[1], ",");
							for(String callTblVoStr : callTblVoStrList) {
								callTblVoList.add(callTblVoStr);
							}
							vo.setCallTblVoList(callTblVoList);
						}
					}
					if(line.startsWith("메서드내용" + div)) {
						String methodBody = FileUtil.readFile(fileName);
						String keyword = "메서드내용" + div;
						methodBody = methodBody.substring(methodBody.indexOf(keyword)+keyword.length());
						vo.setMethodBody(methodBody);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	

	
	
	/**
	 * 쿼리VO를 특정디렉토리에 파일로 저장하는 메소드
	 * @param vo
	 * @param writeFilePath
	 */
	public static void writeQueryVo(QueryVo vo, String writeFilePath) {
		String fileName = "";
		StringBuffer queryInfoConts = new StringBuffer();
		String div = "|";
		StringBuffer tblListConts = new StringBuffer();
		try {
			fileName = StringUtil.nullCheck(vo.getKey(), "") + ".txt";
			queryInfoConts.append("KEY" + div + StringUtil.nullCheck(vo.getKey(), "")).append("\n");
			queryInfoConts.append("네임스페이스" + div + StringUtil.nullCheck(vo.getNamespace(), "")).append("\n");
			queryInfoConts.append("쿼리ID" + div + StringUtil.nullCheck(vo.getQueryId(), "")).append("\n");
			queryInfoConts.append("쿼리종류" + div + StringUtil.nullCheck(vo.getQueryKind(), "")).append("\n");
			queryInfoConts.append("파일명" + div + StringUtil.nullCheck(vo.getFileName(), "")).append("\n");
			List<String> callTblList = vo.getCallTblList();
			if(callTblList != null) {
				for(String item : callTblList) {
					if(tblListConts.length() > 0) {
						tblListConts.append(",");
					}
					tblListConts.append(item);
				}
			}
			queryInfoConts.append("호출테이블" + div + StringUtil.nullCheck(tblListConts, "")).append("\n");
			queryInfoConts.append("쿼리내용" + div + StringUtil.nullCheck(vo.getQueryBody(), "")).append("\n");
			
			FileUtil.writeFile(writeFilePath, fileName, queryInfoConts.toString()); 
		} catch (Exception e) {
			System.out.println("fileName["+fileName+"] 수행중 예외발생.");	
			e.printStackTrace();
		}
	}
	
	/**
	 * 쿼리ID로 특정디렉토리에서 쿼리VO를 복원하는 메소드
	 * @param key
	 * @param readFilePath
	 * @return
	 */
	public static QueryVo readQueryVo(String key, String readFilePath) {
		QueryVo vo = new QueryVo();
		String fileName = "";
		String div = "|";
		try {
			fileName = readFilePath + "/" + key+ ".txt";
			if(FileUtil.isFileExist(fileName)) {
				String[] lines = FileUtil.readFileByLines(fileName);
				for(String line : lines) {
					if(StringUtil.isEmpty(line.trim())) {continue;}
					if(line.startsWith("KEY" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setKey(words[1]);
						}
					}
					if(line.startsWith("네임스페이스" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setNamespace(words[1]);
						}
					}
					if(line.startsWith("쿼리ID" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setQueryId(words[1]);
						}
					}
					if(line.startsWith("쿼리종류" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setQueryKind(words[1]);
						}
					}
					if(line.startsWith("파일명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setFileName(words[1]);
						}
					}
					if(line.startsWith("호출테이블" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							List<String> callTblList = new ArrayList<String>();
							String[] callTblStrList = StringUtil.toStrArray(words[1], ",");
							for(String callTblStr : callTblStrList) {
								callTblList.add(callTblStr);
							}
							vo.setCallTblList(callTblList);
						}
					}
					if(line.startsWith("쿼리내용" + div)) {
						String queryBody = FileUtil.readFile(fileName);
						String keyword = "쿼리내용" + div;
						queryBody = queryBody.substring(queryBody.indexOf(keyword)+keyword.length());
						vo.setQueryBody(queryBody);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	


	
	/**
	 * UiVO를 특정디렉토리에 파일로 저장하는 메소드
	 * @param vo
	 * @param writeFilePath
	 */
	public static void writeUiVo(UiVo vo, String writeFilePath) {
		String fileName = "";
		StringBuffer uiConts = new StringBuffer();
		String div = "|";
		StringBuffer linkConts = new StringBuffer();
		try {
			fileName = StringUtil.nullCheck(vo.getUiId(), "") + ".txt";
			uiConts.append("UI아이디" + div + StringUtil.nullCheck(vo.getUiId(), "")).append("\n");
			uiConts.append("UI명" + div + StringUtil.nullCheck(vo.getUiName(), "")).append("\n");
			uiConts.append("파일명" + div + StringUtil.nullCheck(vo.getFileName(), "")).append("\n");
			List<String> linkList = vo.getLinkList();
			if(linkList != null) {
				for(String item : linkList) {
					if(linkConts.length() > 0) {
						linkConts.append(",");
					}
					linkConts.append(item);
				}
			}
			uiConts.append("링크" + div + StringUtil.nullCheck(linkConts, "")).append("\n");

			FileUtil.writeFile(writeFilePath, fileName, uiConts.toString()); 
		} catch (Exception e) {
			System.out.println("fileName["+fileName+"] 수행중 예외발생.");	
			e.printStackTrace();
		}
	}
	
	/**
	 * UI아이디로 특정디렉토리에서 UiVO를 복원하는 메소드
	 * @param uiId
	 * @param readFilePath
	 * @return
	 */
	public static UiVo readUiVo(String uiId, String readFilePath) {
		UiVo vo = new UiVo();
		String fileName = "";
		String div = "|";
		try {
			fileName = readFilePath + "/" + uiId+ ".txt";
			if(FileUtil.isFileExist(fileName)) {
				String[] lines = FileUtil.readFileByLines(fileName);
				for(String line : lines) {
					if(StringUtil.isEmpty(line.trim())) {continue;}
					if(line.startsWith("UI아이디" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setUiId(words[1]);
						}
					}
					if(line.startsWith("UI명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setUiName(words[1]);
						}
					}
					if(line.startsWith("파일명" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							vo.setFileName(words[1]);
						}
					}
					if(line.startsWith("링크" + div)) {
						String[] words = StringUtil.toStrArray(line, div);
						if(words.length > 1) {
							List<String> linkList = new ArrayList<String>();
							String[] linkStrList = StringUtil.toStrArray(words[1], ",");
							for(String linkStr : linkStrList) {
								linkList.add(linkStr);
							}
							vo.setLinkList(linkList);
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
