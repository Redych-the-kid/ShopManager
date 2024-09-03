package org.example.json.search.read;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LastNameCriteria.class, name = "lastName"),
        @JsonSubTypes.Type(value = ProductNameCriteria.class, name = "productName"),
        @JsonSubTypes.Type(value = ExpensesCriteria.class, name = "expenses"),
        @JsonSubTypes.Type(value = BadCustomersCriteria.class, name = "badCustomers")
})
public abstract class Criteria {

}
