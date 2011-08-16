package hum.server.adapter.janrain;

import hum.client.adapter.JanrainWrapper;
import hum.server.CurrentUser;
import static hum.server.ServerUtils.SERVER_UTILS;
import hum.server.adapter.janrain.json.AuthInfoResponse;
import hum.server.services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;


@Singleton
public class JanrainCallbackServlet extends HttpServlet {
    public static final String JANRAIN_API_URL = "https://rpxnow.com/api/v2/auth_info";
    public static final String JANRAIN_API_KEY = "01e9cac0599a6d3edbfe25d702228fbb350db608";

    private static final Logger log = Logger.getLogger(JanrainCallbackServlet.class.getName());

    @Inject
    private Gson gson;

    @Inject
    private Provider<CurrentUser> currentUserProvider;

    @Inject
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        int pos = path.indexOf(JanrainWrapper.JANRAIN_CALLBACK);
        if (pos < 1) {
            String logMessage = "illegal servlet invocation";
            authError(req, resp, logMessage, null, logMessage);
            return;
        } else {
            path = path.substring(0, pos + 1).replace("//", "/");
        }

        String token = req.getParameter("token");
        String mapPrUrl = SERVER_UTILS.getUrl(req, path, null, null);

        log.info("got janrain token: " + token);

        StringBuilder url = new StringBuilder(JANRAIN_API_URL)
                .append("?apiKey=").append(JANRAIN_API_KEY)
                .append("&token=").append(token)
                .append("&format=json");

        String payload = fetchContent(url.toString());

        if (payload == null || payload.isEmpty()) {
            String logMessage = "Connection to JanRain failed";
            authError(req, resp, logMessage, mapPrUrl, logMessage);
            return;
        }

        AuthInfoResponse response = gson.fromJson(payload, AuthInfoResponse.class);

        if (!"ok".equals(response.stat)) {
            String logMessage = new StringBuilder("Janrain authentication failed with code ")
                    .append(response.err.code)
                    .append(": ")
                    .append(response.err.msg)
                    .toString();
            authError(req, resp, logMessage, mapPrUrl, logMessage);
            return;
        }

        currentUserProvider.get().userId =  userService.save(response.profile).id;

        resp.sendRedirect(mapPrUrl);
    }

    private void authError(
            HttpServletRequest req,
            HttpServletResponse resp,
            String logMessage,
            String mapPrUrl,
            String userMessage
    ) throws ServletException, IOException {
        log.log(Level.WARNING, logMessage);
        req.setAttribute("error", userMessage);
        req.setAttribute("back_url", mapPrUrl == null ? "" : mapPrUrl);
        req.getRequestDispatcher("/auth_error.jsp").forward(req, resp);
    }


    private String fetchContent(String u) {
        BufferedReader reader = null;
        try {
            URL url = new URL(u);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            return result.toString();
        } catch (MalformedURLException e) {
            log.log(Level.WARNING, "failed to fetch URL " + u, e);
        } catch (IOException e) {
            log.log(Level.WARNING, "failed to fetch URL " + u, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                    // do nothing
                }
            }
        }
        return null;
    }

}
