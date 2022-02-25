package shop.helloshop.domain.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.helloshop.domain.entity.Member;
import shop.helloshop.domain.repository.MemberRepository;
import shop.helloshop.web.exception.MemberException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 회원_가입_테스트() {

        //given
        Member member = new Member();
        member.setName("member1");
        member.setPassword("123213");

        //when
        memberService.save(member);

        //then
        assertEquals(member,memberRepository.getById(member.getId()));
    }

    @Test
    void 이메일_중복() {

        //given
        Member member = new Member();
        member.setEmail("zxc123@naver.com");
        Member member1 = new Member();
        member1.setEmail("zxc123@naver.com");
        //when
        memberService.save(member);

        //then
        assertThrows(MemberException.class, () -> memberService.save(member1));
    }

    @Test
    void 이름_중복() {

        //given
        Member member = new Member();
        member.setName("김");
        Member member1 = new Member();
        member1.setName("김");
        //when
        memberService.save(member);

        //then
        assertThrows(MemberException.class, () -> memberService.save(member1));
    }

}