package com.kylej.ai.translator.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChatTranslationRequest {

    private String language;
    private String textToTranslate;
}
