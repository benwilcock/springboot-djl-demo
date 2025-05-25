package com.tanzu.djldemo;

import java.util.Optional;

// TODO: Document this service that obtains percentages.

public interface PercentageService {

    public static final String PERCENTAGE_FORMAT = "##0.00000%";

    public abstract Optional<String> getPercentage(Optional<Double> probability);
    
}
