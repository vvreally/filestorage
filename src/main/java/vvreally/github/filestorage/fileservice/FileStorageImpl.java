package vvreally.github.filestorage.fileservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FileStorageImpl implements FileStorage{

	private final Path rootLocation = Paths.get("localFiles");
 
	@Override
	public void store(MultipartFile file, String filename){ //Save file at local storage with name = filename
	    log.info("Try to store file: " + filename + " in /" + rootLocation.toString() + " directory.");
		try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
        } catch (Exception e) {
        	throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
	}
	
	@Override
    public Resource loadFile(String filename) { //Get file from local storage with name = filename
        log.info("Try to load file: " + filename + " in /" + rootLocation.toString() + " directory.");
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }else{
            	throw new RuntimeException("FAIL!");
            }
        } catch (MalformedURLException e) {
        	throw new RuntimeException("Error! -> message = " + e.getMessage());
        }
    }
    
	@Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void delete(String name){
        log.info("Try to delete file: " + name + " in /" + rootLocation.toString() + " directory.");
	    Path file = rootLocation.resolve(name);
	    FileSystemUtils.deleteRecursively(file.toFile());
    }

	@Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!");
        }
    }
}