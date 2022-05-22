package com.company.filehandlingapp.model.file;

import java.util.List;

public interface FileOperation<T extends Object> {
    List<T> read(String path);

    boolean write(String path, List<T> content);
}
