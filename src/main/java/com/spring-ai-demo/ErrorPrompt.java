package com.spring.ai.demo;

public record ErrorPrompt(String httpStatusCode, String errorSummary, String cause, String advice) {
}
