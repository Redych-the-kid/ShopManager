package org.example.json.stats.read;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JSON-ввод для stat
 */
@Data
@NoArgsConstructor
public class DateRange {
    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;
}
