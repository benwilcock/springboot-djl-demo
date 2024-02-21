# Spring Boot Sentiment Analysis Demo

I wrote this quick demo to show how to build a sentiment analysis microservice. The service will take any sentence and five tou a sentiment analysis for that sentence. The service communicates via a REST API like so:

```text
POST http://localhost:8080/api/analyze 
content-Type: application/json

{
  "sentence": "I like DJL. DJL is the best DL framework!"
}
```

see the file [local-api-tests.http](local-api-tests.http) for more examples.
