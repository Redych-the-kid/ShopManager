package org.example.dac;

import org.javatuples.Pair;
import org.example.util.DatabaseConnection;
import org.javatuples.Triplet;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Контроллер для работы с таблицей покупателей
 */
public class CustomersDAC {
    /**
     * Возвращает список покупателей с заданной фамилией
     *
     * @param surname Фамилия
     * @return Список покупателей с заданной фамилией
     * @throws SQLException Ошибка соединения или запроса
     * @throws IOException  Ошибка соединения
     */
    public ArrayList<Pair<String, String>> getUsersBySurname(String surname) throws SQLException, IOException {
        String sql = "SELECT name FROM public.Customers WHERE surname = ?";
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, surname);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                Pair<String, String> pair = new Pair<>(name, surname);
                result.add(pair);
            }
        }
        return result;
    }

    /**
     * Возвращает список покупателей, потративших от min до max сумму денег
     *
     * @param min Минимальная сумма
     * @param max Максимальная сумма
     * @return Список покупателей, потративших от min до max сумму денег
     * @throws SQLException Ошибка соединения или запроса
     * @throws IOException  Ошибка соединения
     */
    public ArrayList<Pair<String, String>> getCustomersByAmountInterval(int min, int max) throws SQLException, IOException {
        ArrayList<Pair<String, String>> customers = new ArrayList<>();
        String sql = "SELECT c.name, c.surname " +
                "FROM Customers c " +
                "JOIN Purchases pu ON c.id = pu.customer_id " +
                "JOIN Products p ON pu.product_id = p.id " +
                "GROUP BY c.id, c.name, c.surname " +
                "HAVING SUM(p.price) BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, min);
            statement.setInt(2, max);

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
     * Возвращает n самых пассивных покупателей
     *
     * @param limit Заданное ограничение списка пассивных покупателей
     * @return Список из n пассивных покупателей
     * @throws SQLException Ошибка соединения или запроса
     * @throws IOException  Ошибка соединения
     */
    public ArrayList<Pair<String, String>> getBadCustomers(int limit) throws SQLException, IOException {
        ArrayList<Pair<String, String>> customers = new ArrayList<>();
        String sql = "SELECT c.name, c.surname " +
                "FROM public.Customers c " +
                "LEFT JOIN public.Purchases pu ON c.id = pu.customer_id " +
                "GROUP BY c.id, c.name, c.surname " +
                "ORDER BY COUNT(pu.product_id) ASC " +
                "LIMIT ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, limit);

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
     * Возвращает список покупателей, купивших хотя-бы один товар на протяжении заданного интервала
     *
     * @param start Начало интервала
     * @param end   Конец интервала
     * @return Список покупателей
     * @throws SQLException   Ошибка соединения или запроса
     * @throws IOException    Ошибка соединения
     * @throws ParseException Ошибка в формате интервала
     */
    public ArrayList<Triplet<Integer, String, String>> getCustomersBetweenDates(String start, String end) throws SQLException, IOException, ParseException {
        ArrayList<Triplet<Integer, String, String>> customers = new ArrayList<>();
        String sql = "SELECT DISTINCT c.id, c.\"name\", c.surname " +
                "FROM public.Purchases pu " +
                "JOIN public.Customers c ON pu.customer_id = c.id " +
                "WHERE pu.purchase_date BETWEEN ? AND ?;";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            statement.setDate(1, new Date(formatter.parse(start).getTime()));
            statement.setDate(2, new Date(formatter.parse(end).getTime()));

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                customers.add(new Triplet<>(id, name, surname));
            }
        }
        return customers;
    }
}
