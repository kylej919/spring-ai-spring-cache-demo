# Spring AI demo

The repo contains a quick demo on how to use the new [Spring AI library](https://github.com/spring-projects/spring-ai)
to communicate with chatGpt.

This demo currently only tests translating text using chatGpt, but there are many other applications and AI models that
can be configured to be used

To use, replace the configured value in spring.ai.openai.api-key with the value of a chatGpt API key, start the server (
I run Application.java in IntelliJ), and run a sample curl command like this:

``` 
curl --location 'http://localhost:8080/v1/translate' \
--header 'Content-Type: application/json' \
--data '{
"language": "German",
"textToTranslate": "Hello, world!"
}'
``` 