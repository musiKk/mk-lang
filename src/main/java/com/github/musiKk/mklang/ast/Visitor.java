package com.github.musiKk.mklang.ast;

import com.github.musiKk.mklang.ast.Expression.Binary;
import com.github.musiKk.mklang.ast.Expression.Block;
import com.github.musiKk.mklang.ast.Expression.FunctionCall;
import com.github.musiKk.mklang.ast.Expression.IntConstant;
import com.github.musiKk.mklang.ast.Expression.StringConstant;

public interface Visitor {

    public void visitCompilationUnit(CompilationUnit cu);

    public void visitDataDeclarationStatement(DataDeclarationStatement dataDeclarationStatement);

    public void visitBinaryExpression(Binary binary);

    public void visitIntConstant(IntConstant intConstant);

    public void visitStringConstant(StringConstant stringConstant);

    public void visitFunctionCallExpression(FunctionCall functionCall);

    public void visitVariableDeclaration(VariableDeclaration variableDeclaration);

    public void visitVariableAssignment(VariableAssignment vs);

    public void visitBlock(Block block);

    public class Adapter implements Visitor {

        @Override
        public void visitCompilationUnit(CompilationUnit cu) {
        }

        @Override
        public void visitDataDeclarationStatement(DataDeclarationStatement dataDeclarationStatement) {
        }

        @Override
        public void visitBinaryExpression(Binary binary) {
        }

        @Override
        public void visitIntConstant(IntConstant intConstant) {
        }

        @Override
        public void visitStringConstant(StringConstant stringConstant) {
        }

        @Override
        public void visitFunctionCallExpression(FunctionCall functionCall) {
        }

        @Override
        public void visitBlock(Block block) {
        }

        @Override
        public void visitVariableAssignment(VariableAssignment vs) {
        }

        @Override
        public void visitVariableDeclaration(VariableDeclaration variableDeclaration) {
        }

    }


}
