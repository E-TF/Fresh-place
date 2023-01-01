package individual.freshplace.repository;

import individual.freshplace.entity.Item;
import individual.freshplace.util.constant.code.category.SubCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Page<Item> findByCategoryCode(SubCategory subCategory, Pageable pageable);
}
