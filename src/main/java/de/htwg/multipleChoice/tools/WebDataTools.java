package de.htwg.multipleChoice.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@ApplicationScoped
public class WebDataTools {


    @Tool("Returns the content of a web page, given the URL")
    public String getWebPageContent(@P("URL of the page") String url) {
        try {
            Document jsoupDocument = Jsoup.connect(url).get();
            return jsoupDocument.body().text();
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }
}
