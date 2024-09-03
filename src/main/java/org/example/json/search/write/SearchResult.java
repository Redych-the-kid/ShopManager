package org.example.json.search.write;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * JSON-вывод для search
 */
@Data
public class SearchResult {
    @JsonProperty("type")
    private String type;

    @JsonProperty("results")
    private List<Result> results;
}
