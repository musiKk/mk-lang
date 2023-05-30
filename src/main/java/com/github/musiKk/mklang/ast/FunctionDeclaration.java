package com.github.musiKk.mklang.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FunctionDeclaration implements Statement {

    private FunctionSignatureDeclaration signature;
    private Expression expression;

    public void accept(Visitor visitor) {
        visitor.visitFunctionDeclaration(this);
    }

}
