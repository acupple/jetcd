package org.mokey.acupple.jetcd;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by enousei on 4/21/16.
 */
public class Settings {
    private List<URI> urls = new ArrayList<>();
    private AtomicInteger currentIndex = new AtomicInteger(0);
    private boolean useSSL = false;
    private int poolSize;

    private int timeOutInMilliseconds;

    private String sslTrustStorePath;
    private String sslTrustStorePassword;

    private String sslKeyStorePath;
    private String sslKeyStorePassword;

    private boolean sslAuthRequired;

    private boolean followLeader;

    private boolean sslTrustAll;

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getTimeOutInMilliseconds() {
        return timeOutInMilliseconds;
    }

    public void setTimeOutInMilliseconds(int timeOutInMilliseconds) {
        this.timeOutInMilliseconds = timeOutInMilliseconds;
    }

    public String getSslTrustStorePath() {
        return sslTrustStorePath;
    }

    public void setSslTrustStorePath(String sslTrustStorePath) {
        this.sslTrustStorePath = sslTrustStorePath;
    }

    public String getSslTrustStorePassword() {
        return sslTrustStorePassword;
    }

    public void setSslTrustStorePassword(String sslTrustStorePassword) {
        this.sslTrustStorePassword = sslTrustStorePassword;
    }

    public String getSslKeyStorePath() {
        return sslKeyStorePath;
    }

    public void setSslKeyStorePath(String sslKeyStorePath) {
        this.sslKeyStorePath = sslKeyStorePath;
    }

    public String getSslKeyStorePassword() {
        return sslKeyStorePassword;
    }

    public void setSslKeyStorePassword(String sslKeyStorePassword) {
        this.sslKeyStorePassword = sslKeyStorePassword;
    }

    public boolean isSslAuthRequired() {
        return sslAuthRequired;
    }

    public void setSslAuthRequired(boolean sslAuthRequired) {
        this.sslAuthRequired = sslAuthRequired;
    }

    public boolean isFollowLeader() {
        return followLeader;
    }

    public void setFollowLeader(boolean followLeader) {
        this.followLeader = followLeader;
    }

    public boolean isSslTrustAll() {
        return sslTrustAll;
    }

    public void setSslTrustAll(boolean sslTrustAll) {
        this.sslTrustAll = sslTrustAll;
    }

    public boolean isUseSSL() {
        return useSSL;
    }

    public void setUseSSL(boolean useSSL) {
        this.useSSL = useSSL;
    }

    public void addUrl(String url){
        try{
            URI uri = URI.create(url);
            urls.add(uri);
        }catch (Exception ex){}
    }

    public URI loadBalance(){
        int index = this.currentIndex.get();

        int oldIndex = index;

        if (index + 1 == urls.size()) {
            index = 0;
        } else {
            index++;
        }

        if (this.currentIndex.compareAndSet(oldIndex, index)) {
            return urls.get(index);
        }

        return urls.get(currentIndex.get());
    }
}
