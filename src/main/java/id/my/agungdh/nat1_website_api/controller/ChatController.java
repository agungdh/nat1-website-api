package id.my.agungdh.nat1_website_api.controller;

import id.my.agungdh.nat1_website_api.dto.ChatRequest;
import id.my.agungdh.nat1_website_api.dto.ChatResponse;
import id.my.agungdh.nat1_website_api.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String reply = chatService.chat(request.messages());
        return new ChatResponse(reply);
    }
}
