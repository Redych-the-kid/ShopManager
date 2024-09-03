package org.example.json.search.read;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ProductNameCriteria extends Criteria {
    public String productName;
    public int minTimes;
}
