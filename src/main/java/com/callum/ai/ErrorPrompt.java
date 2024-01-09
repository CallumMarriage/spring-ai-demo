package com.callum.ai;

public record ErrorPrompt(String httpStatusCode, String errorSummary, String cause, String advice) {
}
