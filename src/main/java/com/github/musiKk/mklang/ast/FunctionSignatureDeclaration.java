package com.github.musiKk.mklang.ast;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FunctionSignatureDeclaration {
    DottedName name;
    DottedName type;
    List<Parameter> parameters;

    @Data
    @AllArgsConstructor
    public static class Parameter {
        String name;
        DottedName type;
    }
}
