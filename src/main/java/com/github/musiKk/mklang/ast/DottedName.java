package com.github.musiKk.mklang.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class DottedName {
    private List<String> segments = new ArrayList<>();

    public DottedName() {
    }

    public DottedName(String... segments) {
        this.segments = Arrays.asList(segments);
    }

    public void addSegment(String segment) {
        segments.add(segment);
    }
}
