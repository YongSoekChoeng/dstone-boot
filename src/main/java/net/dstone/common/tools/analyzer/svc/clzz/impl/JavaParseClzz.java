package net.dstone.common.tools.analyzer.svc.clzz.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.svc.SvcAnalyzer;
import net.dstone.common.tools.analyzer.svc.clzz.ParseClzz;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class JavaParseClzz extends TextParseClzz implements ParseClzz {
	
	/**
	 * 패키지ID 추출
	 * @param classFile 클래스파일
	 * @return
	 */
	@Override
	public String getPackageId(String classFile) throws Exception {
		String pkg = "";
		CompilationUnit cu = ParseUtil.getCompilationUnit(classFile);
        // packageId
		pkg = cu.getPackageDeclaration().get().getNameAsString();
		return pkg;
	}

	/**
	 * 클래스ID 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getClassId(String classFile) throws Exception {
		String classId = "";
		CompilationUnit cu = ParseUtil.getCompilationUnit(classFile);
		classId = cu.getPackageDeclaration().get().getNameAsString() + "." + cu.getType(0).getNameAsString();
		return classId;
	}

	/**
	 * 클래스명 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getClassName(String classFile) throws Exception {
		String className = "";
		CompilationUnit cu = ParseUtil.getCompilationUnit(classFile);
        if(cu.getType(0).getJavadocComment().isPresent()) {
        	className = ParseUtil.getFnNameFromComment(cu.getType(0).getJavadocComment().get().asString());
        }
		return className;
	}

	/**
	 * 기능종류(UI:화면/JS:자바스크립트/CT:컨트롤러/SV:서비스/DA:DAO/OT:나머지) 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public ClzzKind getClassKind(String classFile) throws Exception {
		ClzzKind classKind = ClzzKind.OT;
		CompilationUnit cu = ParseUtil.getCompilationUnit(classFile);
        String fileExt = FileUtil.getFileExt(classFile);
        String annotation = "";
        
		if("java".equals(fileExt)) {
			List<AnnotationExpr> annotationDeclarationList = cu.getType(0).getAnnotations();
            if(!annotationDeclarationList.isEmpty()) {
            	for(AnnotationExpr item : annotationDeclarationList) {
            		annotation = item.getNameAsString();
            		if("Controller".equals(annotation) || "RestController".equals(annotation)) {
            			classKind = ClzzKind.CT;
            		}else if("Service".equals(annotation)) {
            			classKind = ClzzKind.SV;
            		}else if("Repository".equals(annotation)) {
            			classKind = ClzzKind.DA;
            		}
            		if(!classKind.equals(ClzzKind.OT)) {
            			break;
            		}
            	}
            }
		}
		return classKind;
	}
	
	/**
	 * 어노테이션으로 표현된 리소스ID 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getResourceId(String classFile) throws Exception {
		String resourceId = "";
		
		CompilationUnit cu = ParseUtil.getCompilationUnit(classFile);
		
		List<AnnotationExpr> annotationExprList = cu.findAll(AnnotationExpr.class);
		if( annotationExprList != null ) {
			for( AnnotationExpr annotationExpr : annotationExprList) {
debug("annotationExpr===>>" + annotationExpr);
				List<Node> nodeList = annotationExpr.getChildNodes();
				for(Node node : nodeList){
debug("annotationExpr.node===>>" + node);
				}
				

				
//debug(classFile + " :: resourceId annotationExpr===>>>" +  annotationExpr );
//debug(classFile + " :: resourceId getChildNodes ===>>>" +  annotationExpr.getChildNodes() );
			    List<MemberValuePair>children = annotationExpr.findAll(MemberValuePair.class);
			    for(MemberValuePair memberValuePair : children){

//debug(classFile + " :: memberValuePair ===>>>" +  memberValuePair );
			    	if( "Controller".equals(memberValuePair.getNameAsString()) ) {
			    		resourceId = memberValuePair.getValue().toString();
			    	}else if( "Service".equals(memberValuePair.getNameAsString()) ) {
			    		resourceId = memberValuePair.getValue().toString();
			    	}else if( "Repository".equals(memberValuePair.getNameAsString()) ) {
			    		resourceId = memberValuePair.getValue().toString();
			    	}
			    	if(!StringUtil.isEmpty(resourceId)) {
			    		break;
			    	}
			    }
		    	if(!StringUtil.isEmpty(resourceId)) {
		    		break;
		    	}
			}
		}

//debug(classFile + " :: resourceId ===>>>" +  resourceId );
		
		return super.getResourceId(classFile);
	}
	
	/**
	 * 클래스or인터페이스(C:클래스/I:인터페이스) 추출
	 * @param classFile
	 * @return
	 */
	@Override
	public String getClassOrInterface(String classFile) throws Exception{
		return super.getClassOrInterface(classFile);
	}

	/**
	 * 상위인터페이스 클래스ID 추출.(인터페이스를 구현한 클래스의 경우에만 존재)
	 * @param classFile
	 * @return
	 */
	@Override
	public String getInterfaceId(String classFile) throws Exception{
		return super.getInterfaceId(classFile);
	}
	/**
	 * 상위(부모)클래스ID 추출.
	 * @param classFile
	 */
	@Override
	public String getParentClassId(String classFile) throws Exception {
		return super.getParentClassId(classFile);
	}

	/**
	 * 인터페이스구현하위클래스ID목록 추출.(인터페이스인 경우에만 존재)
	 * @param selfClzzVo
	 * @param analyzedClassFileList
	 * @return
	 */
	public List<String> getImplClassIdList(ClzzVo selfClzzVo, String[] otherClassFileList) throws Exception{
		return super.getImplClassIdList(selfClzzVo, otherClassFileList);
	}
	
	/**
	 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
	 * @param selfClzzVo
	 * @param analyzedClassFileList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<String, String>> getAllClassAlias(ClzzVo selfClzzVo, String[] analyzedClassFileList) throws Exception {
		List<Map<String, String>> callClassAliasList = new ArrayList<Map<String, String>>();
		Map<String, String> callClassAlias = new HashMap<String, String>();
		if( !StringUtil.isEmpty(selfClzzVo.getFileName()) && FileUtil.isFileExist(selfClzzVo.getFileName()) ) {
			String fileConts = FileUtil.readFile(selfClzzVo.getFileName());

			boolean isUsed = false;
			CompilationUnit cu = ParseUtil.getCompilationUnit(selfClzzVo.getFileName());
			
	        HashMap<String, ImportDeclaration> importMap = new HashMap<String, ImportDeclaration>();
	        List<ImportDeclaration> imports = cu.getImports();
	        for(ImportDeclaration item : imports) {
	        	importMap.put(item.getNameAsString().substring(item.getNameAsString().lastIndexOf(".")+1), item);
	        }
	        
			String type = "";
			String alias = "";
			String resourceId = "";
	        for (TypeDeclaration typeDec : cu.getTypes()) {
	            List<BodyDeclaration> members = typeDec.getMembers();
	            if(members != null) {
	                for (BodyDeclaration  member : members) {
	            		type = "";
	            		alias = "";
	            		resourceId = "";
	            		isUsed = false;
	                	if( member instanceof FieldDeclaration ) {
	                		FieldDeclaration field = ((FieldDeclaration) member);
	                		List<VariableDeclarator> fieldVariableDeclaratorList = field.getVariables();
	                        for (VariableDeclarator variable : fieldVariableDeclaratorList) {

	                        	//Print the field's class typr
	                        	type = variable.getType().asString();
	                        	if(type.indexOf(".") == -1) {
	                                if(importMap.containsKey(type) ) {
	                                	type = importMap.get(type).getNameAsString();
	                                }
	                        	}
	                            //Print the field's name
	                            alias = variable.getName().asString();
	                                
	                            //Print the field's annotation name
								if(member.getAnnotations().isNonEmpty()) { 
									String varType = variable.getTypeAsString();
									// variable의 타입과 필드의 타입이 동일한 경우 해당 필드의 어노테이션을 가지고 와서 resourceId를 구해낸다.
									if( varType.indexOf(".")>-1 && type.equals(varType) || varType.indexOf(".")==-1 && type.endsWith("."+varType) ) {
										for(AnnotationExpr an : field.getAnnotations()) {  
						            		/***************************************************
						            		<SingleMemberAnnotation/NormalAnnotation 의 차이>
						            		- @RequestMapping("/sample/admin.do")       ===>> SingleMemberAnnotation
						            		- @RequestMapping(value="/sample/admin.do") ===>> NormalAnnotation
						            		***************************************************/
											if( an.isSingleMemberAnnotationExpr() ) {
												resourceId = an.asSingleMemberAnnotationExpr().getMemberValue().asStringLiteralExpr().asString();
											}else if( an.isNormalAnnotationExpr() ) {
												resourceId = an.asNormalAnnotationExpr().getPairs().get(0).getValue().asStringLiteralExpr().asString();
											}
										}
									}
								}

	                        }
	                	}else if( member instanceof MethodDeclaration ) {
	                		MethodDeclaration method = ((MethodDeclaration) member);
	                		List<VariableDeclarator> methodVariableDeclaratorList = method.findAll(VariableDeclarator.class);
	                        for (VariableDeclarator variable : methodVariableDeclaratorList) {
	                            //Print the field's class typr
	                            type = variable.getType().asString();
	                        	if(type.indexOf(".") == -1) {
	                                if(importMap.containsKey(type) ) {
	                                	type = importMap.get(type).getNameAsString();
	                                }
	                        	}
	                            //Print the field's name
	                            alias = variable.getName().asString();
	                        }
	                	}
	                	if(!StringUtil.isEmpty(alias)) {
	                		if( !SvcAnalyzer.isValidSvcPackage(type) ) {
	                			continue;
	                		}
    						callClassAlias = new HashMap<String, String>();
    						//LogUtil.sysout( "type["+type+"]" + " resourceId["+resourceId+"]" + " findImplClassId["+ParseUtil.findImplClassId(type, resourceId)+"]" + " alias["+alias+"]" );
    						// packageClassId 가 인터페이스 일 경우 구현클래스ID를 구한다.
    						callClassAlias.put("FULL_CLASS", ParseUtil.findImplClassId(type, resourceId));
    						callClassAlias.put("ALIAS", alias);
    						if( !callClassAliasList.contains(callClassAlias) ) {
    							callClassAliasList.add(callClassAlias);
    						}
	                	}
	                }
	            }
	        }
			// 부모클래스가 존재할 경우 부모클래스의 호출알리아스도 가져와서 합쳐준다.
	        if(!StringUtil.isEmpty(selfClzzVo.getParentClassId())) {
	        	ClzzVo parentClzzVo = ParseUtil.readClassVo(selfClzzVo.getParentClassId(), AppAnalyzer.WRITE_PATH + "/class");	
	        	callClassAliasList.addAll(this.getAllClassAlias(parentClzzVo, analyzedClassFileList));
	        }
		}

		return callClassAliasList;
	}
	
	/**
	 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
	 * @param selfClzzVo
	 * @param analyzedClassFileList
	 * @return
	 */
	@Override
	public List<Map<String, String>> getCallClassAlias(ClzzVo selfClzzVo, String[] analyzedClassFileList) throws Exception {
		List<Map<String, String>> callClassAliasList = new ArrayList<Map<String, String>>();
		if( !StringUtil.isEmpty(selfClzzVo.getFileName()) && FileUtil.isFileExist(selfClzzVo.getFileName()) ) {
			List<Map<String, String>> allClassAliasList = this.getAllClassAlias(selfClzzVo, analyzedClassFileList);
			if( allClassAliasList != null ) {
				String fileConts = FileUtil.readFile(selfClzzVo.getFileName());
				String type = "";
				String alias = "";
				boolean isUsed = false;
				for(Map<String, String> callClassAlias : allClassAliasList) {
					isUsed = false;
					type = callClassAlias.get("FULL_CLASS");
					alias = callClassAlias.get("ALIAS");
	            	if(!StringUtil.isEmpty(alias)) {
	            		if( !SvcAnalyzer.isValidSvcPackage(type) ) {
	            			continue;
	            		}
	            		// 변수명.메소드  or 변수Getter.메소드 의 형태가 존재할 경우 사용된걸로 간주한다.
						if (fileConts.indexOf(alias + ".")>-1 || fileConts.indexOf(ParseUtil.getGetterNmFromField(alias) + ".")>-1) {
							isUsed = true;
						}
						if(isUsed) {
							if( !callClassAliasList.contains(callClassAlias) ) {
								callClassAliasList.add(callClassAlias);
							}
						}
	            	}
				}
			}
		}
		return callClassAliasList;
	}


	private static CombinedTypeSolver setClassPath() {
    	CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
    	try {
    		
    		String jdkHome = AppAnalyzer.CONF.getNode("APP_JDK_HOME").getTextContent();
    		if(!StringUtil.isEmpty(jdkHome)) {
    			jdkHome = StringUtil.replace(jdkHome, "\\", "/");
    			combinedTypeSolver.add(new JarTypeSolver(new File( jdkHome + "/jre/lib/rt.jar")));
    		}
    		
    		String classPathStr = AppAnalyzer.CONF.getNode("APP_CLASSPATH").getTextContent();
    		if(!StringUtil.isEmpty(classPathStr)) {
    			classPathStr = StringUtil.replace(classPathStr, "\t", "");
    			classPathStr = StringUtil.replace(classPathStr, "\n", "");
    			String[] classPathArr = StringUtil.toStrArray(classPathStr, ";");
    			for(String classPath : classPathArr) {
    				combinedTypeSolver.add(new JarTypeSolver(new File(classPath)));
    			}
    		}

    		combinedTypeSolver.add(new JavaParserTypeSolver(new File(AppAnalyzer.CLASS_ROOT_PATH)));

    		combinedTypeSolver.add(new ReflectionTypeSolver());

		} catch (Exception e) {
			e.printStackTrace();
		}

    	return combinedTypeSolver;
    }
    
}
