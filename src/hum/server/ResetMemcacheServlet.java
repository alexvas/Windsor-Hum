package hum.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.inject.Singleton;


@Singleton
public class ResetMemcacheServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(ResetMemcacheServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        MemcacheServiceFactory.getMemcacheService().clearAll();
        log.warning("memcache reset");
        resp.sendRedirect("/");
    }
}
