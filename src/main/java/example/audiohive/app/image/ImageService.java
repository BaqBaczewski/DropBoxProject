package example.audiohive.app.image;

import example.audiohive.app.user.User;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Blob;
import java.time.Instant;
import java.util.*;

@Service
public class ImageService {

    private ImageRepository imageRepository;

    @Autowired
    ImageService(ImageRepository imageRepository) {this.imageRepository = imageRepository;}

    public Page<Image> findNewImages(Pageable pageable) {
        return imageRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    public static List<Integer> createPagination(int currentPage, int numberOfPages) {

        List<Integer> pages = new LinkedList<>();

        pages.add(1);
        pages.add(2);
        pages.add(3);
        pages.add(4);
        pages.add(5);
        pages.add(-1);
        pages.add(numberOfPages);

        System.out.println(pages);

        // only 1 page active
        if (currentPage == 0 && numberOfPages == 1) {
            return List.of(1);
        }
        // only 2 pages active
        if (currentPage >= 0 && numberOfPages == 2) {
            return List.of(1, 2);
        }
        // only 3 pages active
        if (currentPage >= 0 && numberOfPages == 3) {
            return List.of(1, 2, 3);
        }
        // only 4 pages active
        if (currentPage >= 0 && numberOfPages == 4) {
            return List.of(1, 2, 3, 4);
        }
        // only 5 pages active
        if (currentPage == 0 && numberOfPages == 5) {
            return List.of(1, 2, 3, -1, 5);
        } else if (currentPage == 4 && numberOfPages ==5)
            return List.of(1, -1, 3, 4, 5);
        if (currentPage >= 0 && numberOfPages == 5) {
            return List.of(1, 2, 3, 4, 5);
        }
        // 6 and more pages active...
        if (currentPage == 0) { // first page with more than 5 pages active
            return List.of(1, 2, 3, -1, numberOfPages);
        }
        if (currentPage == 1) { // second page with more than 5 pages active
            return List.of(1, 2, 3, 4, -1, numberOfPages);
        }
        if (currentPage == 2) { // third page with more than 5 pages active
            return List.of(1, currentPage, currentPage + 1, currentPage + 2, currentPage + 3, -1, numberOfPages);
        }
        if (currentPage == 3) {
            return List.of(1, currentPage - 1, currentPage, currentPage + 1,currentPage+2, currentPage+3,-1, numberOfPages);
        }
        if (currentPage >= 4 && currentPage < numberOfPages-4) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2, currentPage + 3, -1, numberOfPages);
        }
        if (currentPage >= 4 && currentPage < numberOfPages - 3) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2,currentPage+3, numberOfPages);
        }
        if (currentPage >= 4 && currentPage == numberOfPages - 3) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2, numberOfPages);
        }
        if (currentPage > 4 && currentPage < numberOfPages - 2) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, currentPage + 2, numberOfPages);
        }
        if (currentPage > 4 && currentPage == numberOfPages - 2) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, numberOfPages);
        }
        if (currentPage > 4 && currentPage < numberOfPages - 1) {
            return List.of(1, -1, currentPage - 1, currentPage, currentPage + 1, numberOfPages);
        }
        if (numberOfPages == currentPage +1) { // when last page reached
            return List.of(1, -1, currentPage - 1, currentPage, numberOfPages);
        }
        return null;
    }

    public Page<Image> findNewestImagesByUser(User user) {
        return imageRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 5));
    }

    public Optional<Image> findById(String id) {return imageRepository.findById(id);}

    public void delete(Image image) {imageRepository.delete(image);}

    public Image create(String title, InputStream imageDataStream, long imageDataLength, User user, Instant createdAt, String description) {
        Blob imageDataBlob = BlobProxy.generateProxy(imageDataStream, imageDataLength);
        Image image = new Image(title, imageDataBlob, createdAt, user, description);
        return imageRepository.save(image);
    }
}
