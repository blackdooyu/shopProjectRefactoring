package shop.helloshop.domain.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import shop.helloshop.domain.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    @Override
    @EntityGraph(attributePaths = {"member"})
    List<Order> findAll();

    @Query("select distinct o from Order o join fetch o.orderItems where o.member.id = :memberId")
    List<Order> findMemberOrders(@Param("memberId") Long memberId);

    @Query("select o from Order o join fetch o.orderItems where o.id =:orderId")
    Order orderCancel(@Param("orderId") Long orderId);
}
