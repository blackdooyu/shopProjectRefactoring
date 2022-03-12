package shop.helloshop.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.helloshop.domain.entity.items.Item;

import java.util.List;

public interface ItemRepositoryCustom {

    Page<Item> itemBySortList(Pageable pageable);
}
