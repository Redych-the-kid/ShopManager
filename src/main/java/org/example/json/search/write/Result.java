package org.example.json.search.write;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.example.json.search.read.Criteria;

import java.util.List;

@Data
public class Result {
    @JsonProperty("criteria")
    private Criteria criteria;

    @JsonProperty("results")
    private List<Customer> results;
}
