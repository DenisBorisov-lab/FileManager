package com.fileManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum FileType {

    DIRECTORY("dir"),
    FILE("file");

    private String name;

    public String getName() {
        return name;
    }
}
