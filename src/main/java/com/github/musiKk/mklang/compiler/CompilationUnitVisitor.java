package com.github.musiKk.mklang.compiler;

import java.util.HashMap;
import java.util.Map;

import com.github.musiKk.mklang.ast.CompilationUnit;
import com.github.musiKk.mklang.ast.DataDeclaration;
import com.github.musiKk.mklang.ast.Expression;
import com.github.musiKk.mklang.ast.Expression.Binary;
import com.github.musiKk.mklang.ast.Expression.FunctionCall;
import com.github.musiKk.mklang.ast.Expression.IntConstant;
import com.github.musiKk.mklang.ast.Expression.StringConstant;
import com.github.musiKk.mklang.ast.FunctionDeclaration;
import com.github.musiKk.mklang.ast.Statement;
import com.github.musiKk.mklang.ast.Visitor;
import com.github.musiKk.mklang.compiler.output.Outputter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CompilationUnitVisitor extends Visitor.Adapter {

    private Context context;

    @Override
    public void visitCompilationUnit(CompilationUnit cu) {
        // pass 1 - collect definitions
        FunctionDefinitionCollector fDefCollector = new FunctionDefinitionCollector();
        DataDeclarationCollector dDeclCollector = new DataDeclarationCollector();
        for (Statement stmt : cu.getStatements()) {
            stmt.accept(fDefCollector);
            stmt.accept(dDeclCollector);
        }

        Outputter outputter = Outputter.createClass(context.fileName());
        // pass 2 - compile functions
        FunctionCompiler fComp = new FunctionCompiler(outputter, fDefCollector.getDecls());
    }

    record Context(String fileName) {}

    @AllArgsConstructor
    static class FunctionCompiler extends Visitor.Adapter {
        private final Outputter outputter;
        private final FunctionDeclarations decls;
        @Override
        public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) {
            var signature = functionDeclaration.getSignature();
            var methodOutputter = outputter.createMethod(signature);

            var codeCompiler = new FunctionCodeCompiler(decls, methodOutputter);
            functionDeclaration.getExpression().accept(codeCompiler);
        }

        @AllArgsConstructor
        static class FunctionCodeCompiler extends Visitor.Adapter {
            private final FunctionDeclarations decls;
            private final Outputter.MethodOutputter methodOutputter;

            @Override
            public void visitFunctionCallExpression(FunctionCall functionCall) {
                functionCall.getArgs().forEach(e -> e.accept(this));
            }

            @Override
            public void visitBinaryExpression(Binary binary) {
                binary.getLeft().accept(this);
                binary.getRight().accept(this);
                if (binary instanceof Expression.Additive) {
                    methodOutputter.addAdd();
                } else if (binary instanceof Expression.Multiplicative) {
                    methodOutputter.addMul();
                }
            }

            @Override
            public void visitIntConstant(IntConstant intConstant) {
                methodOutputter.addIntConstant(intConstant.getValue());
            }

            @Override
            public void visitStringConstant(StringConstant stringConstant) {
                methodOutputter.addStringConstant(stringConstant.getValue());
            }
        }
    }

    static class DataDeclarationCollector extends Visitor.Adapter {
        private final Map<String, DataDeclaration> datas = new HashMap<>();
        @Override
        public void visitDataDeclaration(DataDeclaration dataDeclaration) {
            datas.put(dataDeclaration.getName(), dataDeclaration);
        }
    }

    static class FunctionDefinitionCollector extends Visitor.Adapter {
        @Getter
        private final FunctionDeclarations decls = new FunctionDeclarations();
        @Override
        public void visitFunctionDeclaration(FunctionDeclaration functionDeclaration) {
            decls.add(functionDeclaration);
        }
    }

    static class FunctionDeclarations {
        private final Map<String, FunctionDeclaration> functions = new HashMap<>();
        public void add(FunctionDeclaration decl) {
            functions.put(decl.getSignature().getName().toString(), decl);
        }
    }

}
