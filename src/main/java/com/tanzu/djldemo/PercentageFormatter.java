package com.tanzu.djldemo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PercentageFormatter implements PercentageService {

    private static final Logger LOG = LoggerFactory.getLogger(PercentageFormatter.class);

    @Override
    public Optional<String> getPercentage(Optional<Double> probability) {

        LOG.debug("Creating a percentage from the value: {}", probability.get());

        if (probability.isPresent()) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setGroupingSeparator(' ');
            DecimalFormat decimalFormat = new DecimalFormat(PercentageService.PERCENTAGE_FORMAT, symbols);
            String formattedString = decimalFormat.format(probability.get());
            LOG.debug("Returning the percentage value: {}", formattedString);
            return Optional.of(formattedString);
        } else {
            LOG.error("The check that the optional probability value was present has failed: {}", probability);
            throw new IllegalArgumentException("The probability cannot be a null or an empty value.");
        }
    }

}
