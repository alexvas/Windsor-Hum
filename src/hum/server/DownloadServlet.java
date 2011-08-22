package hum.server;

import static hum.client.Utils.UTILS;
import hum.server.model.Hum;
import hum.server.services.HumService;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class DownloadServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(DownloadServlet.class.getName());

    @Inject
    private HumService humService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/csv");
        SimpleDateFormat dailyFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat exportFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DecimalFormat numberFormat = new DecimalFormat("###.######", new DecimalFormatSymbols(Locale.CANADA));
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + "windsorhum_" + dailyFormat.format(new Date()) + ".csv\"");

        OutputStream out = null;
        try {
            out = resp.getOutputStream();
            out.write(UTILS.join(",", "When", "Where", "Level", "Country", "Province", "Postcode").getBytes("UTF-8"));
            byte[] eos = "\n".getBytes("UTF-8");
            out.write(eos);
            for (Hum hum : humService.all()) {
                Hum.Point point = hum.getPoint();
                String latLng = quote(numberFormat.format(point.getLat()) + "," + numberFormat.format(point.getLng()));
                Hum.Address address = hum.getAddress();
                if (address.getCountry() == null || address.getRegion() == null || address.getPostcode() == null) {
                    continue;
                }
                String country = quote(address.getCountry());
                String region = quote(address.getRegion());
                String code = quote(address.getPostcode());
                if (hum.getLevel() == null) {
                    continue;
                }
                String level = Integer.toString(hum.getLevel().amount);
                if (hum.getStart() == null) {
                    continue;
                }
                String start = quote(exportFormat.format(hum.getStart()));
                out.write(UTILS.join(",", start, latLng, level, country, region, code).getBytes("UTF-8"));
                out.write(eos);
            }
            out.flush();
        } catch (IOException e) {
            log.log(Level.WARNING, "error writing csv", e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    private String quote(String in) {
        if (in == null) {
            return "";
        }
        boolean needQuoted = false;
        if (in.contains("\"")) {
            needQuoted = true;
            in = in.replace("\"", "\\\"");
        }
        if (in.contains(",")) {
            needQuoted = true;
        }
        return needQuoted
                ? '"' + in + '"'
                : in;
    }

}
