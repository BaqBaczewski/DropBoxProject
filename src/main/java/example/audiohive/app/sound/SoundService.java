package example.audiohive.app.sound;

import example.audiohive.app.image.Image;
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
public class SoundService {

    private final SoundRepository soundRepository;

    @Autowired
    SoundService(SoundRepository soundRepository) {
        this.soundRepository = soundRepository;
    }

    public Page<Sound> findNewSounds(Pageable pageable) {
        return soundRepository.findAllByOrderByCreatedAtDesc(pageable);
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
        if (currentPage == 0) {
            return List.of(1, 2, 3, -1, numberOfPages);
        }
        if (currentPage == 2) {
            return List.of(1, 2, 3, 4, 5, numberOfPages);
        }
        if (currentPage == 3) {
            return List.of(1, 2, 3, 4, 5, numberOfPages);
        }
        if (currentPage == 4) {
            return List.of(1, -1, 3, 4, 5, numberOfPages);
        }
        // 6 and more pages active...
        if (currentPage == 0) { // first page with more than 5 pages active
            return List.of(1, 2, 3, -1, numberOfPages);
        }
        if (currentPage == 1) { // second page with more than 5 pages active
            return List.of(1, 2, 3, 4, -1, numberOfPages);
        }
        if (currentPage == 2 && numberOfPages == 5) { // third page with more than 5 pages active
            return List.of(1, currentPage, currentPage + 1, currentPage + 2, currentPage + 3, -1, numberOfPages);
        }
        if (currentPage == 3 && numberOfPages > 5 ) {
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


    public Page<Sound> findNewestSoundsByUser(User user) {
        return soundRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 5));
    }

    public Optional<Sound> findById(String id) {
        return soundRepository.findById(id);
    }

    public void delete(Sound sound) {
        soundRepository.delete(sound);
    }

    public Sound create(String title, InputStream audioDataStream, long audioDataLength, User user, Instant createdAt, String description) {
        Blob audioDataBlob = BlobProxy.generateProxy(audioDataStream, audioDataLength);
        Sound sound = new Sound(title, audioDataBlob, createdAt, user, description);
        return soundRepository.save(sound);
    }

    public Sound changeDescriptionSound(String soundId, String newDescription) {
        Sound sound = findById(soundId).orElseThrow();
        sound.setDescription(newDescription);
        return soundRepository.save(sound);
    }

    public Sound changeTitleSound(String soundId, String newTitle) {
        Sound sound = findById(soundId).orElseThrow();
        sound.setTitle(newTitle);
        return soundRepository.save(sound);
    }
}

