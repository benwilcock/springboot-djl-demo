package com.tanzu.djldemo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;

import ai.djl.MalformedModelException;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;


@RestController
@RequestMapping(SentimentApiController.BASE_URI)
public class SentimentApiController {

    private static final Logger LOG = LoggerFactory.getLogger(SentimentApiController.class);
    static final String BASE_URI = "/api";

    @Autowired
    SentimentService sentiments;

    @CrossOrigin
	// @Operation(summary = "Get a sentiment analysis on a string of text.",
	//         description = "The API returns a sentiment analysis based on a predefined ML model.",
	// 		tags = {"Message"})
	// @ApiResponses(value = {
	// 		@ApiResponse(responseCode = "200", 
	// 		             description = "A sentiment analysis was performed and the results and are being returned.",
	// 					 content = @Content(schema = @Schema(name = "message", description = "The sentiment analysis on the provided sentence.", type = "map", example = "{\"positive_probability\": \"probability\",\"negative_probability\": \"probability\"}")))
	// })
	@PostMapping(value = "/analyze", produces = {"application/json"})
	public ResponseEntity<SentimentAnalysis2> analyze(@RequestBody Sentence2 sentence) throws MalformedModelException, ModelNotFoundException, IOException, TranslateException {
		LOG.info("A sentence has been received analysis: {}", sentence);
        
        Optional<Classifications> classifications = sentiments.predict(Optional.of(sentence.sentence()));

        if(classifications.isPresent()){
            
            List<String> classNames = classifications.get().getClassNames();
            LOG.info("Class names are: {}", classNames);
            List<Double> probabilities = classifications.get().getProbabilities();
            LOG.info("Probabilities are: {}", probabilities);

            SentimentAnalysis2 sentimentAnalysis = new SentimentAnalysis2(sentence,
                classifications.get().get("Positive").getProbability(),
                classifications.get().get("Negative").getProbability());
            
            return ResponseEntity.ok(sentimentAnalysis); 
        } else {
            return ResponseEntity.ofNullable(null);
        }
	}

    record Sentence2(@JsonProperty("sentence") String sentence){}
    record SentimentAnalysis2(Sentence2 sentence, Double positive_probability, Double negative_probability){}
    
}
