package com.tanzu.djldemo;

/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */

import ai.djl.Application;
import ai.djl.Device;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * An example of inference using DistilBERT for Sentiment Analysis.
 *
 * <p>
 * See this <a
 * href=
 * "https://github.com/deepjavalibrary/djl/blob/master/examples/docs/sentiment_analysis.md">doc</a>
 * for information about this example.*
 */

// TODO: Document this algorithm that predicts the sentiment (good or bad) in an item of text.

@Service
public final class SentimentAnalyzer implements SentimentService{

    private static final Logger logger = LoggerFactory.getLogger(SentimentAnalyzer.class);

    public Optional<Classifications> predict(Optional<String> input)
            throws MalformedModelException, ModelNotFoundException, IOException,
            TranslateException {
        if (!input.isPresent()) {
            logger.warn("You must provide a sentence for analysis. You provided {}.", input);
            return Optional.empty();
        }
        logger.info("Performing a sentiment analysis on this sentence: '{}'", input.get());

        Criteria<String, Classifications> criteria = Criteria.builder()
                .optApplication(Application.NLP.SENTIMENT_ANALYSIS)
                .setTypes(String.class, Classifications.class)
                // This model was traced on CPU and can only run on CPU
                .optDevice(Device.cpu())
                .optProgress(new ProgressBar())
                .build();

        try (ZooModel<String, Classifications> model = criteria.loadModel();
                Predictor<String, Classifications> predictor = model.newPredictor()) {
            return Optional.of(predictor.predict(input.get()));
        }
    }
}
