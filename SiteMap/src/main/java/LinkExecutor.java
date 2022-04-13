import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveTask;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkExecutor extends RecursiveTask<String> {


    private final String url;
    private static final CopyOnWriteArrayList<String> allLinks = new CopyOnWriteArrayList<>();

    public LinkExecutor(@NotNull String url) {
        this.url = url.trim();
    }

    @Override
    protected String compute() {
        String tabulate = StringUtils.repeat("\t",
                url.lastIndexOf("/") != url.length() - 1 ? StringUtils.countMatches(url, "/") - 2
                        : StringUtils.countMatches(url, "/") - 3);
        StringBuilder stringBuilder = new StringBuilder(tabulate + url + "\n");
        List<LinkExecutor> allTasks = new CopyOnWriteArrayList<>();
        Document document;
        Elements elements;
        try {
            Thread.sleep(150);
            document = Jsoup.connect(url).ignoreContentType(true).userAgent("Chrome/99.0.4844.82").get();
            elements = document.select("a[href]");
            for (Element element : elements) {
                String attributeUrl = element.absUrl("href");
                if (!attributeUrl.isEmpty() && attributeUrl.startsWith(url) && !allLinks.contains(attributeUrl) && !attributeUrl
                        .contains("#")) {
                    LinkExecutor linkExecutor = new LinkExecutor(attributeUrl);
                    linkExecutor.fork();
                    allTasks.add(linkExecutor);
                    allLinks.add(attributeUrl);
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        allTasks.sort(Comparator.comparing(o -> o.url));
        for (LinkExecutor link : allTasks) {
            stringBuilder.append(link.join());
        }
        return stringBuilder.toString();
    }
}
