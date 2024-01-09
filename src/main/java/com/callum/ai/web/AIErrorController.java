package com.callum.ai.web;

import com.callum.ai.service.AIErrorHandler;
import com.callum.ai.ErrorPrompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIErrorController {

    private final AIErrorHandler aiErrorHandler;

    public AIErrorController(AIErrorHandler aiErrorHandler) {
        this.aiErrorHandler = aiErrorHandler;
    }

    @GetMapping("/error/summary")
    public ErrorPrompt getErrorSummary() {
        return aiErrorHandler.returnPrettyErrorMessage();
    }

    @GetMapping("/error/summary/simple")
    public String getErrorSummarySimple() {
        return aiErrorHandler.returnPrettyErrorMessageSimple();
    }
}
