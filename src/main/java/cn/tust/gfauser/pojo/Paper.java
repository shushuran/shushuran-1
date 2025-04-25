package cn.tust.gfauser.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Paper {
    private Integer id;
    @NotEmpty
    private String title;
    private String author;
    @NotEmpty
    private String text;
    private LocalDateTime Pushdate;
    private LocalDateTime Updatedate;
    @NotEmpty
    private String type;
}