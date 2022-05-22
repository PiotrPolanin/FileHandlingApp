package com.company.filehandlingapp.model.item;

import java.util.List;

public interface OperationGenerator<T extends Object> {
    List<String> generate(T report);
}
