package com.gn.mvc.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gn.mvc.entity.Attach;
import com.gn.mvc.service.AttachService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AttachController {

	private final AttachService service;
	
	@GetMapping("/download/{id}")
	public ResponseEntity<Object> fileDownload(@PathVariable("id") Long id, HttpServletRequest request) {
		
		try {
			Attach fileData = service.selectAttachOne(id);
			
			if(fileData == null) {
				return ResponseEntity.notFound().build();
			}
			
			Path filePath = Paths.get(fileData.getAttachPath());
			Resource resource = new InputStreamResource(Files.newInputStream(filePath));
			
			// 한글 파일명 인코딩
			String originalFileName = fileData.getOriName();
			String encodedFileName = getEncodedFileName(originalFileName, request); // 공백 처리
            
            // 응답 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+encodedFileName);


			
			return new ResponseEntity<Object>(resource,headers,HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}
	
	private String getEncodedFileName(String fileName, HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        try {
            if (userAgent.contains("MSIE") || userAgent.contains("Trident") || userAgent.contains("Edge")) {
                // IE, Edge(레거시) 대응: URL 인코딩
                return URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                        .replaceAll("\\+", "%20"); // 공백 처리
            } else {
                // Chrome, Firefox, Edge(Chromium) 대응: RFC 5987 방식
                return URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            return fileName; // 인코딩 실패 시 원래 파일명 반환
        }
    }
}
