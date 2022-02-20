package com.example.lab3_news.services;

import java.util.concurrent.Future;


public interface NewsServices {

    Future<String> getJSONorig(String country, String categoru);

}
