package shop.helloshop.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import shop.helloshop.domain.entity.items.Item;
import shop.helloshop.domain.entity.items.QItem;

import java.util.List;

import static shop.helloshop.domain.entity.items.QItem.*;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> itemBySortList(Pageable pageable) {
        List<Item> content = queryFactory
                .selectFrom(item)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(item.count())
                .from(item)
                .fetchOne();

        return new PageImpl<>(content,pageable,count);
    }
}
