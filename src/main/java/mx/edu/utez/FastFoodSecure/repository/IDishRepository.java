package mx.edu.utez.FastFoodSecure.repository;

import mx.edu.utez.FastFoodSecure.model.Dish;
import org.springframework.data.repository.CrudRepository;

public interface IDishRepository extends CrudRepository<Dish, Long> {
}
