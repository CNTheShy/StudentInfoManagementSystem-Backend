package com.xust.sims.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public interface PredictionService {

    String RandomForestClient(String[] data) throws Exception;

}
