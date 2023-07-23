package ru.job4j.cinema.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegistrationPage() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer,
                           @ModelAttribute User user, Model model, HttpSession session) {
        var userOptional = userService.save(user);
        if (userOptional.isEmpty()) {
            model.addAttribute("error",
                    "The user with this mail exists, or the data is incorrect.");
            return "users/register";
        }
        return loginUser(referrer, user, model, session);
    }

    @PostMapping("/registerRedirect")
    public String registerRedirect(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer,
                           @ModelAttribute User user, Model model, HttpSession session) {
        register(referrer, user, model, session);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer,
                            @ModelAttribute User user, Model model, HttpSession session) {
        var userOptional = userService.findByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Email or password entered incorrectly");
            return "users/login";
        }
        session.setAttribute("user", userOptional.get());
        return "redirect:" + referrer;
    }

    @PostMapping("/loginRedirect")
    public String loginUserRedirect(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer,
                            @ModelAttribute User user, Model model, HttpSession session) {
        loginUser(referrer, user, model, session);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(@RequestHeader(value = HttpHeaders.REFERER, required = false) final String referrer,
                         HttpSession session) {
        session.invalidate();
        return "redirect:" + referrer;
    }
}
