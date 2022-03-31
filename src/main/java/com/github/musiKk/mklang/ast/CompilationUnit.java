package com.github.musiKk.mklang.ast;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CompilationUnit {

    private List<Statement> statements = new ArrayList<>();

    public void addStatement(Statement stmt) {
        statements.add(stmt);
    }

    public void accept(Visitor visitor) {
        visitor.visitCompilationUnit(this);
    }
}
