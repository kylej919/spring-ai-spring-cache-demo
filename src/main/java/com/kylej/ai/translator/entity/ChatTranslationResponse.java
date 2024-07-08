package com.kylej.ai.translator.entity;

import lombok.*;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatTranslationResponse {

    // the language that was translated to
    private Locale language;

    // the original text that was translated
    private String originalText;

    // the resulting translated text
    private String translatedText;
}
