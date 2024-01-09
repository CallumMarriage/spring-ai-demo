package com.callum.ai.service;

import com.callum.ai.ErrorPrompt;
import com.theokanning.openai.OpenAiHttpException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.client.AiResponse;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class AIErrorHandler {

    private final static BeanOutputParser<ErrorPrompt> BEAN_OUTPUT_PARSER = new BeanOutputParser<>(ErrorPrompt.class);

    private static final String PROMPT_STRING = """
        Can you summarize this error {error} for a non technical end user, explaining the root cause and giving the end user advice for what is wrong without going into detail and assign an appropriate http status code?
        {format}
        """;

    private final AiClient aiClient;

    public AIErrorHandler(AiClient aiClient) {
        this.aiClient = aiClient;
    }

    public ErrorPrompt returnPrettyErrorMessage() {
        final RuntimeException runtimeException = getException();

        final PromptTemplate promptTemplate = new PromptTemplate(PROMPT_STRING);
        promptTemplate.add("error", ExceptionUtils.getStackTrace(runtimeException));
        promptTemplate.add("format", BEAN_OUTPUT_PARSER.getFormat());
        promptTemplate.setOutputParser(BEAN_OUTPUT_PARSER);
        try {
            final AiResponse aiResponse = aiClient.generate(promptTemplate.create());
            return BEAN_OUTPUT_PARSER.parse(aiResponse.getGeneration().getText());
        } catch (OpenAiHttpException e) {
            return new ErrorPrompt("500", "AI Error", e.getMessage(), "Please fix");
        }
    }

    public String returnPrettyErrorMessageSimple() {
        final RuntimeException runtimeException = getException();
        try {
            return aiClient.generate(
                "Can you summarize this error "
                    + ExceptionUtils.getStackTrace(runtimeException)
                    + " for a non technical end user, explaining the root cause and giving the end user advice for what is wrong without going into detail and assign an appropriate http status code?"
            );
        } catch (OpenAiHttpException e) {
            return "Please fix";
        }
    }

    private RuntimeException getException() {
        final NullPointerException nullPointerException = new NullPointerException("First name cannot be null");
        final IllegalArgumentException illegalArgumentException = new IllegalArgumentException("This argument is illegal", nullPointerException);
        return new RuntimeException("We received an error from a third party", illegalArgumentException);
    }
}
