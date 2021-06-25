package com.wit.farmo.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

//unused
public class AWSV4Auth {

    private AWSV4Auth() {
    }

    public static class Builder {
        private String awsAccessKey;
        private String awsSecretKey;
        private String path;
        private String region;
        private String service;
        private String httpMethodName;
        private TreeMap<String, String> headers;
        private String payload;

        public Builder(String awsAccessKey, String awsSecretKey) {
            this.awsAccessKey = awsAccessKey;
            this.awsSecretKey = awsSecretKey;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder service(String service) {
            this.service = service;
            return this;
        }

        public Builder httpMethodName(String httpMethodName) {
            this.httpMethodName = httpMethodName;
            return this;
        }

        public Builder headers(TreeMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder payload(String payload) {
            this.payload = payload;
            return this;
        }

        public AWSV4Auth build() {
            return new AWSV4Auth(this);
        }
    }

    private String awsAccessKey;
    private String awsSecretKey;
    private String path;
    private String region;
    private String service;
    private String httpMethodName;
    private TreeMap<String, String> headers;
    private String payload;
    private final String hmacAlgorithm = "AWS4-HMAC-SHA256";
    private final String aws4Request = "aws4_request";
    private String signedHeaders;
    private String xAmzDate;
    private String currentDate;

    private AWSV4Auth(Builder builder) {
        awsAccessKey = builder.awsAccessKey;
        awsSecretKey = builder.awsSecretKey;
        path = builder.path;
        region = builder.region;
        service = builder.service;
        httpMethodName = builder.httpMethodName;
        headers = builder.headers;
        payload = builder.payload;
        xAmzDate = getTimeStamp();
        currentDate = getDate();
    }

    public Map<String, String> getHeaders() {
        headers.put("x-amz-date", xAmzDate);

        // Step 1: CREATE A CANONICAL REQUEST
        String canonicalURL = prepareCanonicalRequest();

        // Step 2: CREATE THE STRING TO SIGN
        String stringToSign = prepareStringToSign(canonicalURL);

        // Step 3: CALCULATE THE SIGNATURE
        String signature = calculateSignature(stringToSign);

        // Step 4: CALCULATE AUTHORIZATION HEADER
        if (signature != null) {
            headers.put("Authorization", buildAuthorizationString(signature));
            return headers;
        } else {
            return null;
        }
    }

    private String prepareCanonicalRequest() {
        StringBuilder canonicalUrl = new StringBuilder();

        canonicalUrl.append(httpMethodName).append("\n");

        canonicalUrl.append(path).append("\n").append("\n");

        StringBuilder signedHeaderBuilder = new StringBuilder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entrySet : headers.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                signedHeaderBuilder.append(key).append(";");
                canonicalUrl.append(key).append(":").append(value).append("\n");
            }
            canonicalUrl.append("\n");
        } else {
            canonicalUrl.append("\n");
        }

        signedHeaders = signedHeaderBuilder.substring(0, signedHeaderBuilder.length() - 1);
        canonicalUrl.append(signedHeaders).append("\n");

        payload = payload == null ? "" : payload;
        canonicalUrl.append(toHex(payload));

        return canonicalUrl.toString();
    }

    private String prepareStringToSign(String canonicalUrl) {
        String stringToSign = "";
        stringToSign = hmacAlgorithm + "\n";
        stringToSign += xAmzDate + "\n";
        stringToSign += currentDate + "/" + region + "/" + service + "/" + aws4Request + "\n";
        stringToSign += toHex(canonicalUrl);
        return stringToSign;
    }

    private String calculateSignature(String stringToSign) {
        try {
            byte[] signatureKey = getSignatureKey(awsSecretKey, currentDate, region, service);
            byte[] signature = hmacSha256(signatureKey, stringToSign);
            return bytesToHex(signature);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private String buildAuthorizationString(String signature) {
        return hmacAlgorithm + " "
                + "Credential=" + awsAccessKey + "/" + getDate() + "/" + region + "/" + service + "/" + aws4Request + ","
                + "SignedHeaders=" + signedHeaders + ","
                + "Signature=" + signature;
    }

    private String toHex(String data) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(data.getBytes("UTF-8"));
            byte[] digest = messageDigest.digest();
            return String.format("%064x", new java.math.BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] hmacSha256(byte[] key, String data) throws Exception {
        String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(data.getBytes("UTF8"));
    }

    private byte[] getSignatureKey(String key, String date, String regionName, String serviceName) throws Exception {
        byte[] kSecret = ("AWS4" + key).getBytes("UTF8");
        byte[] kDate = hmacSha256(kSecret, date);
        byte[] kRegion = hmacSha256(kDate, regionName);
        byte[] kService = hmacSha256(kRegion, serviceName);
        byte[] kSigning = hmacSha256(kService, aws4Request);
        return kSigning;
    }

    private final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    private String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }
}