package vvreally.github.filestorage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vvreally.github.filestorage.fileservice.FileStorage;

import javax.annotation.Resource;

@SpringBootApplication
public class FilestorageApplication implements CommandLineRunner {

	@Resource
	FileStorage fileStorage;

	public static void main(String[] args) {
		SpringApplication.run(FilestorageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		fileStorage.deleteAll();
		fileStorage.init();
	}

}
