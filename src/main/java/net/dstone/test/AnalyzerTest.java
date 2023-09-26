package net.dstone.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;

public class AnalyzerTest {

    public static void main(String[] args) throws IOException {
    	
    	
        String srcRoot = "D:/AppHome/framework/dstone-boot/src/main/java";
    	String path = srcRoot + "/" + "net/dstone/sample/UserController.java";
    	
    	System.out.println("================ START ================");
    	
        JavaSymbolSolver javaSymbolSolver = new JavaSymbolSolver(new JavaParserTypeSolver(new File(srcRoot)));
        StaticJavaParser.getParserConfiguration().setSymbolResolver(javaSymbolSolver);
        
        CompilationUnit cu = StaticJavaParser.parse(new File(path));

        MethodVisitor visitor = new MethodVisitor();
        List<MethodDeclaration> methodDeclarationList = new ArrayList<>();
        visitor.visit(cu, methodDeclarationList);
        
        for(MethodDeclaration md : methodDeclarationList) {
        	System.out.println(md.getDeclarationAsString());

        	System.out.println(md.getBody());
        }
        

//        List<MethodCallExpr> methodCallExprList = cu.findAll(MethodCallExpr.class);
//
//        for(MethodCallExpr me : methodCallExprList) {
//        	System.out.println(me.resolve().getQualifiedSignature());
//        }
        
    	System.out.println("================ END ================");
    }
}
