
package com.example.codesmelldetector;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.HashSet;
import java.util.Set;

public class NodeSerializer {

    public static Set<String> serializeNodes(Node node) {
        Set<String> nodesSet = new HashSet<>();
        node.accept(new ContentNodeVisitor(), nodesSet);
        return nodesSet;
    }

    private static class ContentNodeVisitor extends VoidVisitorAdapter<Set<String>> {

        @Override
        public void visit(MethodDeclaration n, Set<String> arg) {
            n.getBody().ifPresent(body -> body.accept(this, arg));
        }


        @Override
        public void visit(ExpressionStmt n, Set<String> arg) {
            super.visit(n, arg);
           arg.add("ExpressionStmt:" + n.getExpression().toString());
        }

        @Override
        public void visit(VariableDeclarationExpr n, Set<String> arg) {
            super.visit(n, arg);
            n.getVariables().forEach(v -> {
                String varRepresentation = "VarDecl:" + v.getType().asString() + "=" + (v.getInitializer().isPresent() ? v.getInitializer().get().toString() : "none");
                arg.add(varRepresentation);
            });
        }

        @Override
        public void visit(MethodCallExpr n, Set<String> arg) {
            super.visit(n, arg);
            String methodCallRepresentation = "MethodCall:" + n.getNameAsString() + "(" +
                    n.getArguments().stream().map(Node::toString).reduce((a, b) -> a + ", " + b).orElse("") + ")";
            arg.add(methodCallRepresentation);
        }

        @Override
        public void visit(ReturnStmt n, Set<String> arg) {
            super.visit(n, arg);
            n.getExpression().ifPresent(expr -> arg.add("Return:" + expr.toString()));
        }

        @Override
        public void visit(BlockStmt n, Set<String> arg) {
            super.visit(n, arg);
        }

        @Override
        public void visit(TryStmt n, Set<String> arg) {
            super.visit(n, arg);
            StringBuilder tryRepresentation = new StringBuilder("TryBlock:");
            tryRepresentation.append("{");
            n.getTryBlock().getStatements().forEach(stmt -> tryRepresentation.append(stmt.toString()).append("; "));
            tryRepresentation.append("}");

            for (CatchClause cc : n.getCatchClauses()) {
                tryRepresentation.append(" Catch:").append(cc.getParameter()).append("{").append(cc.getBody()).append("}");
            }

            n.getFinallyBlock().ifPresent(finallyBlock -> tryRepresentation.append(" Finally:{").append(finallyBlock).append("}"));

            arg.add(tryRepresentation.toString());
        }
    }
}
