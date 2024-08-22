package com.Jobseeker.Jobseeker.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class JsonFileReaderToList {

    private JsonFileReaderToList() {
    }

    public static <T> List<T> readJson(ObjectMapper objectMapper, String pathToFile, TypeReference<List<T>> resourceType) throws IOException {
        return objectMapper.readValue(new File(pathToFile), resourceType);
    }

}