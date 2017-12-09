package searchpp.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

//Nur vorübergehend, wird evtl. noch mit AmazonSignedRequestsHelper zusammengefasst
public class EbayRequestsHelper
{
    private static final String REQUEST_URI = "/services/search/FindingService/v1";
    private static final String REQUEST_METHOD = "GET";
    private static final String UTF8_CHARSET = "UTF-8";
    private String endpoint = null;

    public static EbayRequestsHelper getInstance(
            String endpoint
    ) throws IllegalArgumentException
    {
        if (null == endpoint || endpoint.length() == 0)
        { throw new IllegalArgumentException("endpoint is null or empty"); }

        EbayRequestsHelper instance = new EbayRequestsHelper();
        instance.endpoint = endpoint.toLowerCase();

        return instance;
    }

    public String signEbay(Map<String, String> params) {

        String request = this.canonicalize(params);

        String url = "http://" + this.endpoint + REQUEST_URI + "?" + request;

        return url;
    }

    private String canonicalize(Map<String, String> paramsMap) {
        if (paramsMap.isEmpty()) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter = paramsMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            buffer.append(percentEncodeRfc3986(kvpair.getKey()));
            buffer.append("=");
            buffer.append(percentEncodeRfc3986(kvpair.getValue()));
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        String cannoical = buffer.toString();
        return cannoical;
    }

    private String percentEncodeRfc3986(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, UTF8_CHARSET)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }
}
