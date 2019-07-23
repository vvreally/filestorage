package vvreally.github.filestorage.controllers;


import org.springframework.ui.Model;
import vvreally.github.filestorage.data.FileInfoRepository;
import vvreally.github.filestorage.entity.FileInfo;
import vvreally.github.filestorage.fileservice.FileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vvreally.github.filestorage.services.ControllersServices;

import java.text.ParseException;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/upload")
public class UploadFiles {

    private ControllersServices services;

    @Autowired
    public UploadFiles(ControllersServices services) {
        this.services = services;
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
    public String uploadMultipartFile(@RequestParam String fileName, @RequestParam("uploadFile") MultipartFile file,
                                      FileInfo fileInfo, @RequestParam String date) throws ParseException {
        String filename = services.matchingNames(fileName);
        fileInfo.setFileName(filename);
        fileInfo.setPlacedAt(services.stringToDate(date));
        services.storeFileAndSaveInfo(file, fileInfo);
        return "redirect:/";
    }

}
