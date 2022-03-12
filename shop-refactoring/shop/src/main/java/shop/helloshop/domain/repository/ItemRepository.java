package shop.helloshop.domain.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.helloshop.domain.entity.Order;
import shop.helloshop.domain.entity.items.Clothes;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.domain.entity.items.Phone;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> ,ItemRepositoryCustom {


    @Query("select count(i.id) from Item i")
    Long itemTotalCount();

    @Query("select i.type from Item i where i.id = :id")
    String findType(@Param("id") Long id);

    @Query("select i from Item i where i.id = :id")
    Phone findViewPhone(@Param("id") Long id);

    @Query("select i from Item i where i.id = :id")
    Clothes findViewClothes(@Param("id") Long id);

}
