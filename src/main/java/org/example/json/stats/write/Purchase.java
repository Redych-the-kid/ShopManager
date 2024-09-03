package org.example.json.stats.write;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Purchase {
    @JsonProperty("name")
    private String name;

    @JsonProperty("expenses")
    private int expenses;
}
