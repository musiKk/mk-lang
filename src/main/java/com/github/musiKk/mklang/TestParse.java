package com.github.musiKk.mklang;

import java.io.File;
import java.io.FileInputStream;

public class TestParse {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("expected file as first arg");
        }
        String fileName = args[0];

        File file = new File(fileName);
        Example ex = new Example(new FileInputStream(file), "utf-8");
        ex.compilationUnit();
    }

}
