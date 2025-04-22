package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    List<String> extractWikipediaLinks(String htmlContent) {
        List<String> wikipediaUrls = new ArrayList<>();
        String regex = "href=\\\"(/wiki/[^\\\"]+)\\\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlContent);

        while (matcher.find()) {
            wikipediaUrls.add(matcher.group(1));
        }

        return wikipediaUrls;
    }
    Map<Integer, String> crawl(int limit) {
        int currentDocID = 0;
        Map<Integer, String> MapOfDocuments= new Hashtable<Integer, String>();
        List<String> urls = new LinkedList<>();
        urls.add("https://en.wikipedia.org/wiki/Pharaoh");
        urls.add("https://en.wikipedia.org/wiki/List_of_pharaohs");

        while(MapOfDocuments.size() != limit && currentDocID < urls.size()) {
            System.out.println("CRAWLING PAGE NUMBER {"+currentDocID+"}...");
            String url = urls.get(currentDocID);
            String content = null;
            URLConnection connection = null;
            try {
                connection =  new URL(url).openConnection();
                Scanner scanner = new Scanner(connection.getInputStream());
                scanner.useDelimiter("\\Z");
                content = scanner.next();
                scanner.close();
            }catch ( Exception ex ) {
                ex.printStackTrace();
            }


            Document doc = Jsoup.parse(content);
            String text = doc.text();

            System.out.println("FINISHED CRAWLING PAGE NUMBER {"+currentDocID+"}...");
            MapOfDocuments.put(currentDocID++, text);

            List<String> wikiLinks = extractWikipediaLinks(content);
            for (String s :
                    wikiLinks) {
                if(s.contains(":") || s.contains("Main_Page") || s.contains("Help")){
                    continue;
                }
                s = "https://en.wikipedia.org"+s;
                if(!urls.contains(s)) {
                    urls.add(s);
                }
            }
        }


        return MapOfDocuments;
    }
}
