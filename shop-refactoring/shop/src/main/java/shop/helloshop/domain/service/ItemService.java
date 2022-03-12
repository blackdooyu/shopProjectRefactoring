package shop.helloshop.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.helloshop.domain.entity.Member;
import shop.helloshop.domain.entity.UploadFile;
import shop.helloshop.domain.entity.items.Clothes;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.domain.entity.items.Phone;
import shop.helloshop.domain.repository.ItemRepository;
import shop.helloshop.domain.repository.MemberRepository;
import shop.helloshop.web.dto.FindSort;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void save(Item item,Long memberId) {
        Member findMember = memberRepository.getById(memberId);
        item.setMember(findMember);
        item.setLocalDateTime(LocalDateTime.now());
        item.setSalesQuantity(0);
        itemRepository.save(item);
    }


    @Transactional
    public void update(Long itemId,String name,int price,int quantity) {
        Item findItem = itemRepository.getById(itemId);
        findItem.update(name,price,quantity);
    }

    @Transactional
    public void updatePhone(Long itemId, Phone phone,List<UploadFile> updateUploadFiles) {
        Phone viewPhone = itemRepository.findViewPhone(itemId);
        viewPhone.setName(phone.getName());
        viewPhone.setPrice(phone.getPrice());
        viewPhone.setPhoneColor(phone.getPhoneColor());
        viewPhone.setQuantity(phone.getQuantity());

        viewPhone.getUploadFiles().remove(0);

        for (UploadFile uploadFile : updateUploadFiles) {
            viewPhone.addUploadFile(uploadFile);
        }

    }

    @Transactional
    public void updateClothes(Long itemId, Clothes clothes,List<UploadFile> updateUploadFiles) {
        Clothes viewClothes = itemRepository.findViewClothes(itemId);
        viewClothes.setName(clothes.getName());
        viewClothes.setPrice(clothes.getPrice());
        viewClothes.setItemSize(clothes.getItemSize());


        viewClothes.setQuantity(clothes.getQuantity());
        viewClothes.getUploadFiles().remove(0);

        for (UploadFile uploadFile : updateUploadFiles) {
            viewClothes.addUploadFile(uploadFile);
        }
    }

    @Transactional
    public void remove(Long itemId) {
        itemRepository.delete(itemRepository.getById(itemId));
    }

    public Item findOne(Long id) {
       return itemRepository.getById(id);
    }

    public Page<Item> findHomeList() {
        return itemRepository.itemBySortList(PageRequest.of(0,8, Sort.by(Sort.Direction.DESC,"salesQuantity")));
    }

    public Page<Item> findList(String sort,int page) {
        if (FindSort.Item_Popularity_List.equals(sort)) {
            return itemRepository.itemBySortList(PageRequest.of(page - 1, 16, Sort.by(Sort.Direction.DESC, "salesQuantity")));
        }
        return itemRepository.itemBySortList(PageRequest.of(page - 1, 16, Sort.by(Sort.Direction.DESC, "localDateTime")));
    }

    public Long findCount() {
        return itemRepository.itemTotalCount();
    }

    public Item itemViewForm(Long id) {
        String type = itemRepository.findType(id);

        if (type.equals("T")){
            return itemRepository.findViewClothes(id);
        }

        return itemRepository.findViewPhone(id);
    }






}
