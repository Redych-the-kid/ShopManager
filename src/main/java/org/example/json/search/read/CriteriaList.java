package org.example.json.search.read;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Класс для хранения JSON данных о вводе при вызове search
 */
public class CriteriaList {
    public List<Criteria> criterias;
}
