package org.mokey.acupple.jetcd;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.mokey.acupple.jetcd.utils.InternalLog;
import org.mokey.acupple.jetcd.utils.Strings;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by enousei on 4/21/16.
 */
public class EtcdClient implements Etcd{
    private final OkHttpClient client = new OkHttpClient();

    private URI[] hosts;
    private Random rand = new Random(System.currentTimeMillis());
    private AtomicInteger currentIndex;

    public EtcdClient(URI[] hosts) {
        this.hosts = hosts;
        if(hosts == null || hosts.length <= 0){
            InternalLog.error("The etcd hosts are empty, pls check");
        }
        this.currentIndex = new AtomicInteger(rand.nextInt(this.hosts.length));
        InternalLog.log(String.format("connecting to: %s",
                this.hosts[this.currentIndex.get()].toString()));
    }

    private URI loadBalance() {
        int index = this.currentIndex.get();

        InternalLog.log(String.format("host %s is dead, will rebalance", this.hosts[index]));

        int oldIndex = index;

        if (index + 1 == hosts.length) {
            index = 0;
        } else {
            index++;
        }

        if (this.currentIndex.compareAndSet(oldIndex, index)) {
            InternalLog.log(String.format("rebalance ok, reconnecting to: %s",
                    this.hosts[index]));
            return hosts[index];
        }

        return hosts[this.currentIndex.get()];
    }

    @Override
    public EtcdResponse delete(String key) {

        return request(EtcdRequest.request().methodDELETE().key(key));

    }

    @Override
    public void delete(Handler<EtcdResponse> responseHandler, String key) {

        request(responseHandler, EtcdRequest.request().methodDELETE().key(key));
    }

    @Override
    public EtcdResponse deleteDir(String key) {

        return request(EtcdRequest.request().methodDELETE().key(key).dir(true));

    }

    @Override
    public void deleteDir(Handler<EtcdResponse> responseHandler, String key) {

        request(responseHandler,
                EtcdRequest.request().methodDELETE().key(key).dir(true));

    }

    @Override
    public EtcdResponse deleteDirRecursively(String key) {

        return request(EtcdRequest.request().methodDELETE().key(key).dir(true)
                .recursive(true));

    }

    @Override
    public void deleteDirRecursively(Handler<EtcdResponse> responseHandler,
                                     String key) {
        request(responseHandler,
                EtcdRequest.request().methodDELETE().key(key).dir(true)
                        .recursive(true));

    }

    @Override
    public EtcdResponse deleteIfAtIndex(String key, long index) {

        return request(EtcdRequest.request().methodDELETE().key(key)
                .prevIndex(index));

    }

    @Override
    public void deleteIfAtIndex(Handler<EtcdResponse> responseHandler, String key,
                                long index) {

        request(responseHandler, EtcdRequest.request().methodDELETE().key(key)
                .prevIndex(index));

    }

    @Override
    public EtcdResponse deleteIfValue(String key, String prevValue) {

        return request(EtcdRequest.request().methodDELETE().key(key)
                .prevValue(prevValue));

    }

    @Override
    public void deleteIfValue(Handler<EtcdResponse> responseHandler, String key,
                              String prevValue) {

        request(responseHandler, EtcdRequest.request().methodDELETE().key(key)
                .prevValue(prevValue));

    }

    @Override
    public void createDir(Handler<EtcdResponse> responseHandler, String key) {

        request(responseHandler,
                EtcdRequest.request().methodPUT().key(key).dir(true));

    }

    @Override
    public EtcdResponse createDir(String key) {

        return request(EtcdRequest.request().methodPUT().key(key).dir(true));

    }

    @Override
    public EtcdResponse createTempDir(String key, long ttl) {

        return request(EtcdRequest.request().methodPUT().key(key).ttl(ttl)
                .dir(true));

    }

    @Override
    public void createTempDir(Handler<EtcdResponse> responseHandler, String key,
                              long ttl) {

        request(responseHandler, EtcdRequest.request().methodPUT().key(key)
                .ttl(ttl).dir(true));

    }

    @Override
    public EtcdResponse updateDirTTL(String key, long ttl) {

        return request(EtcdRequest.request().methodPUT().key(key).ttl(ttl)
                .dir(true).prevExist(true));

    }

    @Override
    public void updateDirTTL(Handler<EtcdResponse> responseHandler, String name,
                             long ttl) {

        request(responseHandler,
                EtcdRequest.request().methodPUT().key(name).ttl(ttl).dir(true)
                        .prevExist(true));

    }

    @Override
    public EtcdResponse list(String key) {
        return get(key);
    }

