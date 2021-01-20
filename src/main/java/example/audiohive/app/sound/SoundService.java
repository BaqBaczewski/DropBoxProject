package example.audiohive.app.sound;

import example.audiohive.app.user.User;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Blob;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

        int pageCutLow = currentPage - 1;
        int pageCutHigh = currentPage + 1;
        List<Integer> pages = new LinkedList<>();


        pages.add(1);
        pages.add(2);
        pages.add(3);
        pages.add(4);
        pages.add(5);
        pages.add(-1);
        pages.add(numberOfPages);


return pages;
    }



        public Page<Sound> findNewestSoundsByUser (User user){
            return soundRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 5));
        }

        public Optional<Sound> findById (String id){
            return soundRepository.findById(id);
        }

        public void delete (Sound sound){
            soundRepository.delete(sound);
        }

        public Sound create (String title, InputStream audioDataStream,long audioDataLength, User user, Instant
        createdAt){
            Blob audioDataBlob = BlobProxy.generateProxy(audioDataStream, audioDataLength);
            Sound sound = new Sound(title, audioDataBlob, createdAt, user);
            return soundRepository.save(sound);
        }
    }

