package individual.freshplace.repository;

import individual.freshplace.entity.Item;
import individual.freshplace.util.constant.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByCategoryCode(CategoryType categoryType);
}
