package org.example;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dac.CustomersDAC;
import org.example.dac.ProductsDAC;
import org.example.dac.PurchasesDAC;
import org.example.json.search.read.CriteriaList;
import org.example.json.stats.read.DateRange;
import org.example.usecase.SearchUseCase;
import org.example.usecase.StatsUseCase;
import org.example.util.ErrorUtils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        if(args.length < 3) {
            System.out.println("Not enough arguments passed! Expected: 3, Passed: " + args.length);
            printHelp();
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        switch (args[0]) {
            case "search": {
                try {
                    CriteriaList criteriaList = objectMapper.readValue(new File(args[1]), CriteriaList.class);
                    CustomersDAC customersDAC = new CustomersDAC();
                    PurchasesDAC purchasesDAC = new PurchasesDAC();
                    SearchUseCase useCase = new SearchUseCase(args[2], customersDAC, purchasesDAC);
                    useCase.execute(criteriaList);
                } catch (StreamReadException | DatabindException e) {
                    ErrorUtils.writeError("Некорректный формат данных", args[2]);
                } catch (IOException e) {
                    ErrorUtils.writeError("Ошибка чтения файла", args[2]);
                } catch (SQLException e) {
                    ErrorUtils.writeError("Ошибка соединения", args[2]);
                }
                break;
            }
            case "stat": {
                try {
                    DateRange dateRange = objectMapper.readValue(new File(args[1]), DateRange.class);
                    CustomersDAC customersDAC = new CustomersDAC();
                    PurchasesDAC purchasesDAC = new PurchasesDAC();
                    ProductsDAC productsDAC = new ProductsDAC();
                    StatsUseCase statsUseCase = new StatsUseCase(args[2], customersDAC, productsDAC, purchasesDAC);
                    statsUseCase.execute(dateRange);
                }
                catch (StreamReadException | DatabindException e) {
                    ErrorUtils.writeError("Некорректный формат данных", args[2]);
                } catch (IOException e) {
                    ErrorUtils.writeError("Ошибка чтения файла", args[2]);}
                catch (SQLException e) {
                    ErrorUtils.writeError("Ошибка соединения", args[2]);}
                catch (ParseException e) {
                    ErrorUtils.writeError("Некорректный формат даты", args[2]);
                }
                break;
            }
            default: {
                System.out.println("Неверный тип операции: должен быть либо search, либо stat");
                printHelp();
            }
        }
    }

    private static void printHelp() {
        System.out.println("Пример использования: java -jar shopManager.jar [search/stat] input.json output.json");
    }
}