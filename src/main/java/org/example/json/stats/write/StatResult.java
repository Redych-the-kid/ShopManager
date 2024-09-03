package org.example.json.stats.write;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * JSON-вывод для stat
 */
@Data
public class StatResult {
    @JsonProperty("type")
    private String type;

    @JsonProperty("totalDays")
    private int totalDays;

    @JsonProperty("customers")
    private List<Customer> customers;

    @JsonProperty("totalExpenses")
    private int totalExpenses;

    @JsonProperty("avgExpenses")
    private int avgExpenses;
}