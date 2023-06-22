package com.tanzu.djldemo;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import ai.djl.ModelException;
import ai.djl.modality.Classifications;
import ai.djl.translate.TranslateException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition( 
	tags = {@Tag(name = "Springboot-Maven Deep Java Learing API Demo", 
                 description = "An API for obtaining a sentiment analysis on a message that you provide.")},
    info = @Info(title = "Messages API", version = "0.1-SNAPSHOT", 
                 description = "An API for obtaining a sentiment analysis on a message that you provide"),
    servers = {
       @Server(url = "https://djl-demo.tap-next.blah.cloud", description = "Spring")
    }
) 


@SpringBootApplication
public class DjlDemoApplication {

	@Autowired
	SentimentService sentiments;

	private static final Logger logger = LoggerFactory.getLogger(DjlDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DjlDemoApplication.class, args);
	}

	@EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws IOException, TranslateException, ModelException {
        
		Classifications classifications = sentiments.predict(Optional.of("I like DJL. DJL is the best DL framework!")).get();
        logger.info(classifications.toString());
    }

}
