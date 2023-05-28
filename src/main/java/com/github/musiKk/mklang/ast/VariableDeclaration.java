package com.github.musiKk.mklang.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariableDeclaration implements Statement {
    String name;
    String type;
    Expression value;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitVariableDeclaration(this);
    }
}
