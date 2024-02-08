package com.isa.webapp.controller;

import com.isa.webapp.model.User;
import com.isa.webapp.model.UserRole;
import com.isa.webapp.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final UserService userService;

    @PostMapping("/login2")
    public String postLogin(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes, HttpSession session) {
        User registeredUser = getUserFromRegistrationData(user);

        if (registeredUser != null) {
            user.setGrades(registeredUser.getGrades());
            user.setUserRole(registeredUser.getUserRole());
            session.setAttribute("loggedInUser", user);
            if (user.getUserRole() == UserRole.ADMIN) {
               return user.getPassword().equals("admin")
                       ? "redirect:/admin/edit-profile"
                       : "redirect:/";
            }
            else if (user.getUserRole() == UserRole.STUDENT) {
                return "redirect:/student/dashboard";
            } else if (user.getUserRole() == UserRole.TEACHER) {
                return "redirect:/teacher/students";
            } else {
                return "redirect:/";
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Błąd logowania. Spróbuj ponownie.");
            return "redirect:/login";
        }
    }

    private User getUserFromRegistrationData(User user) {
        return userService.getUserByEmail(user.getEmail());
    }
}
