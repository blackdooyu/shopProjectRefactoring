package shop.helloshop.domain.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.web.dto.MemberDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter @Getter
@EqualsAndHashCode
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private MemberGrade memberGrade;


    @OneToMany(mappedBy = "member")
    private List<Item> itemList = new ArrayList<>();

    @Embedded
    private Address address;


    //생성 편의 메서드
    public static Member createMember(String email, String password, String name, Address address) {

        Member member = new Member();
        member.setEmail(email);
        member.setPassword(password);
        member.setName(name);
        member.setAddress(address);
        member.setMemberGrade(MemberGrade.BASIC);

        return member;
    }

    //update 편의 메서드
    public void updateMember(MemberDto memberDto) {

        this.setEmail(memberDto.getEmail());
        this.setPassword(memberDto.getPassword());
        this.setName(memberDto.getName());
        this.setAddress(new Address(memberDto.getCity(),memberDto.getStreet(),memberDto.getZipcode()));

    }


}
