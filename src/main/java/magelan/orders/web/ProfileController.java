package magelan.orders.web;

import jakarta.validation.Valid;
import magelan.orders.security.UserData;
import magelan.orders.user.model.User;
import magelan.orders.user.service.UserService;
import magelan.orders.web.dto.ChangePasswordRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserData userData, Model model) {
        User user = userService.getById(userData.getUserId());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/profile/password")
    public String changePasswordPage(Model model) {
        if (!model.containsAttribute("changePasswordRequest")) {
            model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        }
        return "profile-password";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @AuthenticationPrincipal UserData userData,
            @Valid ChangePasswordRequest changePasswordRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword())) {
            bindingResult.rejectValue("confirmNewPassword", "password.mismatch", "Passwords must match.");
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.changePasswordRequest", bindingResult);
            redirectAttributes.addFlashAttribute("changePasswordRequest", changePasswordRequest);
            return "redirect:/profile/password";
        }

        try {
            userService.changePassword(
                    userData.getUserId(),
                    changePasswordRequest.getCurrentPassword(),
                    changePasswordRequest.getNewPassword()
            );

            redirectAttributes.addFlashAttribute("passwordUpdated", "Password changed successfully!");
            return "redirect:/profile";

        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("passwordError", ex.getMessage());
            return "redirect:/profile/password";
        }
    }
}
