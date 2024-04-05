package com.pi.farmease.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChatGPTRequest {
	private String model;

	private List<Message> messages;

}

