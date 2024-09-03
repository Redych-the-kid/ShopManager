package org.example.json.stats.write;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Customer {
    @JsonProperty("name")
    private String name;

    @JsonProperty("purchases")
    private List<Purchase> purchases;

    @JsonProperty("totalExpenses")
    private int totalExpenses;
}
