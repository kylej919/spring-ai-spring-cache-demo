package com.kylej.ai.translator.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatTranslationRequest {

    // the language we want to translate to
    private String language;

    // the text we want to translate
    private String textToTranslate;
}
