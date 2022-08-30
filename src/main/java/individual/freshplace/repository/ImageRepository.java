package individual.freshplace.repository;

import individual.freshplace.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findByImagePathContaining(@Param("imagePath") String imagePath);
}
