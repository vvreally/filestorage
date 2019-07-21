package vvreally.github.filestorage.data;

import vvreally.github.filestorage.entity.FileInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

    List<FileInfo> findAllByOrderByPlacedAtAsc();
    Optional<FileInfo> findByFileName(String filename);
}
