package com.bulavkov.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class BrowserServiceImpl implements BrowserService {

    /**
     * @param url
     * @return
     */
    @Override
    public String browse(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            return document.body().text();
        } catch (Exception e) {
            log.error("Unable to load text from {} ", url, e);
            throw new RuntimeException(e);
        }
    }
}
