package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class LoginForm {

    @NotBlank(message = "잘못된 입력입니다.")
    private String email;

    @NotBlank(message = "잘못된 입력입니다.")
    private String password;
}
