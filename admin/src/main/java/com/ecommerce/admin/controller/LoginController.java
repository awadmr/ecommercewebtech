package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.Impl.AdminServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginForm(Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

    @RequestMapping("/index")
    public String home(Model model){
        model.addAttribute("title", "Home Page");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }
        model.addAttribute("remoteUser", authentication.getName()); // Add the remote user to the model
        return "index";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("title", "Register");
        model.addAttribute("adminDto", new AdminDto());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model){
        model.addAttribute("title", "Forgot Password");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(@Valid @ModelAttribute("adminDto") AdminDto adminDto,
                              BindingResult result,
                              Model model){

        try {
            if(result.hasErrors()){
                model.addAttribute("adminDto", adminDto);
                return "register";
            }
            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);
            if(admin != null){
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("emailError", "Your email has been registered!");
                return "register";
            }
            if(adminDto.getPassword().equals(adminDto.getRepeatPassword())){
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                model.addAttribute("success", "Registered successfully!");
                model.addAttribute("adminDto", new AdminDto()); // Reset form
            }else{
                model.addAttribute("adminDto", adminDto);
                model.addAttribute("passwordError", "Password provided does not match. Please try again!");
                return "register";
            }
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errors", "Server has found an error! Double check your information and try again.");
        }
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";  // Redirect to login page with a logout message
    }
}
