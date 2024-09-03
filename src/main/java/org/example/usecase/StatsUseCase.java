package org.example.usecase;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javatuples.Pair;
import org.example.dac.CustomersDAC;
import org.example.dac.ProductsDAC;
import org.example.dac.PurchasesDAC;
import org.example.json.stats.read.DateRange;
import org.example.json.stats.write.Purchase;
import org.example.json.stats.write.StatResult;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.example.json.stats.write.Customer;
import org.example.util.ErrorUtils;
import org.javatuples.Triplet;

/**
 * Use case для stat
 */
public class StatsUseCase {
    private final String outputFileName;
    private final CustomersDAC customersDAC;
    private final ProductsDAC productsDAC;
    private final PurchasesDAC purchasesDAC;

    public StatsUseCase(String outputFileName, CustomersDAC customersDAC, ProductsDAC productsDAC, PurchasesDAC purchasesDAC) {
        this.outputFileName = outputFileName;
        this.customersDAC = customersDAC;
        this.productsDAC = productsDAC;
        this.purchasesDAC = purchasesDAC;
    }

    public void execute(DateRange dateRange) throws ParseException, SQLException, IOException {
        StatResult statResult = new StatResult();
        statResult.setType("stat");
        statResult.setTotalDays(getDaysCount(dateRange.getStartDate(), dateRange.getEndDate()));
        List<Customer> customers = new ArrayList<>();
        ArrayList<Triplet<Integer, String, String>> customersList;
        customersList = customersDAC.getCustomersBetweenDates(dateRange.getStartDate(), dateRange.getEndDate());
        if (customersList != null) {
            for(Triplet<Integer, String, String> customer : customersList) {
                Customer customer1 = new Customer();
                customer1.setName(customer.getValue1() + " " + customer.getValue2());
                ArrayList<Pair<String, Integer>> purchases;
                purchases = productsDAC.getUniqueProducts(customer.getValue0(), dateRange.getStartDate(), dateRange.getEndDate());
                List<Purchase> purchasesList = new ArrayList<>();
                if (purchases != null) {
                    for(Pair<String, Integer> pair : purchases) {
                        Purchase purchase = new Purchase();
                        purchase.setName(pair.getValue0());
                        purchase.setExpenses(pair.getValue1());
                        purchasesList.add(purchase);
                    }
                }
                customer1.setPurchases(purchasesList);
                customer1.setTotalExpenses(purchasesDAC.getSumOfAmounts(customer.getValue0(), dateRange.getStartDate(), dateRange.getEndDate()));
                customers.add(customer1);
            }
        }
        statResult.setCustomers(customers);
        statResult.setAvgExpenses(purchasesDAC.getAverageExpenses(dateRange.getStartDate(), dateRange.getEndDate()));
        statResult.setTotalExpenses(purchasesDAC.getTotalSales(dateRange.getStartDate(), dateRange.getEndDate()));
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(outputFileName), statResult);
            System.out.println("Данные JSON успешно записаны в файл.");
        } catch (Exception e) {
            ErrorUtils.writeError("Ошибка записи в файл!", outputFileName);
        }
    }
    private int getDaysCount(String startDateString, String endDateString) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate startDate = LocalDate.parse(startDateString, formatter);
            LocalDate endDate = LocalDate.parse(endDateString, formatter);
            long totalWeekdays = 0;
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY) {
                    totalWeekdays++;
                }
            }
            return (int) totalWeekdays;
        } catch (Exception e) {
            throw new ParseException("Unexpected error", -1);
        }
    }
}
