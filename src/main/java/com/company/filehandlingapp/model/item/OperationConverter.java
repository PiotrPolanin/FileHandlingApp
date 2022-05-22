package com.company.filehandlingapp.model.item;

import java.util.List;

public interface OperationConverter<T extends Object> {
    List<T> convert(List<String> input, String delimiter);
}
