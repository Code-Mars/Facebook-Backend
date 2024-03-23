package com.facebook.data.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.facebook.data.entity.Data;
import com.facebook.data.exception.DataException;
import com.facebook.data.repository.DataRepository;

@Service(value = "dataService")
@Transactional
public class DataServiceImpl implements DataService{
    	@Autowired
    private DataRepository dataRepository;
	@Autowired
    private Environment environment;
    private Random random=new Random();
@Override
    public String save(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String path = environment.getProperty("storage.path") + fileName;
        
        InputStream is = file.getInputStream();

        OutputStream os = Files.newOutputStream(Path.of(path));
        
        long len = is.available();
        int megaByte = 1024 * 1024;
		for (int i = 0; i < len / megaByte; i++) {
			os.write(is.readNBytes(megaByte));
		}
		os.write(is.readNBytes((int) len % megaByte));

        os.close();
        is.close();

        Data entity = new Data();
        entity.setId(generateId());
        entity.setFileName(fileName);
        entity.setLocation(path);
        entity.setLastUsedOn(LocalDate.now());

        dataRepository.save(entity);

        return entity.getId();
    }
private String generateId() {
        int size = 7;
        String id = generateRandom(size);
        while (dataRepository.existsById(id))
            id = generateRandom(size);
        return id;
    }

    private String generateRandom(int size) {
        String space = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(size);

        for (int i=0; i<size; i++) {
            int randomInt = random.nextInt(space.length());
            sb.append(space.charAt(randomInt));
        }
        return sb.toString();
    }
 @Override
    public Resource fetch(String assetId) throws IOException {
        Data entity = dataRepository.findById(assetId)
                    .orElseThrow(()-> new DataException("SERVICE.NOT_FOUND", HttpStatus.NOT_FOUND));
        String fullFilePath =  environment.getProperty("storage.path") + entity.getFileName();
        Path path = Path.of(fullFilePath);
        Resource file = new UrlResource(path.toUri());
        if (file.exists() || file.isReadable())
            return file;
		else
			throw new DataException("SERVICE.FILE_LOST", HttpStatus.NOT_FOUND);
    }
    
}
