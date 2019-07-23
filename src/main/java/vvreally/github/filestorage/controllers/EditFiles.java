package vvreally.github.filestorage.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vvreally.github.filestorage.data.FileInfoRepository;
import vvreally.github.filestorage.entity.FileInfo;
import vvreally.github.filestorage.fileservice.FileStorage;
import vvreally.github.filestorage.services.ControllersServices;

import java.text.ParseException;
import java.util.Optional;

@Slf4j
@Controller
public class EditFiles {

    private FileStorage fileStorage;
    private FileInfoRepository fileInfoRepository;
    private ControllersServices services;

    @Autowired
    public EditFiles(FileStorage fileStorage, FileInfoRepository fileInfoRepository, ControllersServices services) {
        this.fileStorage = fileStorage;
        this.fileInfoRepository = fileInfoRepository;
        this.services = services;
    }


    @PostMapping("/update")
    public String uploadAndUpdate(@RequestParam String fileName, @RequestParam("uploadFile") MultipartFile file,
                                  FileInfo fileInfo, @RequestParam String date) throws ParseException {

        Optional<FileInfo> oldFileInfo = fileInfoRepository.findById(fileInfo.getId());
        if (oldFileInfo.isEmpty())
            return services.notFound(fileInfo.getId());

        String oldFileName = oldFileInfo.get().getFileName();
        fileInfo.setPlacedAt(services.stringToDate(date));

        if (!file.isEmpty()) { //If true then delete oldfile and store new
            fileStorage.delete(oldFileName);
            String filename = services.matchingNames(fileName);
            fileInfo.setFileName(filename);
            services.storeFileAndSaveInfo(file, fileInfo);
        }

        else {
            log.info("Update fileInfo: " + fileInfo);
            fileInfoRepository.save(fileInfo);
        }

        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String getEditFile(@PathVariable long id, Model model) {
        Optional<FileInfo> fileCheck = fileInfoRepository.findById(id);
        if (fileCheck.isPresent()) { //If true then add fileInfo to model
            var fileInfo = fileCheck.get();
            log.info("{GET} Edit: " + fileInfo);
            model.addAttribute("file", fileInfo);
            return "edit";
        }
        else
            return services.notFound(id);
    }
}
