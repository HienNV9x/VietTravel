package com.viettravelbk.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.viettravelbk.service.CustomUserDetails;
import com.viettravelbk.model.User;
import com.viettravelbk.service.SecurityService;
import com.viettravelbk.service.UserService;

@Controller
public class UserController {
	@Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;
    
    @GetMapping("/viettravel/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("userForm", new User());
        return "redirect:/#member";
    }
	
    @PostMapping("/viettravel/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
        	return "redirect:/#member";
        }
        userService.save(userForm);
        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "login";
    }
  
  //Chức năng khi click vào logo_user thì sẽ phân quyền vào các trang admin hoặc user
  @GetMapping("/viettravel/role")
  public String someMethod(Model model, Principal principal) {
      User user = userService.findByUsername(principal.getName());
      boolean isAdmin = user.getRoles().stream()
                              .anyMatch(role -> role.getName().equals("admin"));
      model.addAttribute("isAdmin", isAdmin);
      if(isAdmin == true) {
      	return "admin_user/admin/dashboard";
      }else {
      	return "admin_user/user/cart";
      }
  }
  
  //Lấy userId để đưa vào localStorage giỏ hàng
  @GetMapping("/api/userId")
  @ResponseBody
  public ResponseEntity<Long> getUserId(Authentication authentication) {
      if (authentication != null) {
          if (authentication.getPrincipal() instanceof CustomUserDetails) {
              CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
              return ResponseEntity.ok(userDetails.getUserId());
          } else if (authentication instanceof OAuth2AuthenticationToken) {
              OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
              OAuth2User principal = oauthToken.getPrincipal();
              String email = principal.getAttribute("email"); //or whatever attribute you're using for email
              // Fetch userId using email or any other identifier
              Long userId = userService.getUserIdByEmail(email);
              if (userId != null) {
                  return ResponseEntity.ok(userId);
              }
          }
      }
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
  }
}
