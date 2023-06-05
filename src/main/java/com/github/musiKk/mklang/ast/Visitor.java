package com.github.musiKk.mklang.ast;

import com.github.musiKk.mklang.ast.Expression.Binary;
import com.github.musiKk.mklang.ast.Expression.Block;
import com.github.musiKk.mklang.ast.Expression.FunctionCall;
import com.github.musiKk.mklang.ast.Expression.IntConstant;
import com.github.musiKk.mklang.ast.Expression.StringConstant;

public interface Visitor {

    void visitCompilationUnit(CompilationUnit cu);

    void visitDataDeclaration(DataDeclaration dataDeclaration);

    void visitTraitDeclaration(TraitDeclaration traitDeclaration);

    void visitBinaryExpression(Binary binary);

    void visitIntConstant(IntConstant intConstant);

    void visitStringConstant(StringConstant stringConstant);

    void visitFunctionCallExpression(FunctionCall functionCall);

    void visitVariableDeclaration(VariableDeclaration variableDeclaration);

    void visitVariableAssignment(VariableAssignment vs);

    void visitBlock(Block block);

    void visitFunctionDeclaration(FunctionDeclaration functionDeclaration);

    public class Adapter implements Visitor {

        @Override
        public void visitCompilationUnit(CompilationUnit cu) {
        }

        @Override
        public void visitDataDeclaration(DataDeclaration dataDeclaration) {
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

        @Override
        public void visitTraitDeclaration(TraitDeclaration traitDeclaration) {
        }

        @Override
        public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) {
        }

    }


}
