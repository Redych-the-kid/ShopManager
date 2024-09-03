package org.example.dac;

import org.javatuples.Pair;
import org.example.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Контроллер для работы с базой покупок
 */
public class PurchasesDAC {
    /**
     * Возвращает список покупателей, купивших данный товар минимум ratio количество раз
     *
     * @param productName Название товара
     * @param ratio       Количество товара
     * @return Список покупателей, купивших данный товар минимум ratio количество раз
     * @throws SQLException Ошибка соединения или запроса
     * @throws IOException  Ошибка соединения
     */
    public ArrayList<Pair<String, String>> getCustomerByProductRatio(String productName, int ratio) throws SQLException, IOException {
        ArrayList<Pair<String, String>> customers = new ArrayList<>();
        String sql = "SELECT c.name, c.surname, COUNT(pu.product_id) AS purchase_count " +
                "FROM Purchases pu " +
                "JOIN Products p ON pu.product_id = p.id " +
                "JOIN Customers c ON pu.customer_id = c.id " +
                "WHERE p.name = ? " +
                "GROUP BY c.name, c.surname " +
                "HAVING COUNT(pu.product_id) >= ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setString(1, productName);
            statement.setInt(2, ratio);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                customers.add(new Pair<>(name, surname));
            }
        }

        return customers;
    }

    /**
     * Возвращает сумму трат конкретного покупателя за данный период
     *
     * @param customerId Индетификатор покупателя
     * @param start      Начало периода
     * @param end        Конец периода
     * @return Сумма трат конкретного покупателя за данный период
     * @throws SQLException   Ошибка соединения или запроса
     * @throws IOException    Ошибка соединения
     * @throws ParseException Ошибка в формате даты
     */
    public int getSumOfAmounts(int customerId, String start, String end) throws SQLException, IOException, ParseException {
        int sum = 0;
        String sql = "SELECT SUM(p.price) AS total_spent " +
                "FROM Purchases pu " +
                "JOIN Products p ON pu.product_id = p.id " +
                "WHERE pu.customer_id = ? AND pu.purchase_date BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            statement.setInt(1, customerId); // Set customer_id
            statement.setDate(2, new Date(formatter.parse(start).getTime()));
            statement.setDate(3, new Date(formatter.parse(end).getTime()));

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("total_spent");
            }
        }
        return sum;
    }

    /**
     * Возвращает сумму всех покупок за данный период
     *
     * @param start Начало периода
     * @param end   Конец периода
     * @return Сумма всех покупок за данный период
     * @throws SQLException   Ошибка соединения или запроса
     * @throws IOException    Ошибка соединения
     * @throws ParseException Ошибка в формате даты
     */
    public int getTotalSales(String start, String end) throws SQLException, IOException, ParseException {
        int sum = 0;
        String sql = "SELECT SUM(p.price) AS total_sales " +
                "FROM Purchases pu " +
                "JOIN Products p ON pu.product_id = p.id " +
                "WHERE pu.purchase_date BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            statement.setDate(1, new Date(formatter.parse(start).getTime()));
            statement.setDate(2, new Date(formatter.parse(end).getTime()));

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("total_sales");
            }
        }
        return sum;
    }

    /**
     * Возвращает среднюю сумму трат за данный период
     *
     * @param start Начало периода
     * @param end   Конец периода
     * @return Средняя сумма трат за данный период
     * @throws SQLException   Ошибка соединения или запроса
     * @throws IOException    Ошибка соединения
     * @throws ParseException Ошибка в формате даты
     */
    public int getAverageExpenses(String start, String end) throws SQLException, IOException, ParseException {
        int sum = 0;
        String sql = "SELECT AVG(total_spent) AS average_spent " +
                "FROM ( " +
                "    SELECT SUM(p.price) AS total_spent " +
                "    FROM Purchases pu " +
                "    JOIN Products p ON pu.product_id = p.id " +
                "    WHERE pu.purchase_date BETWEEN ? AND ? " +
                "    GROUP BY pu.customer_id " +
                ") AS customer_totals;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // Формат даты

            statement.setDate(1, new Date(formatter.parse(start).getTime())); // Set start date
            statement.setDate(2, new Date(formatter.parse(end).getTime())); // Set end date

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                sum = rs.getInt("average_spent");
            }
        }
        return sum;
    }
}
