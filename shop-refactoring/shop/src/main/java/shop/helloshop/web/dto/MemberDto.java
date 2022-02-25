package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;
import shop.helloshop.domain.entity.Address;
import shop.helloshop.domain.entity.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class MemberDto {

    private Long id;

    @NotBlank(message = "필수 값 입니다")
    private String email;

    @NotBlank(message = "필수 값 입니다")
    @Size(min = 6,max = 15,message = "비밀번호 길이는 '({min})~({max})' 사이여야 합니다")
    private String password;

    @NotBlank(message = "필수 값 입니다")
    @Size(min = 2,max = 8 ,message = "이름의 길이는 '({min})~({max})' 사이여야 합니다")
    private String name;

    @NotBlank(message = "필수 값 입니다")
    private String city;

    @NotBlank(message = "필수 값 입니다")
    private String street;

    @NotBlank(message = "필수 값 입니다")
    private String zipcode;





    public static MemberDto createUpdateForm(String email,String name, Address address) {

        MemberDto memberDto = new MemberDto();
        memberDto.setEmail(email);
        memberDto.setName(name);
        memberDto.setCity(address.getCity());
        memberDto.setStreet(address.getStreet());
        memberDto.setZipcode(address.getZipcode());
        return memberDto;
    }

}
