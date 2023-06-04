package com.github.musiKk.mklang.ast;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DataDeclaration implements Statement {

    private String name;
    private List<FieldDeclaration> fieldDeclarations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitDataDeclaration(this);
    }
}
