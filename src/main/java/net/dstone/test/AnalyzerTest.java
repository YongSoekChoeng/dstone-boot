package net.dstone.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

import net.dstone.common.tools.analyzer.consts.ClzzKind;
import net.dstone.common.tools.analyzer.util.DirExplorer;
import net.dstone.common.tools.analyzer.util.ParseUtil;
import net.dstone.common.utils.FileUtil;
import net.dstone.common.utils.StringUtil;

public class AnalyzerTest {

    public static void main(String[] args) throws IOException {


    	System.out.println("================ START ================");

    	//test0();
    	//test1();
    	analizeFolder(new File("D:/AppHome/framework/dstone-boot/src/main/java/net/dstone/sample"));

    	System.out.println("================ END ================");
    }
    

	public static void test0() throws IOException {

		File projectDir = new File("D:/AppHome/framework/dstone-boot/src/main/java");
        analizeFolder(projectDir);

	}

    public static void test1() throws IOException {
    	
    	
        String srcRoot = "D:/AppHome/framework/dstone-boot/src/main/java";
//    	String path = srcRoot + "/" + "net/dstone/sample/AdminController.java";
    	String path = srcRoot + "/" + "net/dstone/sample/UserController.java";

        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(new JavaParserTypeSolver(new File(srcRoot)));
        StaticJavaParser.getParserConfiguration().setSymbolResolver(javaSymbolSolver);
        
        CompilationUnit cu = StaticJavaParser.parse(new File(path));

        List<MethodCallExpr> methodCallExprList = cu.findAll(MethodCallExpr.class);

        for(MethodCallExpr me : methodCallExprList) {
        	List<Node> nodeList = me.getChildNodes();
        	for(Node node : nodeList) {
        		System.out.println( node.getClass().getName());
        	}
//        	System.out.println(nodeList);
//        	System.out.println(me.resolve().getQualifiedSignature());
        }
        
    }
    
    
    
    

