package com.github.musiKk.mklang.ast;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TraitDeclarationStatement implements Statement {

    String name;
    List<FunctionSignatureDeclaration> functionSignatureDeclarations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visitTraitDeclarationStatement(this);
    }

}
