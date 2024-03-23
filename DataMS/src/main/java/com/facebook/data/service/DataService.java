package com.facebook.data.service;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface DataService {
    public String save(MultipartFile file) throws IOException;
    public Resource fetch(String assetId) throws IOException;
}
