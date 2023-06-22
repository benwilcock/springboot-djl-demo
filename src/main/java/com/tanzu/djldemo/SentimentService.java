package com.tanzu.djldemo;

import java.io.IOException;
import java.util.Optional;

import ai.djl.MalformedModelException;
import ai.djl.modality.Classifications;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.translate.TranslateException;

public interface SentimentService {

    public abstract Optional<Classifications> predict(Optional<String> input)
            throws MalformedModelException, ModelNotFoundException, IOException,
            TranslateException;
}
