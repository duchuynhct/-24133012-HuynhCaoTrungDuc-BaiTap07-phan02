package vn.iotstar.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/category-ajax";
    }

    @GetMapping("/category-ajax")
    public String categoryAjax() {
        return "category-ajax";
    }

    @GetMapping("/product-ajax")
    public String productAjax() {
        return "product-ajax";
    }
}
