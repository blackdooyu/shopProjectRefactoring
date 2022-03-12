package shop.helloshop.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.helloshop.domain.entity.Member;
import shop.helloshop.domain.repository.MemberRepository;
import shop.helloshop.web.dto.LoginForm;
import shop.helloshop.web.dto.MemberDto;
import shop.helloshop.web.dto.MemberSessionDto;
import shop.helloshop.web.exception.MemberException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional //회원가입
    public Long save(Member member) {
        validateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional //업데이트
    public void update(MemberDto memberDto) {
        Member findMember = memberRepository.getById(memberDto.getId());
        findMember.updateMember(memberDto);
    }


    public MemberSessionDto login(LoginForm loginForm) {
        return validateLogin(loginForm);
    }

    public Member findOne(Long id) {
      return memberRepository.getById(id);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }



    /*
   Email 을 기준으로 List<Member> 를 가지고와서 List 가 비어있을경우 null 반환
   비어있지 않을경우 loginForm 에 있는 password 와 비교해서 맞을경우 세션에 저장해놓을 정보 생성
   틀릴경우 null 반환
     */
    private MemberSessionDto validateLogin(LoginForm loginForm) {

        Member findMember = memberRepository.findByEmail(loginForm.getEmail());
        if (findMember == null) {
            return null;
        }

        if (findMember.getPassword().equals(loginForm.getPassword())) {
            return new MemberSessionDto(findMember.getId(), findMember.getName(),findMember.getMemberGrade());
        }


        return null;
    }

    /*
    회원가입 Check logic Email 또는 Name 이 같을경우 MemberException 을 던진다
     */
    private void validateMember(Member member) {
      Member findEmail = memberRepository.findByEmail(member.getEmail());

        if (findEmail != null) {
            throw new MemberException("이미 존재하는 email 입니다");
        }

      Member findName = memberRepository.findByName(member.getName());

        if (findName != null) {
            throw new MemberException("이미 존재하는 이름입니다.");
        }

    }

}

