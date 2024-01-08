package com.callum.ai.service;

import com.theokanning.openai.OpenAiHttpException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.ai.client.AiClient;
import org.springframework.stereotype.Service;

@Service
public class AIErrorHandler {

    private final AiClient aiClient;

    public AIErrorHandler(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    public String returnPrettyErrorMessage() {
        final NullPointerException nullPointerException = new NullPointerException("First name cannot be null");
        final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("This argument is illegal", nullPointerException);
        final RuntimeException runtimeException = new RuntimeException("We received an error from a third party", illegalArgumentException);

        try {
            return aiClient.generate("Can you summarise this " + ExceptionUtils.getStackTrace(runtimeException) + " for a non technical end user, giving the end user advice for what is wrong without going into detail?");
        } catch (OpenAiHttpException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}
