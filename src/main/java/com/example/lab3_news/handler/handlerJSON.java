package com.example.lab3_news.handler;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import static com.example.lab3_news.Lab3NewsApplication.log;
import static com.example.lab3_news.controllers.NewsController.countryGlob;


/**
 * Class for working with JSON
 */
@EnableAsync
public class handlerJSON {

    /** Info message field. */
    private static String infoMessage;

    /** Info message field. */
    final static String CONNECTION = "INFO MESSAGE: Some problem with news API connection";

    /** Info message field. */
    final static String FILE = "INFO MESSAGE: Some problem with .docx";

    /** Info message field. */
    final static String INDOC = "INFO MESSAGE: SOME PROBLEM WITH RESOURCE";

    /**
     * Method for parsing JSON and write to .docx file
     * @param resultJson JSON formed from few selected news category
     * @param file file for write
     */
    public static void parseAndWriteJson(ArrayList<String> resultJson, File file) {

        XWPFDocument document = new XWPFDocument();
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();

        FileOutputStream fout;
        try {
            fout = new FileOutputStream(file.getPath());
            for (int j = 0; j < resultJson.size(); j++) {
                JSONObject newsJsonObject = new JSONObject(resultJson.get(j));
                JSONArray articlesArray = (JSONArray) newsJsonObject.get("articles");
                for (int i = 0; i < articlesArray.length(); i++) {
                    try {
                        JSONObject currentAtrticle = (JSONObject) articlesArray.get(i);
                        int imageType = XWPFDocument.PICTURE_TYPE_JPEG;
                        int width = 200;
                        int height = 250;
                        String imageFileName = "";
                        run.setText(currentAtrticle.get("title").toString());
                        run.addBreak();
                        URL imageURL = new URL(currentAtrticle.get("urlToImage").toString());
                        InputStream is = imageURL.openStream();
                        run.addPicture(is, imageType, imageFileName, Units.toEMU(width), Units.toEMU(height));
                        run.addBreak();
                        is.close();
                        run.setText(currentAtrticle.get("description").toString());
                        run.addBreak();
                        run.setText(currentAtrticle.get("author").toString());
                        run.addBreak();
                        run.setText(currentAtrticle.get("url").toString());
                        run.addBreak();
                        run.setText("---------------------------------------------------------------------");
                        run.addBreak();
                    } catch (Exception e) {
                        run.setText(INDOC);
                        infoMessage = FILE;
                        log.warn(infoMessage);
                    }
                }
            }
            document.write(fout);
            document.close();
            fout.close();
        } catch (IOException e) {
            infoMessage = FILE;
            log.warn(infoMessage);
        }
    }

    /**
     *Method that create result JSON from accumulated requested JSON's
     * @param listJSON List of JSON for accumulation
     * @return result JSON Object
     */
    public static JSONObject resultJSON (ArrayList<String> listJSON, String quantityOfNews) {
        int countryCalc = 0;
        JSONObject jsonResult = new JSONObject();
        for (String jsonItem : listJSON) {
            if (jsonItem != null) {
                JSONObject jsonResource = new JSONObject(jsonItem);
                JSONArray articlesArray = (JSONArray) jsonResource.get("articles");
                jsonResult.accumulate("articles", articlesArray);
            } else {
                if (!countryGlob.get(countryCalc).equals("def") ) {
                    infoMessage = CONNECTION;
                }
            }
            ++countryCalc;
        }
        jsonResult.put("infoMessage", infoMessage);
        jsonResult.put("quantityOfNews", quantityOfNews);
        log.warn(infoMessage);
        infoMessage = "";
        return jsonResult;
    }

}
