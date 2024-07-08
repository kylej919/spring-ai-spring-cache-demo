package com.kylej.ai.translator.service;

import static com.kylej.ai.translator.cache.CacheConfiguration.TRANSLATION_CACHE;

import com.kylej.ai.translator.cache.CacheProps;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiService {

  private final OpenAiChatModel chatModel;

  /**
   * Translates the given text to the specified language. Caching layer added based on the
   * combination of language used and text to translate
   * <p>
   * The {@link org.springframework.cache.CacheManager} used is defined by the configuration
   * property {@link CacheProps#getType()}
   *
   * @param locale - language to translate to
   * @param text   - text to translate
   * @return translated text
   */
  @Cacheable(cacheNames = TRANSLATION_CACHE, key = "#locale.language + #text")
  public String translate(Locale locale, String text) {
    return chatModel.call(getTranslationPrompt(locale, text));
  }

  public static String getTranslationPrompt(Locale locale, String text) {
    return STR."Translate the following text to \{locale.getLanguage()}: \{text}";
  }
}
