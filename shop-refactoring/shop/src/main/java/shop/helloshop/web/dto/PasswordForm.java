package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PasswordForm {

    @NotBlank(message = "필수 값 입니다")
    private String password;
}
