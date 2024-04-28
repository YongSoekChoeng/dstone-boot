package net.dstone.common.tools.analyzer.svc.mtd.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;

import net.dstone.common.tools.analyzer.AppAnalyzer;
import net.dstone.common.tools.analyzer.svc.mtd.ParseMtd;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.tools.analyzer.vo.ClzzVo;
import net.dstone.common.tools.analyzer.vo.MtdVo;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class JavaParseMtd extends TextParseMtd implements ParseMtd {

	/**
	 * 파일로부터 메소드ID/메소드명/메소드URL/메소드내용 이 담긴 메소드정보목록 추출
	 * LIST[ MAP<메서드ID(METHOD_ID), 메서드명(METHOD_NAME), 메서드URL(METHOD_URL), 메서드바디(METHOD_BODY)> ]
	 * @param classFile
	 * @return
	 */
	@Override
	public List<Map<String, String>> getMtdInfoList(String classFile) throws Exception {
		
		List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
		
		CompilationUnit cu = ParseUtil.getCompilationUnit(classFile);

    	// classUrl
        String classUrl = "";
        if(cu.findFirst(ClassOrInterfaceDeclaration.class).isPresent()) {
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = cu.findFirst(ClassOrInterfaceDeclaration.class).get();
            List<AnnotationExpr> annotationList = classOrInterfaceDeclaration.getAnnotations();
            for(AnnotationExpr an : annotationList) {
            	if(an.getNameAsString().endsWith("Mapping")) {
            		/***************************************************
            		<SingleMemberAnnotation/NormalAnnotation 의 차이>
            		- @RequestMapping("/sample/admin.do")       ===>> SingleMemberAnnotation
            		- @RequestMapping(value="/sample/admin.do") ===>> NormalAnnotation
            		***************************************************/
            		if( an.isSingleMemberAnnotationExpr() ) {
            			classUrl = an.asSingleMemberAnnotationExpr().getMemberValue().asStringLiteralExpr().asString();
            		}else if( an.isNormalAnnotationExpr() ) {
            			classUrl = an.asNormalAnnotationExpr().getPairs().get(0).getValue().asStringLiteralExpr().asString();
            		}
            	}
            }
            if(!StringUtil.isEmpty(classUrl)) {
            	if(classUrl.endsWith("/*")) {
            		classUrl = classUrl.substring(0, classUrl.length()-2);
            	}else if(classUrl.endsWith("/")) {
            		classUrl = classUrl.substring(0, classUrl.length()-1);
            	}
            }

            List<MethodDeclaration> methodDeclarationList = cu.findAll(MethodDeclaration.class);
            for(MethodDeclaration methodDec : methodDeclarationList) {
            	Map<String, String> item = new HashMap<String, String>();
            	
            	// FUNCTION_ID
                String FUNCTION_ID = methodDec.resolve().getQualifiedSignature();
            	item.put("FUNCTION_ID", FUNCTION_ID);
            	
            	// CLASS_ID
                String CLASS_ID = classOrInterfaceDeclaration.resolve().getQualifiedName();
            	item.put("CLASS_ID", CLASS_ID);

            	// METHOD_ID
                String METHOD_ID = methodDec.getNameAsString();
            	item.put("METHOD_ID", METHOD_ID);
            	
            	// METHOD_NAME
                String METHOD_NAME = "";
            	if(methodDec.getJavadocComment().isPresent()) {
            		METHOD_NAME = ParseUtil.getFnNameFromComment(methodDec.getJavadocComment().get().asString());
            	}
            	item.put("METHOD_NAME", METHOD_NAME);
            	
            	// METHOD_URL
                String METHOD_URL = "";
                List<AnnotationExpr> methodAnnotationList =  methodDec.getAnnotations();
                for(AnnotationExpr an : methodAnnotationList) {
                	if(an.getNameAsString().endsWith("Mapping")) {
                		/***************************************************
                		<SingleMemberAnnotation/NormalAnnotation 의 차이>
                		- @RequestMapping("/sample/admin.do")       ===>> SingleMemberAnnotation
                		- @RequestMapping(value="/sample/admin.do") ===>> NormalAnnotation
                		***************************************************/
                		if( an.isSingleMemberAnnotationExpr() ) {
                			METHOD_URL = an.asSingleMemberAnnotationExpr().getMemberValue().asStringLiteralExpr().asString();
                		}else if( an.isNormalAnnotationExpr() ) {           			
                			if (an.asNormalAnnotationExpr().getPairs().get(0).getValue().isStringLiteralExpr()) {
                				METHOD_URL = an.asNormalAnnotationExpr().getPairs().get(0).getValue().asStringLiteralExpr().asString();
                			}else if (an.asNormalAnnotationExpr().getPairs().get(0).getValue().isArrayInitializerExpr()) {
                				List<Node> nodes = an.asNormalAnnotationExpr().getPairs().get(0).getValue().asArrayInitializerExpr().getChildNodes();
                				if(nodes != null) {
                		            for (Node node : nodes) {
                		                if (node instanceof StringLiteralExpr) {
                		                	METHOD_URL = ((StringLiteralExpr) node).getValue();
                		                	break;
                		                }
                		            }
                				}
                			}
                		}
                	}
                }
                if(!StringUtil.isEmpty(METHOD_URL)) {
                	if(!METHOD_URL.startsWith("/")) {
                		METHOD_URL = "/" + METHOD_URL;
                	}
                	METHOD_URL = classUrl + METHOD_URL;
                }
            	item.put("METHOD_URL", METHOD_URL);
            	// METHOD_BODY
            	String METHOD_BODY = "";
            	if( methodDec.getBody() != null && methodDec.getBody().isPresent() ) {
            		METHOD_BODY = StringUtil.trimTextForParse(methodDec.getBody().get().toString());
            	}
        		String[] lines = StringUtil.toStrArray(METHOD_BODY, "\n");
        		METHOD_BODY = "";
        		for(String line : lines) {
        			METHOD_BODY = METHOD_BODY + line.trim() + "\n";
        		}
            	item.put("METHOD_BODY", METHOD_BODY);
            	
            	mList.add(item);
            }
        }
        
		return mList;
	}

	/**
	 * 호출메소드 목록 추출
	 * @param clzzVo
	 * @param analyzedMethodFile
	 * @return
	 */
	@Override
	public List<String> getCallMtdList(String analyzedMethodFile) throws Exception {
		
		List<String> callsMtdList = new ArrayList<String>();
		
		/*** 메소드VO 정보 획득  ***/
		String functionId = FileUtil.getFileName(analyzedMethodFile, false);
		MtdVo mtdVo = ParseUtil.readMethodVo(functionId, AppAnalyzer.WRITE_PATH + "/method");
		
		/*** 클래스VO 정보 획득  ***/
		ClzzVo clzzVo = ParseUtil.readClassVo(mtdVo.getClassId(), AppAnalyzer.WRITE_PATH + "/class");

		/*** 클래스멤버-알리아스 정보 획득  ***/
		/* 반환리스트 형태
		 {
		    [<FULL_CLASS:aaa.bbb.Clzz1, 		ALIAS:alias1>]
		  , [<FULL_CLASS:aaa.bbb.Clzz2, 		ALIAS:alias2>]
		  , [<FULL_CLASS:aaa.bbb.Clzz3, 		ALIAS:alias3>]
		  , [<FULL_CLASS:aaa.bbb.Clzz4Impl1, 	ALIAS:alias4>]
		  , [<FULL_CLASS:aaa.bbb.Clzz4Impl2, 	ALIAS:alias4>]
		 }		
		*/
		List<Map<String, String>> callClassAliasList = clzzVo.getCallClassAlias();
		
		/*** 메서드 AST 조회  ***/
		MethodDeclaration mtdDec = ParseUtil.getMethodDec(AppAnalyzer.CLASS_ROOT_PATH, mtdVo.getFunctionId());

		if(mtdDec != null) {
			
			/*** 메서드멤버-알리아스 정보 획득  ***/
			HashMap<String, ArrayList<ClassOrInterfaceDeclaration>> mtdMemberMap = new HashMap<String, ArrayList<ClassOrInterfaceDeclaration>>(); 
			List<VariableDeclarationExpr> varList = mtdDec.findAll(VariableDeclarationExpr.class);
			for (VariableDeclarationExpr var: varList) {
				String name = var.getVariable (0).getNameAsString(); 
				ClassOrInterfaceDeclaration valClzz = ParseUtil.getClassDec(AppAnalyzer.WRITE_PATH, var.calculateResolvedType().describe());
				if( valClzz != null) {
					if( !valClzz.isInterface() && !valClzz.isAbstract()) {
						if(!mtdMemberMap.containsKey(name)) {
							mtdMemberMap.put(name, new ArrayList<ClassOrInterfaceDeclaration>());
						}
						ArrayList<ClassOrInterfaceDeclaration> valClzzList = mtdMemberMap.get(name);
						valClzzList.add(valClzz);
					}
				}
			}
			/*** 메서드멤버생성 목록조회 ***/
			
			/*** 메서드 호출 목록조회 ***/
			
		}
		
		return super.getCallMtdList(analyzedMethodFile);
	}

	/**
	 * 호출테이블 목록 추출
	 * 파일에서 매칭되는 queryKey를 찾아내어 해당 [쿼리정보.테이블목록]을 읽어온 후 [메소드정보.호출테이블]에 저장.
	 * @param analyzedMethodFile
	 * @return
	 */
	@Override
	public List<String> getCallTblList(String analyzedMethodFile) throws Exception {
		return super.getCallTblList(analyzedMethodFile);
	}

}
