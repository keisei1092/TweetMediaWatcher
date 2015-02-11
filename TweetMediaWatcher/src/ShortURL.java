import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ShortURL {

    private static final Set<URL> services;
    static {
        try {
            Set<URL> tmp = new HashSet<URL>();
            tmp.add(new URL("http://t.co/"));
            tmp.add(new URL("http://bit.ly/"));
            tmp.add(new URL("http://goo.gl/"));
            services = Collections.unmodifiableSet(tmp);
        } catch (MalformedURLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static URL expand(URL shortUrl) throws IOException {
        boolean isTarget = false;
        for (URL service : services) {
            if (service.getProtocol().equals(shortUrl.getProtocol())
                    && service.getHost().equals(shortUrl.getHost())) {
                isTarget = true;
                break;
            }
        }
        if (!isTarget) {
            return shortUrl;
        }

        HttpURLConnection conn = (HttpURLConnection) shortUrl.openConnection();
        try {
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("HEAD");
            conn.setDoOutput(false);
            int statusCode = conn.getResponseCode();
            if (statusCode == 301) {
                String tmpUrlStr = conn.getHeaderField("Location");
                return new URL(tmpUrlStr);
            } else {
                return shortUrl;
            }
        } finally {
            conn.disconnect();
        }
    }
}