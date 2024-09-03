package org.example.dac;

import org.javatuples.Pair;
import org.example.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Контроллер для работы с таблицей продуктов
 */
public class ProductsDAC {
    /**
     * Возвращает список товаров, купленных конкретным покупателем в конкретный период, и их суммарную стоимость
     *
     * @param id        Индекс покупателя
     * @param startDate Начало периода
     * @param endDate   Конец периода
     * @return Список товаров, купленных конкретным покупателем в конкретный период, и их суммарную стоимость
     * @throws SQLException   Ошибка соединения или запроса
     * @throws IOException    Ошибка соединения
     * @throws ParseException Ошибка формата даты
     */
    public ArrayList<Pair<String, Integer>> getUniqueProducts(int id, String startDate, String endDate) throws SQLException, IOException, ParseException {
        ArrayList<Pair<String, Integer>> result = new ArrayList<>();
        String sql = "SELECT p.name, SUM(p.price) AS expenses " +
                "FROM Products p " +
                "JOIN Purchases pu ON p.id = pu.product_id " +
                "WHERE pu.customer_id = ? AND pu.purchase_date BETWEEN ? AND ? " +
                "GROUP BY p.id, p.name " +
                "ORDER BY expenses DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            statement.setInt(1, id);
            statement.setDate(2, new Date(formatter.parse(startDate).getTime()));
            statement.setDate(3, new Date(formatter.parse(endDate).getTime()));

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name");
                int expenses = rs.getInt("expenses");
                result.add(new Pair<>(productName, expenses));
            }
        }
        return result;
    }
}
