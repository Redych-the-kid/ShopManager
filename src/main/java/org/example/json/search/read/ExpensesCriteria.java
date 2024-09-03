package org.example.json.search.read;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ExpensesCriteria extends Criteria {
    public int minExpenses;
    public int maxExpenses;
}
