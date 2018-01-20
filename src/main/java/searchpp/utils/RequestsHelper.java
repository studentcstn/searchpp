package searchpp.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * From the amazon example at https://aws.amazon.com/de/code/product-advertising-api-signed-requests-sample-code-java-rest-query/?tag=code%23keywords%23product-advertising-api
 * Minor changes with abstarct class and subclasses to support ebay request
 */
public abstract class RequestsHelper
{
    /**
     * The charset
     */
    protected final String UTF8_CHARSET = "UTF-8";

    /**
     * Abstract method for subclasses
     * @param params The Map with the parameter
     * @param requestUri The destination uri
     * @return The generated request url
     */
    public abstract String generateRequest(Map<String, String> params, String requestUri);

    /**
     * Canonicalize the query string as required by Amazon.
     *
     * @param sortedParamMap    Parameter name-value pairs in lexicographical order.
     * @return                  Canonical form of query string.
     */
    protected String canonicalize(SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = sortedParamMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            builder.append(percentEncodeRfc3986(kvpair.getKey()));
            builder.append("=");
            builder.append(percentEncodeRfc3986(kvpair.getValue()));
            if (iter.hasNext()) {
                builder.append("&");
            }
        }
        String cannoical = builder.toString();
        return cannoical;
    }

    /**
     * Percent-encode values according the RFC 3986. The built-in Java
     * URLEncoder does not encode according to the RFC, so we make the
     * extra replacements.
     *
     * @param s decoded string
     * @return  encoded string per RFC 3986
     */
    protected String percentEncodeRfc3986(String s) {
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
