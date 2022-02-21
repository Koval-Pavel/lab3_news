package com.example.lab3_news.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Class for get Values from apllication.properties
 */
@ConfigurationProperties(prefix="spring.demo")
@Configuration
public class GetValue {

    /** field with selected quantity of news*/
    private String quantityOfNews;

    /** field with key from News Api*/
    private String apikey;

    public String getQuantityOfNews() {
        return quantityOfNews;
    }

    public void setQuantityOfNews(String quantityOfNews) {
        this.quantityOfNews = quantityOfNews;
    }

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

}