    @Override
    public void list(Handler<EtcdResponse> responseHandler, String key) {

        get(responseHandler, key);
    }

    @Override
    public EtcdResponse listRecursive(String key) {

        return request(EtcdRequest.request().key(key).recursive(true));

    }

    @Override
    public void listRecursive(Handler<EtcdResponse> responseHandler, String key) {

        request(responseHandler, EtcdRequest.request().key(key).recursive(true));

    }

    @Override
    public EtcdResponse listSorted(String key) {
        return request(EtcdRequest.request().key(key).recursive(true).sorted(true));

    }

    @Override
    public void listSorted(Handler<EtcdResponse> responseHandler, String key) {
        request(responseHandler, EtcdRequest.request().key(key).recursive(true)
                .sorted(true));

    }

    @Override
    public EtcdResponse addToDir(String dirName, String key, String value) {

        return request(EtcdRequest.request().methodPOST()
                .key(Strings.add(dirName, "/", key)).value(value));
    }

    @Override
    public void addToDir(Handler<EtcdResponse> responseHandler, String dirName,
                         String key, String value) {
        request(responseHandler,
                EtcdRequest.request().methodPOST().key(Strings.add(dirName, "/", key))
                        .value(value));
    }

    public EtcdResponse set(String key, String value) {

        return request(EtcdRequest.request().methodPUT().key(Strings.add(key))
                .value(value));

    }

    @Override
    public void set(Handler<EtcdResponse> responseHandler, String key, String value) {

        request(responseHandler, EtcdRequest.request().methodPUT()
                .key(Strings.add(key)).value(value));

    }

    @Override
    public EtcdResponse setIfExists(String key, String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevExist(true);

        return request(etcdRequest);

    }

    @Override
    public void setIfExists(Handler<EtcdResponse> responseHandler, String key,
                            String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevExist(true);

        request(responseHandler, etcdRequest);

    }

    @Override
    public EtcdResponse setIfNotExists(String key, String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevExist(false);

        return request(etcdRequest);

    }

    @Override
    public void setIfNotExists(Handler<EtcdResponse> responseHandler, String key,
                               String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevExist(false);

        request(responseHandler, etcdRequest);

    }

    @Override
    public EtcdResponse compareAndSwapByValue(String key, String prevValue,
                                              String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevValue(prevValue);

        return request(etcdRequest);
    }

    @Override
    public void compareAndSwapByValue(Handler<EtcdResponse> responseHandler,
                                      String key, String prevValue, String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevValue(prevValue);

        request(responseHandler, etcdRequest);

    }

    @Override
    public EtcdResponse compareAndSwapByModifiedIndex(String key, long prevIndex,
                                                      String value) {
        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevIndex(prevIndex);

        return request(etcdRequest);

    }

    @Override
    public void compareAndSwapByModifiedIndex(
            Handler<EtcdResponse> responseHandler, String key, long prevIndex,
            String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .prevIndex(prevIndex);
        request(responseHandler, etcdRequest);
    }

