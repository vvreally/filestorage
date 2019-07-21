package vvreally.github.filestorage.controllers;

import vvreally.github.filestorage.data.FileInfoRepository;
import vvreally.github.filestorage.entity.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Controller
public class EditFiles {

    private FileInfoRepository fileInfoRepository;

    @Autowired
    public EditFiles(FileInfoRepository fileInfoRepository) {
        this.fileInfoRepository = fileInfoRepository;
    }


    @GetMapping("/edit/{id}")
    public String getEditFile(@PathVariable long id, Model model) {
        Optional<FileInfo> fileCheck = fileInfoRepository.findById(id);
        if (fileCheck.isPresent()) { //If true then add fileInfo to model
            var file = fileCheck.get();
            log.info("{GET} Edit: " + file);
            model.addAttribute("file", file);
            return "edit";
        }
        else {
            return "redirect:/notFound/" + id;
        }
    }

    @PostMapping("/update")
    public String editFile(FileInfo fileInfo, Model model) {
        long id = fileInfo.getId();
        Optional<FileInfo> fileCheck = fileInfoRepository.findById(id);
        if (fileCheck.isPresent()){
            fileInfo.setPlacedAt(new Date()); //Update time
            fileInfoRepository.save(fileInfo);
            log.info("Successful edit: " + fileInfo);
            return "redirect:/";
        }
        else {
            return "redirect:/notFound/" + id;
        }
    }
}
