package vvreally.github.filestorage.controllers;

import vvreally.github.filestorage.data.FileInfoRepository;
import vvreally.github.filestorage.entity.FileInfo;
import vvreally.github.filestorage.fileservice.FileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@Controller
public class FilesController {

    private FileInfoRepository fileInfoRepository;
    private FileStorage fileStorage;

    @Autowired
    public FilesController(FileInfoRepository fileInfoRepository, FileStorage fileStorage) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileStorage = fileStorage;
    }

    @GetMapping("/")
    public String Files(Model model) {
        model.addAttribute("files", fileInfoRepository.findAllByOrderByPlacedAtAsc());
        return "index.html";
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable long id, Model model) {
        Optional<FileInfo> fileCheck = fileInfoRepository.findById(id);
        if (fileCheck.isPresent()) { //If true then delete file from local storage and database
            FileInfo file = fileCheck.get();
            log.info("Delete: " + file);
            fileStorage.delete(file.getFileName());
            fileInfoRepository.delete(file);
            return "redirect:/";
        }
        else {
            return "redirect:/notFound/" + id;
        }
    }


    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Optional<FileInfo> fileCheck = fileInfoRepository.findById(id);
        if (fileCheck.isPresent()) { //If true then return ResponseEntity with file from local storage
            FileInfo fileInfo = fileCheck.get();
            log.info("Download file: " + fileInfo);
            Resource file = fileStorage.loadFile(fileInfo.getFileName());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        }
        else
        {
            log.warn("No file with id = " + id);
            return ResponseEntity.notFound().build(); //404
        }
    }

    @GetMapping("/notFound/{id}")
    public String notFoundId(Model model, @PathVariable long id) {
        log.warn("No file with id = " + id);
        model.addAttribute("notFoundId", "Not found file with id = " + id);
        return "notfound";
    }

}
