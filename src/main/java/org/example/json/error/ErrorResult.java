package org.example.json.error;

import lombok.Data;

/**
 * JSON-сообщение об ошибке
 */
@Data
public class ErrorResult {
    private String type;
    private String message;
}
