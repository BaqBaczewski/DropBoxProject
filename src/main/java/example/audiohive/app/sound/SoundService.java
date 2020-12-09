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

    public Page<Sound> findNewestSoundsByUser(User user) {
        return soundRepository.findAllByUserOrderByCreatedAtDesc(user, PageRequest.of(0, 5));
    }

    public Optional<Sound> findById(String id) {
        return soundRepository.findById(id);
    }

    public Sound create(String title, InputStream audioDataStream, long audioDataLength, User user) {
        Blob audioDataBlob = BlobProxy.generateProxy(audioDataStream, audioDataLength);
        Sound sound = new Sound(title, audioDataBlob, Instant.now(), user);
        return soundRepository.save(sound);
    }
}
