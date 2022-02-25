package shop.helloshop.web.dto;

import lombok.Getter;
import lombok.Setter;
import shop.helloshop.domain.entity.MemberGrade;

@Getter @Setter
public class MemberSessionDto {

    private Long id;
    private String name;
    private MemberGrade memberGrade;

    public MemberSessionDto(Long id, String name, MemberGrade memberGrade) {
        this.id = id;
        this.name = name;
        this.memberGrade = memberGrade;
    }

    public MemberSessionDto() {
    }
}
