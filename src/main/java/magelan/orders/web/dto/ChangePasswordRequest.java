package magelan.orders.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @Size(min = 6, max = 20, message = "Password must be between 6 and 20 symbols.")
    private String newPassword;

    @NotBlank
    private String confirmNewPassword;
}
