package com.management.book.services;

import static java.io.File.separator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {

	@Value("${application.file.upload.photos-output-path}")
	private String fileUploadPath;

	public String saveFile(@NonNull MultipartFile sourceFile, @NonNull Long userId) {
		final String fileUploadSubPath = "users" + separator + userId;
		return uploadFile(sourceFile, fileUploadSubPath);
	}

	private String uploadFile(@NonNull MultipartFile sourceFile, @NonNull String fileUploadSubPath) {
		final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath;
		File targetFolder = new File(finalUploadPath);
		
		if(!targetFolder.exists()) {
			boolean folderCreated = targetFolder.mkdirs();
			
			if(!folderCreated) {
				log.warn("Falha ao criar o directorio principal");
				return null;
			}
		}
		
		final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
		String targetFilePath = finalUploadPath + separator + System.currentTimeMillis() + "." + fileExtension;
		Path targetPath = Paths.get(targetFilePath);
		
		try {
			Files.write(targetPath, sourceFile.getBytes());
			log.info("Arquivo salvo no local pretendido");
			
			return targetFilePath;
		}
		catch(IOException e) {
			log.error("Arquivo não foi salvo", e);
		}
		return null;
	}

	private String getFileExtension(String fileName) {
		if(fileName == null || fileName.isEmpty()) {
			return "";
		}
		
		int lastDotIndex = fileName.lastIndexOf(".");
		
		if(lastDotIndex == -1) {
			return "";
		}
		
		return fileName.substring(lastDotIndex + 1).toLowerCase();
	}

}
