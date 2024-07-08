package com.kylej.ai.translator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final OpenAiChatModel chatModel;

    /**
     * Translates the given text to the specified language
     *
     * @param locale - language to translate to
     * @param text   - text to translate
     * @return translated text
     */
    public String translate(Locale locale, String text) {
        String result = STR."Translate the following text to \{locale.getLanguage()} \{text}";
        return chatModel.call(result);
    }
}
