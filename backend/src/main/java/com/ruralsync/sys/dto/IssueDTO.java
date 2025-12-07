package com.ruralsync.sys.dto;

import lombok.Data;
import java.util.List;

@Data
public class IssueDTO {
    private String type;
    private String content;
    private List<String> mediaUrls; // Front-end sends list, backend converts to JSON
}
