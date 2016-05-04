package org.mokey.acupple.jetcd;

public interface Etcd {
	
    /**
     * EtcdRequest something from Etcd using standard Etcd client commands. Non blocking.
     * @param responseHandler handler
     */
    void request(Handler<EtcdResponse> responseHandler, EtcdRequest etcdRequest);


    /**
     * EtcdRequest something from Etcd using standard Etcd client commands. Blocking.
     */
    EtcdResponse request(EtcdRequest etcdRequest);


    /**
     * Create a directory using async handler
     * @param responseHandler handler
     * @param name name of dir
     */
    void createDir(Handler<EtcdResponse> responseHandler, String name);

    /**
     * Create a directory (blocking)
     * @param name name of dir
     * @return response
     */
    EtcdResponse createDir(String name);


    /**
     * Create a temp directory, i.e., one with a time to live TTL
     * @param name name of dir
     * @param ttl ttl
     * @return
     */
    EtcdResponse createTempDir(String name, long ttl);

    /**
     * Create a temp dir async.
     * @param responseHandler async handler
     * @param name name of dir
     * @param ttl time to live
     */
    void createTempDir(Handler<EtcdResponse> responseHandler, String name, long ttl);

    /**
     * Update a directories time to live.
     * @param name dir name (path)
     * @param ttl ttl
     * @return
     */
    EtcdResponse updateDirTTL(String name, long ttl);

    /**
     * Update a directories time to live.
     * @param responseHandler
     * @param name
     * @param ttl
     */
    void updateDirTTL(Handler<EtcdResponse> responseHandler, String name, long ttl);

    /**
     * Delete a dir
     * @param name
     * @return
     */
    EtcdResponse deleteDir(String name);

    /**
     * Delete a dir async.
     * @param responseHandler
     * @param name
     */
    void deleteDir(Handler<EtcdResponse> responseHandler, String name);


    /**
     * Delete a dir and all of its children recursively.
     * @param name
     * @return
     */
    EtcdResponse deleteDirRecursively(String name);
    void deleteDirRecursively(Handler<EtcdResponse> responseHandler, String name);


    /**
     * List keys and value
     * @param key
     * @return
     */
    EtcdResponse list(String key);


    /**
     * List keys and values asycn
     * @param responseHandler
     * @param key
     */
    void list(Handler<EtcdResponse> responseHandler, String key);

    /**
     * List dir recursively.
     * @param key
     * @return
     */
    EtcdResponse listRecursive(String key);
    void listRecursive(Handler<EtcdResponse> responseHandler, String key);

    /**
     * List dir sorted for order so we can pull things out FIFO for job queuing.
     * @param key
     * @return
     */
    EtcdResponse listSorted(String key);
    void listSorted(Handler<EtcdResponse> responseHandler, String key);


    /**
     * Add key / value to dir
     * @param key
     * @param value
     * @return
     */
    EtcdResponse addToDir(String dirName, String key, String value);
    void addToDir(Handler<EtcdResponse> responseHandler, String dirName, String key, String value);

    /**
     * Set a key
     * @param key
     * @param value
     * @return
     */
    EtcdResponse set(String key, String value);
    void set(Handler<EtcdResponse> responseHandler, String key, String value);


    /**
     * Update the key with a new value if it already exists
     * @param key
     * @param value
     * @return
     */
    EtcdResponse setIfExists(String key, String value);
    void  setIfExists(Handler<EtcdResponse> responseHandler, String key, String value);


    /**
     * Create the new key value only if it does not already exist.
     * @param key
     * @param value
     * @return
     */
    EtcdResponse setIfNotExists(String key, String value);
    void  setIfNotExists(Handler<EtcdResponse> responseHandler, String key, String value);

    /**
     * Create a temporary value with ttl set
     * @param key
     * @param value
     * @param ttl
     * @return
     */
    EtcdResponse setTemp(String key, String value, int ttl);
    void  setTemp(Handler<EtcdResponse> responseHandler, String key, String value, int ttl);

    /**
     * Remove TTL from key/value
     * @param key
     * @param value
     * @return
     */
    EtcdResponse removeTTL(String key, String value);
    void removeTTL(Handler<EtcdResponse> responseHandler, String key, String value);


    /**
     * Compare and swap if the previous value is the same
     * @param key
     * @param preValue
     * @param value
     * @return
     */
    EtcdResponse compareAndSwapByValue(String key, String preValue, String value);
    void compareAndSwapByValue(Handler<EtcdResponse> responseHandler, String key, String preValue, String value);

    /**
     * Compare and swap if the modified index has not changed.
     * @param key
     * @param prevIndex
     * @param value
     * @return
     */
    EtcdResponse compareAndSwapByModifiedIndex(String key, long prevIndex, String value);
    void compareAndSwapByModifiedIndex(Handler<EtcdResponse> responseHandler, String key, long prevIndex, String value);


    /**
     * Get the value
     * @param key
     * @return
     */
    EtcdResponse get(String key);
    void get(Handler<EtcdResponse> responseHandler, String key);


    /**
     * Get the value and ensure it is consistent. (Slow but consistent)
     * @param key
     * @return
     */
    EtcdResponse getConsistent(String key);
    void getConsistent(Handler<EtcdResponse> responseHandler, String key);

    /**
     * Wait for this key to change
     * @param key
     * @return
     */
    EtcdResponse wait(String key);
    void wait(Handler<EtcdResponse> responseHandler, String key);


    /**
     * Wait for this key to change and you can ask for the past key value based on index just in case you missed it.
     * @param key
     * @param index
     * @return
     */
    EtcdResponse wait(String key, long index);
    void wait(Handler<EtcdResponse> responseHandler, String key, long index);


    /**
     * Wait for this key to change and any key under this key dir recursively.
     * @param key
     * @return
     */
    EtcdResponse waitRecursive(String key);
    void waitRecursive(Handler<EtcdResponse> responseHandler, String key);


    /**
     * Wait for this key to change and any key under this key dir recursively, and
     * ask for the past key value based on index just in case you missed it.
     * @param key
     * @param index
     * @return
     */
    EtcdResponse waitRecursive(String key, long index);
    void waitRecursive(Handler<EtcdResponse> responseHandler, String key, long index);

    /**
     * Delete the key.
     * @param key
     * @return
     */
    EtcdResponse delete(String key);
    void delete(Handler<EtcdResponse> responseHandler, String key);

    /** Delete the key only if it is at this index
     *
     * @param key
     * @param index
     * @return
     */
    EtcdResponse deleteIfAtIndex(String key, long index);
    void deleteIfAtIndex(Handler<EtcdResponse> responseHandler, String key, long index);

    /**
     * Delete the value but only if it is at the previous value
     * @param key
     * @param prevValue
     * @return
     */
    EtcdResponse deleteIfValue(String key, String prevValue);
    void deleteIfValue(Handler<EtcdResponse> responseHandler, String key, String prevValue);


}
