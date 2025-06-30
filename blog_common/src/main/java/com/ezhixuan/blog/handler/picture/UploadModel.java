package com.ezhixuan.blog.handler.picture;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UploadModel {

    private String model;

    private String desc;
}
