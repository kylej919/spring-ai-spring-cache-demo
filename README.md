# Spring AI and Spring Cache demo

The repo contains a quick demo on how to use the
new [Spring AI library](https://github.com/spring-projects/spring-ai)
to communicate with chatGpt.

It also contains an implementation of Spring Cache for both redis and caffeine, which is used to
cache the results from the chatGpt API

This demo currently only tests translating text using chatGpt, but there are many other applications
and AI models that
can be configured to be used

To use, do the following:

1.

In [application.yml](https://github.com/kylej919/ai-demo/blob/main/src/main/resources/application.yml)
replace the configured value in spring.ai.openai.api-key with the value of a chatGpt API key

2. Start the server (I run Application.java in IntelliJ). Requires Java 21 and maven to be installed
3. Run a sample curl command like this:

``` 
curl --location 'http://localhost:8080/v1/translate' \
--header 'Content-Type: application/json' \
--data '{
"language": "German",
"textToTranslate": "Hello, world!"
}'
``` 

but use whatever language and text you want

There's also an example of using conditional spring beans combined with application properties to
choose which type of cache you want to use in your application. By selecting 'redis' for
cache-props.type, the application will use redis, otherwise it'll use caffeine cache. The test class
TranslationIT contains some test cases to validate the cache behavior 