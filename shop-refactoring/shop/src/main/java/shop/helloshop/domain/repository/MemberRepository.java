package shop.helloshop.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.helloshop.domain.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByEmail(String email);

    Member findByName(String name);
}
