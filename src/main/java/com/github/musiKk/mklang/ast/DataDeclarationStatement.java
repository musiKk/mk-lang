package com.github.musiKk.mklang.ast;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataDeclarationStatement implements Statement {

    private String name;
    private List<FieldDeclaration> fieldDeclarations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDataDeclarationStatement(this);
    }
}
