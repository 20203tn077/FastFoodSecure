package mx.edu.utez.FastFoodSecure.controller;

import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.FastFoodSecure.model.Dish;
import mx.edu.utez.FastFoodSecure.repository.IDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/Platillos")
public class ApiController {
    @Autowired
    IDishRepository dishRepository;

    @RequestMapping("/find/{id}")
    public Map<String, Object> find(@PathVariable Long id, HttpServletResponse response) {
        Map<String, Object> responseData = new HashMap<>();
        try {
            Optional<Dish> dish = dishRepository.findById(id);
            if (dish.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                responseData.put("message", "Ocurrió un error: E003");
            } else responseData.put("dish", dish.get());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            responseData.put("message", "Ocurrió un error: E000");
        } finally {
            return responseData;
        }
    }
}
