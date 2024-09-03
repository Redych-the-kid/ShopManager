package org.example.json.search.write;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Customer {
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("firstName")
    private String firstName;
}
