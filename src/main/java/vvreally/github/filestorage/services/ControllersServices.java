package vvreally.github.filestorage.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vvreally.github.filestorage.data.FileInfoRepository;
import vvreally.github.filestorage.entity.FileInfo;
import vvreally.github.filestorage.fileservice.FileStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class ControllersServices {

    private FileInfoRepository fileInfoRepository;
    private FileStorage fileStorage;

    @Autowired
    public ControllersServices(FileInfoRepository fileInfoRepository, FileStorage fileStorage) {
        this.fileInfoRepository = fileInfoRepository;
        this.fileStorage = fileStorage;
    }

    public String notFound(long id) {
        return "redirect:/notFound/" + id;
    }


    public String matchingNames(String fileName) {
        String filename = fileName;
        Optional<FileInfo> fileCheck = fileInfoRepository.findByFileName(filename);
        while (fileCheck.isPresent()) {  //Check matching names
            log.warn(String.format("%s file already exists. Save with new name.", filename));
            filename += "(1)";
            fileCheck = fileInfoRepository.findByFileName(filename);
        }
        return filename;
    }

    public Date stringToDate(String date) throws ParseException {
        Date new_date;
        log.info("Try to parse " + date);
        try {
            new_date = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        }
        catch (ParseException e) {
            log.error(e.getMessage());
            throw e;
        }
        return new_date;
    }

    public void storeFileAndSaveInfo(MultipartFile file, FileInfo fileInfo) {
        log.info("Save file: " + fileInfo + ". Size = " + file.getSize() / 1024 + "KB");
        fileStorage.store(file, fileInfo.getFileName());
        fileInfoRepository.save(fileInfo);
    }
}
