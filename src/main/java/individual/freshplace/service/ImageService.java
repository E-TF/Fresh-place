package individual.freshplace.service;

import individual.freshplace.entity.Image;
import individual.freshplace.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void save(final Image image) {
        imageRepository.save(image);
    }

    @Transactional(readOnly = true)
    public List<Image> findByImagePath(final String path) {
        return imageRepository.findByImagePathContaining(path);
    }
}
