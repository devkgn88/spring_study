package com.gn.mvc.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="chat_message")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatMessage {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="message_no")
	private Long messageNo;
	
	@OneToOne
	@JoinColumn(name="send_member")
	private Member sendMember;
	
	@ManyToOne
	@JoinColumn(name="room_no")
	private ChatRoom chatRoom;
	
	@Column(name="message_content")
	private String messageContent;
	
	@CreationTimestamp
	@Column(updatable=false, name="send_date")
	private LocalDateTime sendDate;
	

}
