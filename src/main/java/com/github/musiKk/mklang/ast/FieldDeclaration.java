package com.github.musiKk.mklang.ast;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldDeclaration {
    private String name;
    private DottedName type;
}
