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
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;

import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.svc.SvcAnalyzer;
import net.dstone.common.tools.analyzer.svc.clzz.Clzz;
import net.dstone.common.tools.analyzer.util.ParseUtil;
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
		if("jsp".equals(fileExt)) {
			classKind = ClzzKind.UI;
		}else if("js".equals(fileExt)) {
			classKind = ClzzKind.JS;
		}else if("java".equals(fileExt)) {
            List<MarkerAnnotationExpr> annotationDeclarationList = cu.findAll(MarkerAnnotationExpr.class);
            if(!annotationDeclarationList.isEmpty()) {
            	for(MarkerAnnotationExpr item : annotationDeclarationList) {
            		String annotation = item.getNameAsString();
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
	 * 호출알리아스 추출. 리스트<맵>을 반환. 맵항목- Full클래스,알리아스 .(예: FULL_CLASS:aaa.bbb.Clzz2, ALIAS:clzz2)
	 * @param classFile
	 * @param analyzedClassFileList
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map<String, String>> getCallClassAlias(String classFile, String[] analyzedClassFileList) throws Exception {
		List<Map<String, String>> callClassAliasList = new ArrayList<Map<String, String>>();
		Map<String, String> callClassAlias = new HashMap<String, String>();
		String fileConts = FileUtil.readFile(classFile);
		boolean isUsed = false;
		CompilationUnit cu = StaticJavaParser.parse(new File(classFile));

        HashMap<String, ImportDeclaration> importMap = new HashMap<String, ImportDeclaration>();
        List<ImportDeclaration> imports = cu.getImports();
        for(ImportDeclaration item : imports) {
        	importMap.put(item.getNameAsString().substring(item.getNameAsString().lastIndexOf(".")+1), item);
        }
		String type = "";
		String alias = "";
        for (TypeDeclaration typeDec : cu.getTypes()) {
            List<BodyDeclaration> members = typeDec.getMembers();
            if(members != null) {
                for (BodyDeclaration  member : members) {
            		type = "";
            		alias = "";
            		isUsed = false;
                	if( member instanceof FieldDeclaration ) {
                		FieldDeclaration field = ((FieldDeclaration) member);
                		List<VariableDeclarator> fieldVariableDeclaratorList = field.getVariables();
                        for (VariableDeclarator variable : fieldVariableDeclaratorList) {
                            //Print the field's class typr
                            type = variable.getType().asString();
                            if(type.indexOf(".") == -1 && importMap.containsKey(type) ) {
                            	type = importMap.get(type).getNameAsString();
                            }
                            //Print the field's name
                            alias = variable.getName().asString();
                        }
                	}else if( member instanceof MethodDeclaration ) {
                		MethodDeclaration method = ((MethodDeclaration) member);
                		List<VariableDeclarator> methodVariableDeclaratorList = method.findAll(VariableDeclarator.class);
                        for (VariableDeclarator variable : methodVariableDeclaratorList) {
                            //Print the field's class typr
                            type = variable.getType().asString();
                            if(type.indexOf(".") == -1 && importMap.containsKey(type) ) {
                            	type = importMap.get(type).getNameAsString();
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
    						callClassAlias.put("FULL_CLASS", type);
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
