package com.example.lab3_news.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;


/**
 * Service Class that work with News API
 */
@Service
@EnableAsync
public class News implements NewsServices{

    /** field with api key for News Api */
    @Value("${some.apikey}")
    private final String apiKey = "&apiKey=f9f1f528312a41cca4e2fada6bf47247";

    /** field with link for News Api */
    private final String URI = "https://newsapi.org/v2/top-headlines?country=";

    /**
     * Method that form request to News Api and get answer with JSON
     * @param country list of country for requested News
     * @param category list of category for requested News
     * @return JSON with answer from API (news)
     */
    @Async
    @Override
    public Future<String>  getJSONorig(String country, String category) {
        String uri;
        String result = null;
            if (!country.equals("def")) {
                if (category.equals("def")) {
                    uri = URI + country + apiKey;
                } else {
                    uri = URI + country + "&category=" + category + apiKey ;
                }
                RestTemplate restTemplate = new RestTemplate();
                result = restTemplate.getForObject(uri, String.class);
            }
            return new AsyncResult<>(result);
        }
}
