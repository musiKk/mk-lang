package com.github.musiKk.mklang.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface Expression extends Statement {

    @Data
    @AllArgsConstructor
    public static class Block implements Expression {
        List<Statement> statements = new ArrayList<>();
        public String toString() {
            StringBuilder sb = new StringBuilder("Block(\n");
            for (Statement e : statements) {
                sb.append(" " + e + "\n");
            }
            sb.append(")");
            return sb.toString();
        }

        public void accept(Visitor visitor) {
            visitor.visitBlock(this);
        }
    }

    public interface Binary extends Expression {
        String getOp();
        Expression getLeft();
        Expression getRight();
        @Override
        default void accept(Visitor visitor) {
            visitor.visitBinaryExpression(this);
        }
    }

    @Data
    @AllArgsConstructor
    public static class Additive implements Binary {
        String op;
        Expression left;
        Expression right;
    }
    @Data
    @AllArgsConstructor
    public static class Multiplicative implements Binary {
        String op;
        Expression left;
        Expression right;
    }
    @Data
    @AllArgsConstructor
    public static class IntConstant implements Expression {
        int value;
        public void accept(Visitor visitor) {
            visitor.visitIntConstant(this);
        }
    }
    @Data
    @AllArgsConstructor
    public static class StringConstant implements Expression {
        String value;
        public void accept(Visitor visitor) {
            visitor.visitStringConstant(this);
        }
    }
    @Data
    @AllArgsConstructor
    public static class FunctionCall implements Expression {
        DottedName name;
        List<Expression> args;
        public void accept(Visitor visitor) {
            visitor.visitFunctionCallExpression(this);
        }
    }
}
