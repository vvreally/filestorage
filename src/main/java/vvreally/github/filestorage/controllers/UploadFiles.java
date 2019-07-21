package vvreally.github.filestorage.controllers;


import vvreally.github.filestorage.data.FileInfoRepository;
import vvreally.github.filestorage.entity.FileInfo;
import vvreally.github.filestorage.fileservice.FileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/upload")
public class UploadFiles {

    private FileStorage fileStorage;
    private FileInfoRepository fileInfoRepository;

    @Autowired
    public UploadFiles(FileStorage fileStorage, FileInfoRepository fileInfoRepository) {
        this.fileStorage = fileStorage;
        this.fileInfoRepository = fileInfoRepository;
    }

    @GetMapping
    public String uploadForm() {
        return "uploadForm";
    }

    @ModelAttribute(name = "fileInfo")
    public FileInfo fileInfoModel() {
        return new FileInfo();
    }

    @PostMapping
    public String uploadMultipartFile(@RequestParam("uploadFile") MultipartFile file, FileInfo fileInfo) {
        String filename = file.getOriginalFilename();
        Optional<FileInfo> fileCheck = fileInfoRepository.findByFileName(filename);
        while (fileCheck.isPresent()) {  //Check matching names
            log.warn(String.format("%s file already exists. Save with new name.", filename));
            filename += "(1)";
            fileCheck = fileInfoRepository.findByFileName(filename);
        }
        fileStorage.store(file, filename);
        fileInfo.setFileName(filename);
        fileInfoRepository.save(fileInfo);
        log.info("Save file: " + fileInfo + ". Size = " + file.getSize() / 1024 + "KB");
        return "redirect:/";
    }

}
