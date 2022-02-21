package com.example.lab3_news.controllers;

import com.example.lab3_news.handler.handlerJSON;
import com.example.lab3_news.services.GetValue;
import com.example.lab3_news.services.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.example.lab3_news.Lab3NewsApplication.log;
import static com.example.lab3_news.handler.handlerJSON.resultJSON;

/**
 * Class for Controller of News services.
 * @author  Pavel Koval
 */
@RestController
@EnableAsync
@EnableCaching
public class NewsController {

    /**
     * Bean for GetValue class
     */
    @Autowired
    private GetValue getValue;

    /**
     * Object of News Services
     */
    private News news = new News();

    /**
     * List of choosen countrys for News
     */
    public static List<String> countryGlob;

    /** Info message field. */
    private final static String INTER = "Interrupted exception in getNewsJSON method";

    /** Info message field. */
    private final static String EXEC = "Execution exception in getNewsJSON method";

    /** Info message field. */
    private final static String ACCES = "ResourceAccessException in getNewsJSON method";


    /**
     * Method that get JSON of selected News from NEWS API and formed Result JSON
     * @param country selected country News
     * @param category selected category for News
     * @return JSON with News
     */
    @RequestMapping(value = "/newsjson")
    @Cacheable(value = "forecast")
    public String getNewsJSON(@RequestParam List<String> country, @RequestParam List<String> category) {
        countryGlob = country;
        List<Future<String>> json = new ArrayList<>();
        if (country.get(0).isEmpty()) {
            country.set(0, "ua");
            category.set(0, "def");
        }
        try {
            for (int i = 0; i < country.size(); i++) {
                json.add(news.getJSONorig(country.get(i), category.get(i), getValue.getApikey()));
            }
        } catch (ResourceAccessException e) {
            log.warn(ACCES);
        }
//        URL url = this.getClass().getProtectionDomain().getCodeSource().getLocation(); // - for local use
//        File file = new File(url.getPath() + "webresult.docx"); // - for local use

        File currDir = new File("src/main/resources"); // for docker
        String keyPath = currDir.getAbsolutePath() + "/webresult.docx"; //for docker
        File file = new File(keyPath); //for docker

        ArrayList<String> listJSON = null;
        try {
            boolean check = false;
            while (!check) {
                for ( int j = 0; j < json.size(); j ++) {
                    if (json.get(j).isDone()) {
                        check = true;
                    } else {
                        check = false;
                        Thread.sleep(10);
                        break;
                    }
                }
            }
            listJSON = new ArrayList<>();
            for (int i = 0; i < country.size(); i++) {
                listJSON.add(json.get(i).get());
            }
            handlerJSON.parseAndWriteJson(listJSON, file);
        } catch (InterruptedException e) {
            log.warn(INTER);
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            log.warn(EXEC);
        }
        return resultJSON(listJSON, getValue.getQuantityOfNews()).toString();
    }
}
