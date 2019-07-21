package vvreally.github.filestorage.fileservice;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
	void store(MultipartFile file, String filename);
	Resource loadFile(String filename);
	void deleteAll();
	void init();
	void delete(String name);
}