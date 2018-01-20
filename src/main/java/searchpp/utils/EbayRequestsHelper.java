package searchpp.utils;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * From the amazon example at https://aws.amazon.com/de/code/product-advertising-api-signed-requests-sample-code-java-rest-query/?tag=code%23keywords%23product-advertising-api
 * Minor changes to support ebay request
 */

public class EbayRequestsHelper extends RequestsHelper
{
    private String _endpoint;
    private String _clientId;

    public EbayRequestsHelper(String endpoint)
    {
        _endpoint = endpoint.toLowerCase();
        //_clientId = ConfigLoader.getConfig("ebay", Api.clientID);
    }

    @Override
    public String generateRequest(Map<String, String> params, String requestUri)
    {
        //params.put("SECURITY-APPNAME", _clientId);
        SortedMap<String, String> sortedParamMap = new TreeMap<>(params);

        String request = canonicalize(sortedParamMap);

        String url = "http://" + _endpoint + requestUri + "?" + request;

        return url;
    }
}
