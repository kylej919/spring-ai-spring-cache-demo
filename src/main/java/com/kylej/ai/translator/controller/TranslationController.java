package com.kylej.ai.translator.controller;

import com.kylej.ai.translator.entity.ChatTranslationRequest;
import com.kylej.ai.translator.entity.ChatTranslationResponse;
import com.kylej.ai.translator.exception.InvalidLanguageException;
import com.kylej.ai.translator.service.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class TranslationController {

    private final OpenAiService openAiService;

    @PostMapping("/v1/translate")
    public ChatTranslationResponse translate(@RequestBody ChatTranslationRequest request) throws InvalidLanguageException {
        Locale locale = checkLanguage(request.getLanguage());
        String originalText = request.getTextToTranslate();
        String translatedText = openAiService.translate(locale, originalText);
        return ChatTranslationResponse.builder().language(locale).originalText(originalText).translatedText(translatedText).build();
    }

    private Locale checkLanguage(String language) throws InvalidLanguageException {
        try {
            return Locale.of(language);
        } catch (Exception e) {
            throw new InvalidLanguageException(STR."Could not resolve language \{language}");
        }
    }
}
