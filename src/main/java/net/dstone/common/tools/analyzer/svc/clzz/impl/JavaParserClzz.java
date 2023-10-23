package net.dstone.common.tools.analyzer.svc.clzz.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.svc.SvcAnalyzer;
import net.dstone.common.tools.analyzer.svc.clzz.Clzz;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class JavaParserClzz extends DefaultClzz implements Clzz {

	/**
	 * 패키지ID 추출
	 * @param classFile 클래스파일
	 * @return
	 */
	@Override
	public String getPackageId(String classFile) throws Exception {
		String pkg = "";
		CompilationUnit cu = StaticJavaParser.parse(new File(classFile));
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
		CompilationUnit cu = StaticJavaParser.parse(new File(classFile));
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
		CompilationUnit cu = StaticJavaParser.parse(new File(classFile));
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
		CompilationUnit cu = StaticJavaParser.parse(new File(classFile));
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
	@Override
	public List<Map<String, String>> getCallClassAlias(ClzzVo selfClzzVo, String[] analyzedClassFileList) throws Exception {
		List<Map<String, String>> callClassAliasList = new ArrayList<Map<String, String>>();
		Map<String, String> callClassAlias = new HashMap<String, String>();
		String fileConts = FileUtil.readFile(selfClzzVo.getFileName());
		boolean isUsed = false;
		CompilationUnit cu = StaticJavaParser.parse(new File(selfClzzVo.getFileName()));
		
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
                                }else {
                                	type = cu.getPackageDeclaration().get().getNameAsString()+"."+type;
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
                                }else {
                                	type = cu.getPackageDeclaration().get().getNameAsString()+"."+type;
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
    					if (fileConts.indexOf(alias + ".")>-1) {
    						isUsed = true;
    					}
    					if(isUsed) {
    						callClassAlias = new HashMap<String, String>();
    						
    						callClassAlias.put("FULL_CLASS", ParseUtil.findImplClassId(type, resourceId));
    						//callClassAlias.put("FULL_CLASS", type);
    						
    						callClassAlias.put("ALIAS", alias);
                    		callClassAliasList.add(callClassAlias);
    					}
                	}
                }
            }
        }
		return callClassAliasList;
	}

}
