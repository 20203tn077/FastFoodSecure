package mx.edu.utez.FastFoodSecure.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import mx.edu.utez.FastFoodSecure.model.Dish;
import mx.edu.utez.FastFoodSecure.repository.IDishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/Platillos")
public class DishController {
    @Autowired
    IDishRepository dishRepository;

    @Autowired
    Validator validator;

    @RequestMapping("/")
    public String platillos(Model model) {
        model.addAttribute("dishes", dishRepository.findAll());
        if (new Random().nextBoolean()) throw new RuntimeException();
        return "platillos";
    }

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("dishes", dishRepository.findAll());
        return "dishes";
    }

    @RequestMapping("/register")
    public String create(RedirectAttributes attributes, Dish dish) {
        try {
            dish.setRegistrationDate(LocalDateTime.now());
            Set<ConstraintViolation<Dish>> violations = validator.validate(dish);
            if(!violations.isEmpty()) for (ConstraintViolation<Dish> error : violations) {
                attributes.addFlashAttribute("success", false);
                attributes.addFlashAttribute("message", error.getMessage());
            } else {
                dishRepository.save(dish);
                attributes.addFlashAttribute("success", true);
                attributes.addFlashAttribute("message", "El platillo se registró exitosamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("success", false);
            attributes.addFlashAttribute("message", "Ocurrió un error: E004");
        } finally {
            return "redirect:/Platillos/index";
        }
    }

    @RequestMapping("/update")
    public String update(RedirectAttributes attributes, Dish updatedDish) {
        try {
            Dish dish = dishRepository.findById(updatedDish.getId()).orElseGet(null);
            if (dish == null) {
                attributes.addFlashAttribute("success", false);
                attributes.addFlashAttribute("message", "Ocurrió un error: E003");
            } else {
                dish.setName(updatedDish.getName());
                dish.setDescription(updatedDish.getDescription());
                dish.setPrice(updatedDish.getPrice());
                Set<ConstraintViolation<Dish>> violations = validator.validate(dish);
                if(!violations.isEmpty()) for (ConstraintViolation<Dish> error : violations) {
                    attributes.addFlashAttribute("success", false);
                    attributes.addFlashAttribute("message", error.getMessage());
                    break;
                } else {
                    dishRepository.save(dish);
                    attributes.addFlashAttribute("success", true);
                    attributes.addFlashAttribute("message", "El platillo se actualizó exitosamente");
                }
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("success", false);
            attributes.addFlashAttribute("message", "Ocurrió un error: E005");
        } finally {
            return "redirect:/Platillos/index";
        }
    }

    @RequestMapping("/delete")
    public String delete(RedirectAttributes attributes, Long id) {
        try {
            Optional<Dish> dish = dishRepository.findById(id);
            if (dish.isEmpty()) {
                attributes.addFlashAttribute("success", false);
                attributes.addFlashAttribute("message", "Ocurrió un error: E003");
            } else {
                Dish dishObj = dish.get();
                dishObj.setStatus(false);
                dishRepository.save(dishObj);
                attributes.addFlashAttribute("success", true);
                attributes.addFlashAttribute("message", "El platillo se eliminó exitosamente");
            }
        } catch (Exception e) {
            attributes.addFlashAttribute("success", false);
            attributes.addFlashAttribute("message", "Ocurrió un error: E006");
        } finally {
            return "redirect:/Platillos/index";
        }
    }
}
