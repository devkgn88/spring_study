package com.gn.mvc.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gn.mvc.dto.AttachDto;
import com.gn.mvc.entity.Attach;
import com.gn.mvc.entity.Board;
import com.gn.mvc.repository.AttachRepository;
import com.gn.mvc.repository.BoardRepository;
import com.gn.mvc.specification.AttachSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachService {

	@Value("${ffupload.location}")
	private String fileDir;
	
	private final AttachRepository repository;
	private final BoardRepository boardRepository;
	
	public Attach selectAttachOne(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public List<Attach> selectAttachList(Long boardNo){
		Board board = boardRepository.findById(boardNo).orElse(null);
		Specification<Attach> spec = (root,query,criteriaBuilder) -> null; 
		spec = spec.and(AttachSpecification.boardEquals(board));
		return repository.findAll(spec);
	}
	
	public AttachDto uploadFile(MultipartFile file) {
		AttachDto dto = new AttachDto();
		try {
			// 1. 정상 파일 여부 확인
			if(file == null || file.isEmpty()) {
				throw new Exception("존재하지 않는 파일입니다.");
			}
			// 2. 파일 최대 용량 체크
			long fileSize = file.getSize();
			if( fileSize <= 0 || 5242880 < fileSize) {
				throw new Exception("파일 크기 : "+fileSize+"(파일 사이즈가 5MB를 넘습니다.)");
			}
			// 3. 파일 최초 이름
			String oriName = file.getOriginalFilename();
			dto.setOri_name(oriName);
			// 4. 파일 확장자 자르기
			String fileExt
				= oriName.substring(oriName.lastIndexOf("."),oriName.length());
			// 5. 파일 명칭 바꾸기
			UUID uuid = UUID.randomUUID();
			// 6. 8자리마다 포함되는 하이픈 제거
			String uniqueName = uuid.toString().replaceAll("-", "");
			// 7. 새로운 파일명
			String newName = uniqueName+fileExt;
			dto.setNew_name(newName);
			// 8. 파일 저장 경로 설정
			// 9. 파일 껌데기 생성
			String downDir = fileDir+"board/"+newName;
			dto.setAttach_path(downDir);
			File saveFile = new File(downDir);
			// 10. 경로 존재 여부 확인
			if(!saveFile.exists()) {
				saveFile.mkdirs();
			}
			// 11. 껍데기에 파일 정보 복제
			file.transferTo(saveFile);
			
		}catch(Exception e) {
			dto = null; 
			e.printStackTrace();
		}
		return dto;
	}
}
