package org.example.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.example.dac.CustomersDAC;
import org.example.dac.PurchasesDAC;
import org.example.json.search.read.*;
import org.example.json.search.write.Customer;
import org.example.json.search.write.Result;
import org.example.json.search.write.SearchResult;
import org.example.util.ErrorUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Use case для search
 */
public class SearchUseCase {
    private final String outputFileName;
    private final CustomersDAC customersDAC;
    private final PurchasesDAC purchasesDAC;

    public SearchUseCase(String outputFileName, CustomersDAC customersDAC, PurchasesDAC purchasesDAC) {
        this.outputFileName = outputFileName;
        this.customersDAC = customersDAC;
        this.purchasesDAC = purchasesDAC;
    }

    public void execute(CriteriaList criteriaList) throws SQLException, IOException {
        List<Result> results = new ArrayList<>();
        SearchResult searchResult = new SearchResult();
        searchResult.setType("search");

        for(Criteria criteria: criteriaList.criterias) {
            if(criteria instanceof LastNameCriteria) {
                ArrayList<Pair<String, String >> customers = null;
                try {
                    customers = customersDAC.getUsersBySurname(((LastNameCriteria) criteria).lastName);
                } catch (SQLException e) {
                    ErrorUtils.writeError("Некорректные аргументы в запросе!", outputFileName);
                } catch (IOException e) {
                    ErrorUtils.writeError("Ошибка соединения с базой данных!", outputFileName);
                }
                if (customers != null) {
                    results.add(formatResult(criteria, customers));
                }
            }
            else if (criteria instanceof ProductNameCriteria) {
                ArrayList<Pair<String, String>> customers;
                customers = purchasesDAC.getCustomerByProductRatio(((ProductNameCriteria) criteria).productName, ((ProductNameCriteria) criteria).minTimes);
                if (customers != null) {
                    results.add(formatResult(criteria, customers));
                }
            } else if (criteria instanceof ExpensesCriteria) {
                ArrayList<Pair<String, String >> customers;
                customers = customersDAC.getCustomersByAmountInterval(((ExpensesCriteria)criteria).minExpenses, ((ExpensesCriteria) criteria).maxExpenses);
                if (customers != null) {
                    results.add(formatResult(criteria, customers));
                }
            } else if(criteria instanceof BadCustomersCriteria) {
                ArrayList<Pair<String, String>> customers;
                customers = customersDAC.getBadCustomers(((BadCustomersCriteria) criteria).badCustomers);
                if (customers != null) {
                    results.add(formatResult(criteria, customers));
                }
            }
        }
        searchResult.setResults(results);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFileName), searchResult);
            System.out.println("Данные JSON успешно записаны в файл.");
        } catch (Exception e) {
            ErrorUtils.writeError("Ошибка записи в файл!", outputFileName);
        }
    }
    private Result formatResult(Criteria criteria, ArrayList<Pair<String, String>> customers) {
        Result result = new Result();
        result.setCriteria(criteria);

        List<Customer> writeCustomers = new ArrayList<>();
        for(Pair<String, String> pair : customers) {
            Customer writeCustomer = new Customer();
            writeCustomer.setFirstName(pair.getValue0());
            writeCustomer.setLastName(pair.getValue1());
            writeCustomers.add(writeCustomer);
        }
        result.setResults(writeCustomers);
        return result;
    }
}
