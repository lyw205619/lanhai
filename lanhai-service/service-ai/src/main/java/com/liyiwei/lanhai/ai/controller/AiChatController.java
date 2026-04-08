package com.liyiwei.lanhai.ai.controller;

import com.liyiwei.lanhai.ai.service.OllamaChatService;
import com.liyiwei.lanhai.model.vo.common.Result;
import com.liyiwei.lanhai.model.vo.common.ResultCodeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "AI 助手（Ollama）")
@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final OllamaChatService ollamaChatService;

    public AiChatController(OllamaChatService ollamaChatService) {
        this.ollamaChatService = ollamaChatService;
    }

    @Operation(summary = "购物助手对话（转发 Ollama）")
    @PostMapping("/chat")
    public Result<Map<String, String>> chat(@RequestBody Map<String, String> req) {
        String message = req.get("message");
        if (!StringUtils.hasText(message)) {
            return Result.build(null, ResultCodeEnum.DATA_ERROR);
        }
        String model = req.get("model");
        String reply;
        try {
            reply = ollamaChatService.chat(message, model);
        } catch (Exception e) {
            return Result.build(
                    Map.of("error", e.getMessage() != null ? e.getMessage() : "ollama error"),
                    ResultCodeEnum.SYSTEM_ERROR);
        }
        return Result.build(Map.of("reply", reply), ResultCodeEnum.SUCCESS);
    }

    @Operation(summary = "健康检查（Ollama 是否可达）")
    @PostMapping("/ping")
    public Result<Map<String, Object>> ping() {
        return Result.build(Map.of("ok", true, "hint", "确保本机已执行 ollama serve 且已 ollama pull 模型"), ResultCodeEnum.SUCCESS);
    }
}
