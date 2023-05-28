package com.github.musiKk.mklang.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VariableAssignment implements Statement {
    String name;
    Expression value;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitVariableAssignment(this);
    }
}
