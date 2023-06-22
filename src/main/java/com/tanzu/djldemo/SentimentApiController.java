package com.tanzu.djldemo;

import java.io.IOException;

import java.util.List;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.djl.MalformedModelException;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(SentimentApiController.BASE_URI)
public class SentimentApiController {

    private static final Logger LOG = LoggerFactory.getLogger(SentimentApiController.class);
    static final String BASE_URI = "/api";

    @Autowired
    SentimentService sentiments;

    @CrossOrigin
    @Operation(summary = "Get a sentiment analysis on a sentence of text.", description = "The API returns a sentiment analysis based on a predefined ML model.", tags = {"Sentence" })
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "A sentiment analysis was performed and the results and are being returned.", content = @Content(schema = @Schema(name = "message", description = "The sentiment analysis on the provided sentence.", type = "map", example = "{\"sentence\": \"This was your sentence\",\"positive_probability\": 0.00,\"negative_probability\": 0.00}")))})
    @PostMapping(value = "/analyze", produces = { "application/json" })
    public ResponseEntity<SentimentAnalysis> analyze(@RequestBody Sentence sentence)
            throws MalformedModelException, ModelNotFoundException, IOException, TranslateException {
        LOG.info("A sentence has been received analysis: {}", sentence.sentence());

        Optional<Classifications> classifications = sentiments.predict(Optional.of(sentence.sentence()));

        if (classifications.isPresent()) {

            LOG.info("Sentiment probabilities are: {}", classifications.get().items());

            SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(sentence.sentence(),
                    classifications.get().get("Positive").getProbability(),
                    classifications.get().get("Negative").getProbability());

            return ResponseEntity.ok(sentimentAnalysis);
        } else {
            return ResponseEntity.ofNullable(null);
        }
    }

    record Sentence(@JsonProperty("sentence") String sentence) {
    }

    record SentimentAnalysis(String sentence, Double positive_probability, Double negative_probability) {
    }

}
