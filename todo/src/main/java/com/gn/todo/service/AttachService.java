package com.gn.todo.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gn.todo.dto.AttachDto;
import com.gn.todo.entity.Attach;
import com.gn.todo.repository.AttachRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachService {
	
	private final AttachRepository attachRepository;
	
	@Value("${ffupload.location}")
	private String fileDir;
	
	public Attach selectAttachOne(Long id) {
		return attachRepository.findById(id).orElse(null);
	}
	
	public List<Attach> selectAttachList(){
		return attachRepository.findAll();
	}
	
	public Attach createAttach(AttachDto dto) {
		Attach param = dto.toEntity();
		return attachRepository.save(param);
	}

	public AttachDto uploadFile(MultipartFile file) {
		AttachDto dto = new AttachDto();
		try {
			if(file == null || file.isEmpty()) {
				throw new Exception("존재하지 않는 파일입니다.");
			}
			long fileSize = file.getSize();
			if(fileSize >= 1048576) {
				throw new Exception("허용 용량을 초과한 파일입니다.");
			}
			
			String oriName = file.getOriginalFilename();
			dto.setOri_name(oriName);
			
			String fileExt 
				= oriName.substring(oriName.lastIndexOf("."),oriName.length());
			
			UUID uuid = UUID.randomUUID();
			String uniqueName = uuid.toString().replaceAll("-", "");
			String newName = uniqueName+fileExt;
			dto.setNew_name(newName);
			String downDir = fileDir+newName;
			dto.setAttach_path(downDir);
			
			File saveFile = new File(downDir);
			if(saveFile.exists() == false) {
				saveFile.mkdirs();
			}
			file.transferTo(saveFile);
			
		}catch(Exception e) {
			dto = null;
			e.printStackTrace();
		}
		return dto;
	} 
}