    public static void analizeFolder(File projectDir) {
    	
    	net.dstone.common.tools.analyzer.AppAnalyzer.INCLUDE_PACKAGE_ROOT = new String[1];
    	net.dstone.common.tools.analyzer.AppAnalyzer.INCLUDE_PACKAGE_ROOT[0] = "net.dstone.sample";
    	net.dstone.common.tools.analyzer.AppAnalyzer.EXCLUDE_PACKAGE_PATTERN = new String[3];
    	net.dstone.common.tools.analyzer.AppAnalyzer.EXCLUDE_PACKAGE_PATTERN[0] = ".vo.";
    	net.dstone.common.tools.analyzer.AppAnalyzer.EXCLUDE_PACKAGE_PATTERN[1] = "vo.";
    	net.dstone.common.tools.analyzer.AppAnalyzer.EXCLUDE_PACKAGE_PATTERN[2] = ".vo";
    	
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level, path, file) -> {
            System.out.println("============================" + path + "============================");
//            System.out.println(Strings.repeat("=", path.length()));
            try {
                CompilationUnit cu = StaticJavaParser.parse(file);

                // packageId
                String packageId = cu.getPackageDeclaration().get().getNameAsString();
                System.out.println("packageId[" + packageId + "]");
                
                // classId
                System.out.println("classId[" + cu.getType(0).getNameAsString() + "]");
                
                // className
                String className = "";
                if(cu.getType(0).getJavadocComment().isPresent()) {
                	className = ParseUtil.getFnNameFromComment(cu.getType(0).getJavadocComment().get().asString());
                }
                System.out.println("className[" + className + "]");
                
                // classKind
                ClzzKind classKind = ClzzKind.OT;
                String fileExt = FileUtil.getFileExt(path);
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
                System.out.println("classKind[" + classKind + "]");
                
                // callClassAlias
                List<Map<String, String>> callClassAlias = new ArrayList<Map<String, String>>();
                HashMap<String, ImportDeclaration> importMap = new HashMap<String, ImportDeclaration>();
                List<ImportDeclaration> imports = cu.getImports();
                for(ImportDeclaration item : imports) {
                	importMap.put(item.getNameAsString().substring(item.getNameAsString().lastIndexOf(".")+1), item);
                }
        		String type = "";
        		String alias = "";
                for (TypeDeclaration typeDec : cu.getTypes()) {
                    List<BodyDeclaration > members = typeDec.getMembers();
                    if(members != null) {
                        for (BodyDeclaration  member : members) {
                    		type = "";
                    		alias = "";
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
                        		Map<String, String> aliasMap = new HashMap<String, String>();
                        		if( !net.dstone.common.tools.analyzer.svc.SvcAnalyzer.isValidSvcPackage(type) ) {
                        			continue;
                        		}
                        		aliasMap.put("FULL_CLASS", type);
                        		aliasMap.put("ALIAS", alias);
                        		callClassAlias.add(aliasMap);
                        	}
	                    }
	                }
                }
                System.out.println("callClassAlias:" + callClassAlias);

            	// classUrl
                String classUrl = "";
                ClassOrInterfaceDeclaration classOrInterfaceDeclaration = cu.findFirst(ClassOrInterfaceDeclaration.class).get();
                List<AnnotationExpr> annotationList = classOrInterfaceDeclaration.getAnnotations();
                for(AnnotationExpr an : annotationList) {
                	if(an.getNameAsString().endsWith("Mapping")) {
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
                //System.out.println(cu.getType(0).getNameAsString() + ":classUrl===>>>" + classUrl );
                
                List<MethodDeclaration> methodDeclarationList = cu.findAll(MethodDeclaration.class);
                for(MethodDeclaration item : methodDeclarationList) {
                	// METHOD_ID
                	System.out.println("METHOD_ID:" + item.getNameAsString());
                	// METHOD_NAME
                	if(item.getJavadocComment().isPresent()) {
                		System.out.println("METHOD_NAME:" + ParseUtil.getFnNameFromComment(item.getJavadocComment().get().asString()));
                	}
                	// METHOD_URL
                    String METHOD_URL = "";
                    List<AnnotationExpr> methodAnnotationList =  item.getAnnotations();
                    for(AnnotationExpr an : methodAnnotationList) {
                    	if(an.getNameAsString().endsWith("Mapping")) {
                    		if( an.isSingleMemberAnnotationExpr() ) {
                    			METHOD_URL = an.asSingleMemberAnnotationExpr().getMemberValue().asStringLiteralExpr().asString();
                    		}else if( an.isNormalAnnotationExpr() ) {
                    			METHOD_URL = an.asNormalAnnotationExpr().getPairs().get(0).getValue().asStringLiteralExpr().asString();
                    		}
                    	}
                    }
                    if(!StringUtil.isEmpty(METHOD_URL)) {
                    	if(!METHOD_URL.startsWith("/")) {
                    		METHOD_URL = "/" + METHOD_URL;
                    	}
                    	METHOD_URL = classUrl + METHOD_URL;
                    }
                    System.out.println("METHOD_URL:" + METHOD_URL);


                	// METHOD_BODY
                	String conts = StringUtil.trimTextForParse(item.getBody().get().toString());
            		String[] lines = StringUtil.toStrArray(conts, "\n");
            		conts = "";
            		for(String line : lines) {
            			conts = conts + line.trim() + "\n";
            		}
                	//System.out.println("METHOD_BODY:" + conts);
                	
                }

                List<MethodCallExpr> methodCallExprList = cu.findAll(MethodCallExpr.class);
                for(MethodCallExpr item : methodCallExprList) {
                	//System.out.println("item.getNameAsString():" + item.getNameAsString());
                }
                
            } catch (Exception e) {
            	e.printStackTrace();
                new RuntimeException(e);
            }
        }).explore(projectDir);
    }
}
