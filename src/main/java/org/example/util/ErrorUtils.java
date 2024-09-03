package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.json.error.ErrorResult;

import java.io.File;

/**
 * Вспомогательный класс для формирования отчётов об ошибках
 */
public class ErrorUtils {
    public static void writeError(String error, String outputFileName) {
        ErrorResult result = new ErrorResult();
        result.setType("error");
        result.setMessage(error);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFileName), result);
        } catch (Exception e) {
            System.out.println("Error writing error message in the file. The message is: " + error);
        }
    }
}
