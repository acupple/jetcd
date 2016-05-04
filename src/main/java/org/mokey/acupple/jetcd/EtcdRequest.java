package org.mokey.acupple.jetcd;

import org.mokey.acupple.jetcd.utils.Strings;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by enousei on 4/21/16.
 */
public class EtcdRequest {
    private final static String URI_STUB = "/v2/keys/";

    private final static String UTF_8 = StandardCharsets.UTF_8.displayName();

    private boolean wait;
    private long waitIndex;
    private boolean recursive;
    private boolean sorted;
    private String key;
    private long ttl;
    private boolean dir;

    private boolean consistent;

    private String value;
    private String method = "GET";

    private String host;
    private int port;

    private boolean prevExist;
    private String prevValue;
    private long prevIndex;
    private boolean emptyTTL;

    public static EtcdRequest request() {
        return new EtcdRequest();
    }

    public boolean isWait() {
        return wait;
    }

    public EtcdRequest wait(boolean wait) {
        this.wait = wait;
        return this;
    }

    public long waitIndex() {
        return waitIndex;
    }

    public EtcdRequest waitIndex(long waitIndex) {
        this.waitIndex = waitIndex;
        return this;
    }

    public boolean recursive() {
        return recursive;
    }

    public EtcdRequest recursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    public boolean consistent() {
        return consistent;
    }

    public EtcdRequest consistent(boolean consistent) {
        this.consistent = consistent;
        return this;
    }

    public boolean sorted() {
        return sorted;
    }

    public EtcdRequest sorted(boolean sorted) {
        this.sorted = sorted;
        return this;
    }

    public String key() {
        return key;
    }

    public EtcdRequest key(String key) {
        this.key = key;
        return this;
    }

    public long ttl() {
        return ttl;
    }

    public EtcdRequest ttl(long ttl) {
        this.ttl = ttl;
        return this;
    }

    public long prevIndex() {
        return prevIndex;
    }

    public EtcdRequest prevIndex(long prevIndex) {
        this.prevIndex = prevIndex;
        return this;
    }

    public boolean prevExist() {
        return prevExist;
    }

    public EtcdRequest prevExist(boolean prevExist) {
        this.prevExist = prevExist;
        return this;
    }

    public boolean dir() {
        return dir;
    }

    public EtcdRequest dir(boolean dir) {
        this.dir = dir;
        return this;
    }

    public String value() {
        return value;
    }

    public EtcdRequest value(String value) {
        this.value = value;
        return this;
    }

    public String prevValue() {
        return prevValue;
    }

    public EtcdRequest prevValue(String prevValue) {
        this.prevValue = prevValue;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public EtcdRequest methodGET() {
        this.method = "GET";
        return this;
    }

    public EtcdRequest methodPUT() {
        this.method = "PUT";
        return this;
    }

    public EtcdRequest methodPOST() {
        this.method = "POST";
        return this;
    }

    public EtcdRequest methodDELETE() {
        this.method = "DELETE";
        return this;
    }

    public String uri() {

        StringBuilder builder = new StringBuilder(80);
        builder.append(URI_STUB).append(key);

        if (this.method.equals("GET") || this.method.equals("DELETE")) {

            builder.append("?");

            paramBody(builder);
        }

        return builder.toString();
    }

    public String paramBody() {
        StringBuilder builder = new StringBuilder(80);
        paramBody(builder);
        return builder.toString();
    }

    private void paramBody(StringBuilder builder) {

        boolean first = true;

        if (!Strings.isNullOrEmpty(prevValue)) {
            builder.append("prevValue=").append(encode(prevValue));
            first = false;
        }

        if (!Strings.isNullOrEmpty(value)) {
            if (!first)
                builder.append("&");
            builder.append("value=").append(encode(value));
            first = false;
        }

        if (ttl > 0) {
            if (!first)
                builder.append("&");
            builder.append("ttl=").append(ttl);
            first = false;
        }

        if (waitIndex > 0) {
            if (!first)
                builder.append("&");
            builder.append("waitIndex=").append(waitIndex);
            first = false;
        }

        if (prevIndex > 0) {
            if (!first)
                builder.append("&");
            builder.append("prevIndex=").append(prevIndex);
            first = false;
        }

        if (wait) {
            if (!first)
                builder.append("&");
            builder.append("wait=true");
            first = false;
        }

        if (recursive) {
            if (!first)
                builder.append("&");
            builder.append("recursive=true");
            first = false;
        }

        if (sorted) {
            if (!first)
                builder.append("&");
            builder.append("sorted=true");
            first = false;
        }

        if (emptyTTL) {
            if (!first)
                builder.append("&");
            builder.append("ttl=");
            first = false;
        }

        if (prevExist) {
            if (!first)
                builder.append("&");
            builder.append("prevExist=true");
            first = false;
        }

        if (dir) {
            if (!first)
                builder.append("&");
            builder.append("dir=true");
            first = false;
        }

        if (consistent) {
            if (!first)
                builder.append("&");
            builder.append("consistent=true");
            first = false;
        }

    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, UTF_8);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public String host() {
        return host;
    }

    public EtcdRequest host(String host) {
        this.host = host;
        return this;
    }

    public int port() {
        return port;
    }

    public EtcdRequest port(int port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(80);
        if (method.equals("GET")) {
            sb.append("http://").append(host).append(":").append(port)
                    .append(uri());
        } else {
            sb.append("http://").append(host).append(":").append(port)
                    .append("::").append(method).append(uri())
                    .append("\nREQUEST_BODY\n\t").append(paramBody());
        }
        return sb.toString();
    }

    public EtcdRequest emptyTTL() {
        this.emptyTTL = true;

        return this;
    }
}
