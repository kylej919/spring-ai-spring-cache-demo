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

    /**
     * Defines a mapping for the POST method to the /v1/translate endpoint.
     * <p>
     * Demo endpoint only: Assumes the original text is in English, and that the language specified is not English.
     *
     * @param request - contains the language and text to translate
     * @return - response object containing the translated text
     * @throws InvalidLanguageException - if the provided language is invalid
     */
    @PostMapping("/v1/translate")
    public ChatTranslationResponse translate(@RequestBody ChatTranslationRequest request) throws InvalidLanguageException {
        Locale locale = checkLanguage(request.getLanguage());
        String originalText = request.getTextToTranslate();
        String translatedText = openAiService.translate(locale, originalText);
        return ChatTranslationResponse.builder().language(locale).originalText(originalText).translatedText(translatedText).build();
    }

    /**
     * Attempts to resolve the provided language string to a Locale object, which verifies the text correlates to a
     * language. Catches runtime exceptions involved and throws a checked exception instead, InvalidLanguageException.
     *
     * @param language - text to resolve to a Locale language object
     * @return - Locale object representing the language
     * @throws InvalidLanguageException - if the provided language text does not correspond to a language
     */
    private Locale checkLanguage(String language) throws InvalidLanguageException {
        try {
            return Locale.of(language);
        } catch (Exception e) {
            throw new InvalidLanguageException(STR."Could not resolve language \{language}");
        }
    }
}
