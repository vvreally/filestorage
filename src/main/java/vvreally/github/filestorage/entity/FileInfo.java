package vvreally.github.filestorage.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String fileName;
    private String author = "";
    private String comment = "";

    private Date placedAt;

    @PrePersist
    void placedAt() {
        this.placedAt = new Date();
    }
}
