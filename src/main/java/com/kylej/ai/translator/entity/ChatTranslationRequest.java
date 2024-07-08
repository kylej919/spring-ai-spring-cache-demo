package com.kylej.ai.translator.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ChatTranslationRequest {

    // the language we want to translate to
    private String language;

    // the text we want to translate
    private String textToTranslate;
}
