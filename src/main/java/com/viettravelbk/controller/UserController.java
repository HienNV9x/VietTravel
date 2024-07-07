package com.viettravelbk.controller;

import java.security.Principal;
import java.util.HashMap; //
import java.util.Map; //

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated; //
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody; //
import org.springframework.web.bind.annotation.RequestParam; //
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

	@GetMapping("/viettravel/registration")
	public String registration(Model model) {
		if (securityService.isAuthenticated()) {
			return "redirect:/";
		}
		model.addAttribute("userForm", new User());
		return "redirect:/#member";
	}

	@PostMapping("/viettravel/registration")
	public ResponseEntity<?> registration(@RequestBody @Validated User userForm, BindingResult bindingResult) {
        if (userService.existsByUsername(userForm.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
        }
        
        if (userService.existsByEmail(userForm.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists");
        }
		
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error -> {
				errors.put(error.getField(), error.getDefaultMessage());
			});
			return ResponseEntity.badRequest().body(Map.of("errors", errors));
		}
		userService.save(userForm);
		securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());
		return ResponseEntity.ok().build();
	}

	@GetMapping("/login")
	public String login(Model model, @RequestParam(name = "error", required = false) String error) {
		if (securityService.isAuthenticated()) {
			return "redirect:/";
		} else if (error != null) {
			model.addAttribute("error", "Your username and password is invalid");
		}
		return "login";
	}

	//Chức năng khi click vào logo_user thì sẽ phân quyền vào các trang admin hoặc cart
	@GetMapping("/viettravel/role")
	public String someMethod(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("admin"));
		model.addAttribute("isAdmin", isAdmin);
		if (isAdmin == true) {
			return "admin_user/admin/dashboard";
		} else {
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
				String email = principal.getAttribute("email"); 
				Long userId = userService.getUserIdByEmail(email);		//Fetch userId using email or any other identifier
				if (userId != null) {
					return ResponseEntity.ok(userId);
				}
			}
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
}
