package shop.helloshop.domain.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import shop.helloshop.domain.entity.Member;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.domain.entity.items.Phone;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private EntityManager em;

    @Test
    void 아이템_저장() {
        //given
        Member member1 = createMember("회원1");
        Long memberId = memberService.save(member1);
        Item item = createPhone("상품1");

        //when
        itemService.save(item,memberId);
        em.flush();
        em.clear();
        Item findItem = itemService.findOne(item.getId());

        //then
        assertEquals(itemService.findOne(item.getId()),findItem);
        assertEquals(findItem.getMember(),memberService.findOne(memberId));

    }

    @Test
    void 아이템_업데이트() {
        //given
        Member member1 = createMember("회원1");
        Long memberId = memberService.save(member1);
        Item item = createPhone("상품1");
        itemService.save(item,memberId);
        String update = "업데이트";

        //when
        itemService.update(item.getId(), update,1500,5);
        em.flush();
        em.clear();
        Item findItem = itemService.findOne(item.getId());

        //then
        assertEquals(findItem.getName(),update);
        assertEquals(findItem.getPrice(),1500);
        assertEquals(findItem.getQuantity(),5);

    }

    @Test
    void 아이템_삭제() {
        //given
        Member member1 = createMember("회원1");
        Long memberId = memberService.save(member1);
        Item item = createPhone("상품1");
        itemService.save(item,memberId);

        //when
       itemService.remove(item.getId());
       em.flush();
       em.clear();

        //then
        assertNull(itemService.findOne(item.getId()));

    }

    @Test
    void 아이템_리스트() {
        //given
        Member member1 = createMember("회원1");
        Long memberId = memberService.save(member1);
        Item item = createPhone("상품1");
        Item item2 = createPhone("상품2");
        Item item3 = createPhone("상품3");
        itemService.save(item,memberId);
        itemService.save(item2,memberId);
        itemService.save(item3,memberId);

        //when
        em.flush();
        em.clear();

        //then
      assertEquals(itemService.findCount(),3);

    }

    private Item createPhone(String name) {
        Phone item = new Phone();
        item.setName(name);
        return item;
    }

    private Member createMember(String name) {
        Member member = new Member();
        member.setName("");
        return member;
    }

}