package net.dstone.test;

import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<List<MethodDeclaration>> {
    @Override
    public void visit(MethodDeclaration md, List<MethodDeclaration> collector) {
        super.visit(md, collector);
        collector.add(md);
    }
}
