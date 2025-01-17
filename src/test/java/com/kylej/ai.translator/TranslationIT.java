package com.kylej.ai.translator;

import static com.kylej.ai.translator.cache.CacheConfiguration.TRANSLATION_CACHE;
import static com.kylej.ai.translator.service.OpenAiService.getTranslationPrompt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kylej.ai.translator.entity.ChatTranslationRequest;
import com.kylej.ai.translator.entity.ChatTranslationResponse;
import java.util.Locale;
import java.util.Objects;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@SpringBootTest(properties = "cache-props.type=redis")
@AutoConfigureMockMvc
class TranslationIT {

  private static final Locale SPANISH = Locale.of("Spanish");
  private static final String TEXT_TO_TRANSLATE = "How are you?";
  private static final String TRANSLATION_PROMPT = getTranslationPrompt(SPANISH, TEXT_TO_TRANSLATE);
  private static final String CACHE_KEY_NAME = "spanishHow are you?";
  private static final ObjectMapper mapper = new ObjectMapper();
  // replacing bean with MockBean to avoid calls to openAI
  @MockBean
  private OpenAiChatModel chatModel;
  @Autowired
  private CacheManager cacheManager;
  @Autowired
  private MockMvc mockMvc;

  @BeforeEach
  void mockTranslation() {
    clearCache();
    when(chatModel.call(TRANSLATION_PROMPT)).thenReturn("Como estas?");
  }


  @Test
  @SneakyThrows
  void testTranslationSuccess() {

    // Ensure that cache exists with nothing stored.
    Cache cache = cacheManager.getCache(TRANSLATION_CACHE);
    assertThat(cache).isNotNull();
    assertThat(cache.get(CACHE_KEY_NAME)).isNull();

    // first request results in expected translation
    callTextTranslationSuccess();

    // verify cache put
    cache = cacheManager.getCache(TRANSLATION_CACHE);
    assertThat(cache).isNotNull();
    String cachedVal = (String) cache.get(CACHE_KEY_NAME).get();
    assertThat(Objects.requireNonNull(cachedVal)).isNotNull().isEqualTo("Como estas?");

    // chatModel bean is invoked since there was a cache miss
    verify(chatModel).call(TRANSLATION_PROMPT);
    reset(chatModel);

    // second request results in expected translation
    callTextTranslationSuccess();

    // we don't invoke the chatModel functionality the 2nd time due to the cache hit
    verify(chatModel, never()).call(anyString());
  }

  private void callTextTranslationSuccess() {
    ChatTranslationRequest request = ChatTranslationRequest.builder()
        .language("Spanish")
        .textToTranslate("How are you?")
        .build();

    ChatTranslationResponse response = apiPost("/v1/translate",
        request,
        new HttpHeaders(),
        HttpStatus.OK,
        ChatTranslationResponse.class);

    assertThat(response.getTranslatedText()).isEqualTo("Como estas?");
  }

  @SneakyThrows
  public <T> T apiPost(String path, Object request, HttpHeaders headers, HttpStatus expectedStatus,
      Class<T> responseClass) {
    String contentString = mapper.writeValueAsString(request);
    MockHttpServletRequestBuilder action = post(path).content(contentString)
        .contentType(MediaType.APPLICATION_JSON);
    if (headers != null) {
      action = action.headers(headers);
    }
    MvcResult result = mockMvc.perform(action).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(expectedStatus.value());
    return mapper.readValue(result.getResponse().getContentAsString(), responseClass);
  }

  @AfterEach
  void cleanUp() {
    clearCache();
  }

  private void clearCache() {
    if (cacheManager.getCache(TRANSLATION_CACHE) != null) {
      cacheManager.getCache(TRANSLATION_CACHE).invalidate();
    }
  }

  @Test
  void verifyRedisCacheManager() {
    assertThat(cacheManager).isInstanceOf(RedisCacheManager.class);
  }
}