    @Override
    public EtcdResponse setTemp(String key, String value, int ttl) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .ttl(ttl);
        return request(etcdRequest);

    }

    @Override
    public void setTemp(Handler<EtcdResponse> responseHandler, String key,
                        String value, int ttl) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .ttl(ttl);

        request(responseHandler, etcdRequest);
    }

    @Override
    public EtcdResponse removeTTL(String key, String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .emptyTTL().prevExist(true);
        return request(etcdRequest);
    }

    @Override
    public void removeTTL(Handler<EtcdResponse> responseHandler, String key,
                          String value) {

        EtcdRequest etcdRequest = EtcdRequest.request().methodPUT().key(key).value(value)
                .emptyTTL().prevExist(true);
        request(responseHandler, etcdRequest);

    }

    @Override
    public EtcdResponse get(String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key);
        return request(etcdRequest);

    }

    @Override
    public void get(Handler<EtcdResponse> responseHandler, String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key);
        request(responseHandler, etcdRequest);

    }

    @Override
    public EtcdResponse getConsistent(String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).consistent(true);
        return request(etcdRequest);

    }

    @Override
    public void getConsistent(Handler<EtcdResponse> responseHandler, String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).consistent(true);

        request(responseHandler, etcdRequest);

    }

    @Override
    public EtcdResponse wait(String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true);

        return requestForever(etcdRequest);

    }

    @Override
    public void wait(Handler<EtcdResponse> responseHandler, String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true);

        request(responseHandler, etcdRequest);
    }

    @Override
    public EtcdResponse wait(String key, long index) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true)
                .waitIndex(index);

        return requestForever(etcdRequest);

    }

    @Override
    public void wait(Handler<EtcdResponse> responseHandler, String key, long index) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true)
                .waitIndex(index);

        request(responseHandler, etcdRequest);

    }

    @Override
    public EtcdResponse waitRecursive(String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true).recursive(true);

        return requestForever(etcdRequest);

    }

    @Override
    public void waitRecursive(Handler<EtcdResponse> responseHandler, String key) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true).recursive(true);
        request(responseHandler, etcdRequest);
    }

    @Override
    public EtcdResponse waitRecursive(String key, long index) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true).recursive(true)
                .waitIndex(index);

        return requestForever(etcdRequest);

    }

    @Override
    public void waitRecursive(Handler<EtcdResponse> responseHandler, String key,
                              long index) {

        EtcdRequest etcdRequest = EtcdRequest.request().key(key).wait(true).recursive(true)
                .waitIndex(index);

        request(responseHandler, etcdRequest);
    }

    @Override
    public void request(Handler<EtcdResponse> responseHandler, EtcdRequest etcdRequest) {

        URI host = hosts[this.currentIndex.get()];

        etcdRequest.host(host.getHost()).port(host.getPort());

        sendHttpRequest(etcdRequest, responseHandler);
    }

    @Override
    public EtcdResponse request(final EtcdRequest etcdRequest) {

        final BlockingQueue<EtcdResponse> etcdResponseBlockingQueue = new ArrayBlockingQueue<>(
                1);

        request(new Handler<EtcdResponse>() {
            @Override
            public void handle(EtcdResponse event) {
                etcdResponseBlockingQueue.offer(event);
            }
        }, etcdRequest);

        return getResponse(etcdRequest.key(), etcdResponseBlockingQueue);
    }

    public EtcdResponse requestForever(final EtcdRequest etcdRequest) {

        final BlockingQueue<EtcdResponse> etcdResponseBlockingQueue = new ArrayBlockingQueue<>(
                1);

        request(new Handler<EtcdResponse>() {
            @Override
            public void handle(EtcdResponse event) {
                etcdResponseBlockingQueue.offer(event);
            }
        }, etcdRequest);

        return getResponseWaitForever(etcdRequest.key(), etcdResponseBlockingQueue);
    }

    /**
     * This actually sends the etcdRequest.
     *
     * @param etcdRequest
     *            etcdRequest
     * @param responseHandler
     *            handler
     */
    private void sendHttpRequest(final EtcdRequest etcdRequest,
                                 final Handler<EtcdResponse> responseHandler) {
        URI host = hosts[this.currentIndex.get()];
        Request.Builder requestBuilder  =
                new Request.Builder().url(host.toString() + etcdRequest.uri());

        if(etcdRequest.getMethod().equalsIgnoreCase("GET")){
            requestBuilder.get();
        }else if(etcdRequest.getMethod().equalsIgnoreCase("PUT")){
            requestBuilder.put(RequestBody.create(
                    MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),
                    etcdRequest.paramBody()));
        }else if(etcdRequest.getMethod().equalsIgnoreCase("POST")){
            requestBuilder.post(RequestBody.create(
                    MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"),
                    etcdRequest.paramBody()));
        }else if(etcdRequest.getMethod().equalsIgnoreCase("DELETE")){
            requestBuilder.delete();
        }

        client.newCall(requestBuilder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException ex) {
                if (ex instanceof SocketTimeoutException) {
                    // 10秒钟没有从远程获取到数据，Do Noting
                } else if (ex instanceof ConnectException) {
                    //考虑如下情形:
                    //1. 准备连ETCD的一个实例，但是该实例已经挂了，这时候，会抛出ConnectException，需要Rebalance
                    //2. 已经连上ETCD的一个实例，在waitRecursive的时候，该实例挂了，会抛出ConnectionClosedException， 需要Rebalance
                    loadBalance();
                }
                EtcdResponse etcdResponse = createResponseFromException(
                        etcdRequest, ex);
                responseHandler.handle(etcdResponse);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String re;
                try {
                    if(!response.isSuccessful()){
                        throw new IOException("Unexpected code " + response);
                    }

                    re = response.body().string();
                    EtcdResponse etcdResponse = parseResponse(re, etcdRequest,
                            responseHandler, response);

                    if (etcdResponse instanceof RedirectEtcdResponse) {
                        // do nothing
                    } else {
                        responseHandler.handle(etcdResponse);
                    }
                } catch (IOException e) {
                    EtcdResponse etcdResponse = createResponseFromException(
                            etcdRequest, e);
                    responseHandler.handle(etcdResponse);
                }finally {
                    response.body().close();
                }
            }
        });
    }

    private EtcdResponse getResponse(String key,
                                     BlockingQueue<EtcdResponse> etcdResponseBlockingQueue) {
        try {
            EtcdResponse etcdResponse = etcdResponseBlockingQueue.poll(5000,
                    TimeUnit.MILLISECONDS);
            if (etcdResponse == null) {
                throw new EtcdException(Strings.add("EtcdResponse timeout for get request key=", key));
            }

            return etcdResponse;
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        return null;
    }

    private EtcdResponse getResponseWaitForever(String key,
                                                BlockingQueue<EtcdResponse> etcdResponseBlockingQueue) {
        try {
            EtcdResponse etcdResponse = etcdResponseBlockingQueue.take();
            if (etcdResponse == null) {
                throw new EtcdException("EtcdResponse timeout for get request key="
                        + key);
            }

            return etcdResponse;
        } catch (InterruptedException e) {
            Thread.interrupted();
        }

        return null;
    }

    private EtcdResponse createResponseFromException(EtcdRequest etcdRequest,
                                                     Throwable throwable) {

        EtcdResponse resp = new EtcdResponse();
        if (throwable instanceof ConnectException) {

            Error error = new Error();
            error.setErrorCode(-1);
            error.setCause(throwable.getClass().getName());
            error.setMessage(Strings.add("Unable to connect", etcdRequest.toString()));
            error.setIndex(0L);

            resp.setAction(etcdRequest.toString());
            resp.setResponseCode(-1);
            resp.setError(error);

        } else {
            Error error = new Error();
            error.setErrorCode(-1);
            error.setCause(throwable.getClass().getName());
            error.setMessage(Strings.add(throwable.getMessage(),
                    " Unable to connect to ", etcdRequest.toString(), " key ",
                    etcdRequest.key()));
            error.setIndex(0L);

            resp.setAction(etcdRequest.toString());
            resp.setResponseCode(-1);
            resp.setError(error);
        }
        return resp;
    }

    private EtcdResponse parseResponse(String json, EtcdRequest etcdRequest,
                                       Handler<EtcdResponse> handler, Response httpResponse) {
        try {

            EtcdResponse etcdResponse = null;
            switch (httpResponse.code()) {

                case 307:
                    etcdResponse = new RedirectEtcdResponse();
                    if (httpResponse.headers().get("Location") != null) {
                        ((RedirectEtcdResponse) etcdResponse).setLocation(URI
                                .create(httpResponse.headers().get("Location")));
                    }

                    Etcd client = new EtcdClient(new URI[]{((RedirectEtcdResponse) etcdResponse).getLocation()});
                    client.request(handler, etcdRequest);

                    return etcdResponse;

                case 200:
                    etcdResponse = JSON.parseObject(json, EtcdResponse.class);
                    etcdResponse.setResponseCode(httpResponse.code());
                    return etcdResponse;

                case 201:
                    etcdResponse = JSON.parseObject(json, EtcdResponse.class);
                    etcdResponse.setResponseCode(httpResponse.code());
                    etcdResponse.setCreated(true);
                    return etcdResponse;

                case 404:

                    Error notFound = JSON.parseObject(json, Error.class);

                    etcdResponse = new EtcdResponse();
                    etcdResponse.setAction(etcdRequest.toString());
                    etcdResponse.setResponseCode(httpResponse.code());
                    etcdResponse.setError(notFound);

                    return etcdResponse;

                default:

                    if (!Strings.isNullOrEmpty(json)
                            && (json.contains("cause") || json
                            .contains("errorCode"))) {

                        Error error = JSON.parseObject(json, Error.class);

                        etcdResponse = new EtcdResponse();
                        etcdResponse.setAction(etcdRequest.toString());
                        etcdResponse.setResponseCode(httpResponse.code());
                        etcdResponse.setError(error);

                        return etcdResponse;
                    } else if (!Strings.isNullOrEmpty(json)) {

                        etcdResponse = JSON.parseObject(json, EtcdResponse.class);
                        etcdResponse.setResponseCode(httpResponse.code());
                        return etcdResponse;
                    } else {
                        throw new EtcdException();
                    }

            }

        } catch (Exception ex) {
            InternalLog.error("parseResponse failed", ex);
            if (!Strings.isNullOrEmpty(json)) {
                return createResponseFromException(etcdRequest, ex);
            } else {

                return createResponseFromException(etcdRequest, ex);
            }
        }
    }
}
