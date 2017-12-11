 /**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License.
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */
 package searchpp.utils;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import searchpp.model.config.Api;
import searchpp.model.products.AmazonProduct;

 /**
 * This class contains all the logic for signing requests
 * to the Amazon Product Advertising API.
 */
public class AmazonRequestsHelper extends RequestsHelper
{
    private final String HMAC_SHA256_ALGORITHM = "HmacSHA256";

    private final String REQUEST_METHOD = "GET";

    private String _endpoint;
    private String _awsAccessKeyId;
    private String _awsSecretKey;
    private String _assocTag;

    private SecretKeySpec _secretKeySpec = null;
    private Mac _mac = null;

    public AmazonRequestsHelper(String endpoint)
    {
        _endpoint = endpoint.toLowerCase();
        _awsAccessKeyId = ConfigLoader.getConfig("amazon", Api.accessKey);
        _awsSecretKey = ConfigLoader.getConfig("amazon", Api.secretKey);
        _assocTag = ConfigLoader.getConfig("amazon", Api.clientID);

        try
        {
            byte[] secretKeyBytes = _awsSecretKey.getBytes(UTF8_CHARSET);
            _secretKeySpec = new SecretKeySpec(secretKeyBytes, HMAC_SHA256_ALGORITHM);
            _mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            _mac.init(_secretKeySpec);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public String generateRequest(Map<String, String> params, String requestUri)
    {
        // Let's add the AWSAccessKeyId and Timestamp parameters to the request.
        params.put("AWSAccessKeyId", _awsAccessKeyId);
        params.put("AssociateTag" , _assocTag);
        params.put("Timestamp", timestamp());

        // The parameters need to be processed in lexicographical order, so we'll
        // use a TreeMap implementation for that.
        SortedMap<String, String> sortedParamMap = new TreeMap<>(params);

        // get the canonical form the query string
        String canonicalQS = canonicalize(sortedParamMap);

        // create the string upon which the signature is calculated
        String toSign =
                REQUEST_METHOD + "\n"
                        + _endpoint + "\n"
                        + requestUri + "\n"
                        + canonicalQS;

        // get the signature
        String hmac = hmac(toSign);
        String sig = percentEncodeRfc3986(hmac);

        // construct the URL
        String url =
                "http://" + _endpoint + requestUri + "?" + canonicalQS + "&Signature=" + sig;

        return url;
    }

    /**
     * Compute the HMAC.
     *  
     * @param stringToSign  String to compute the HMAC over.
     * @return              base64-encoded hmac value.
     */
    private String hmac(String stringToSign)
    {
        String signature;
        byte[] data;
        byte[] rawHmac;
        try {
            data = stringToSign.getBytes(UTF8_CHARSET);
            rawHmac = _mac.doFinal(data);
            Base64 encoder = new Base64();
            signature = new String(encoder.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
        }
        return signature;
    }

    /**
     * Generate a ISO-8601 format timestamp as required by Amazon.
     *  
     * @return  ISO-8601 format timestamp.
     */
    private String timestamp()
    {
        String timestamp;
        Calendar cal = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dfm.format(cal.getTime());
        return timestamp;
    }
}
