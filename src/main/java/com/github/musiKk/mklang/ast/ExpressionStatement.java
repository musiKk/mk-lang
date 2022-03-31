package com.github.musiKk.mklang.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpressionStatement implements Statement {
    Expression expression;

    @Override
    public void accept(Visitor visitor) {
        expression.accept(visitor);
    }
}
