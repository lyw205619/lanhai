package com.liyiwei.lanhai.ai.service;

import com.liyiwei.lanhai.ai.config.OllamaProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaChatService {

    private final RestTemplate restTemplate;
    private final OllamaProperties props;

    public OllamaChatService(RestTemplate ollamaRestTemplate, OllamaProperties props) {
        this.restTemplate = ollamaRestTemplate;
        this.props = props;
    }

    /**
     * 调用 Ollama /api/chat，非流式。
     *
     * @see <a href="https://github.com/ollama/ollama/blob/main/docs/api.md">Ollama API</a>
     */
    public String chat(String userMessage, String modelOverride) {
        String url = props.getBaseUrl().replaceAll("/$", "") + "/api/chat";
        String model = (modelOverride != null && !modelOverride.isBlank()) ? modelOverride.trim() : props.getModel();

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("stream", false);
        body.put("messages", List.of(
                Map.of("role", "user", "content", userMessage)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> resp = restTemplate.postForObject(url, entity, Map.class);
        if (resp == null) {
            return "";
        }
        Object message = resp.get("message");
        if (message instanceof Map<?, ?> m) {
            Object content = m.get("content");
            return content != null ? content.toString() : "";
        }
        return "";
    }
}
