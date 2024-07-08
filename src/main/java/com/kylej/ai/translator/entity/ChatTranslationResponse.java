package com.kylej.ai.translator.entity;

import lombok.*;

import java.util.Locale;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatTranslationResponse {
    private Locale language;
    private String originalText;
    private String translatedText;
}
