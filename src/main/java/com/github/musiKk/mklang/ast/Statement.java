package com.github.musiKk.mklang.ast;

public interface Statement {
    void accept(Visitor visitor);
}
