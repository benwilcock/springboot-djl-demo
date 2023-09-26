package com.tanzu.djldemo;

import java.io.IOException;

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
import com.tanzu.djldemo.SentimentApiControllerAdvice.ErrorDescription;

import ai.djl.MalformedModelException;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(SentimentApiController.BASE_URI)
public class SentimentApiController {

    private static final Logger LOG = LoggerFactory.getLogger(SentimentApiController.class);
    static final String BASE_URI = "/api";

    static final String POSITIVE = "Positive";
    static final String NEGATIVE = "Negative";

    @Autowired
    SentimentService sentiments;

    @Autowired
    PercentageService percentages;

    @CrossOrigin 
    @Operation(summary="Get a sentiment analysis on a sentence of text.",description="The API returns a sentiment analysis based on a predefined ML model.",tags={"Sentence"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = SentimentAnalysis.class))),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDescription.class)))
    })
    @PostMapping(value = "/analyze", produces = { "application/json" })
    public ResponseEntity<SentimentAnalysis> analyze(@RequestBody Sentence sentence)
            throws MalformedModelException, ModelNotFoundException, IOException, TranslateException {
        LOG.info("A sentence has been received analysis: {}", sentence.sentence());

        if (!isNullOrEmptyOrContainsNoWords(sentence.sentence)) {

            // Perform the sentiment analysis on the sentence
            Optional<Classifications> classifications = sentiments.predict(Optional.of(sentence.sentence()));

            // Perform a null check and continue
            if (classifications.isPresent()) {

                LOG.info("Sentiment probabilities are: {}", classifications.get().items());

                // There's a bit of duplication here but I left it in for clarity

                // Convert the positive probability into a percentage
                Double positive_probability = classifications.get().get(POSITIVE).getProbability();
                String positive_percentage = percentages
                        .getPercentage(Optional.of(Double.valueOf(positive_probability))).get();

                // Convert the negative probability into a percentage
                Double negative_probability = classifications.get().get(NEGATIVE).getProbability();
                String negative_percentage = percentages
                        .getPercentage(Optional.of(Double.valueOf(negative_probability))).get();

                // Create the SentimentAnalysis return object
                SentimentAnalysis sentimentAnalysis = new SentimentAnalysis(sentence.sentence(),
                        positive_percentage,
                        negative_percentage);

                // pass it back
                return ResponseEntity.ok(sentimentAnalysis);
            } else {
                LOG.error("The check that the optional classification values were present has failed: {}",
                        classifications);
                throw new RuntimeException(
                        "Unable to perform the sentiment analysis. There was an unexpected issue getting the probabilities and returning the analysis.");
            }

        } else {
            LOG.error("The check that the sentence contained at least a word has failed: {}", sentence.sentence());
            throw new IllegalArgumentException("The sentence given appears to contain no words?");
        }

    }

    private boolean isNullOrEmptyOrContainsNoWords(String str) {
        return str == null || str.trim().isEmpty() || !str.matches(".*\\w.*");
    }

    @Schema(description = "The sentence to analyze")
    public record Sentence(
        @JsonProperty("sentence") String sentence) {
    }

    @Schema(description = "The result of a sentiment analysis")
    public record SentimentAnalysis(
        @Schema(description = "The sentence to analyze") String sentence, 
        @Schema(description = "The probability that the sentence is positive in the format ##0.00000%") String positive_probability, 
        @Schema(description = "The probability that the sentence is negative in the format ##0.00000%") String negative_probability) {
    }

}
