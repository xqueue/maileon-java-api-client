package com.maileon.api;

import java.net.Proxy;
import java.net.SocketAddress;

public class MaileonProxy extends Proxy {

    protected String HttpProxyHost = null;
    protected int HttpProxyPort = 0;
    protected String HttpsProxyHost = null;
    protected int HttpsProxyPort = 0;

    /**
     * Creates an entry representing a PROXY connection. Certain combinations are illegal. For instance, for types Http, and Socks, a SocketAddress <b>must</b> be provided.
     * <p>
     * Use the {@code Proxy.NO_PROXY} constant for representing a direct connection.
     *
     * @param type the {@code Type} of the proxy
     * @param sa the {@code SocketAddress} for that proxy
     * @throws IllegalArgumentException when the type and the address are incompatible
     */
    public MaileonProxy(Type type, SocketAddress sa) {
        super(type, sa);
    }

    public String getHttpProxyHost() {
        return HttpProxyHost;
    }

    public void setHttpProxyHost(String httpProxyHost) {
        HttpProxyHost = httpProxyHost;
    }

    public int getHttpProxyPort() {
        return HttpProxyPort;
    }

    public void setHttpProxyPort(int httpProxyPort) {
        HttpProxyPort = httpProxyPort;
    }

    public String getHttpsProxyHost() {
        return HttpsProxyHost;
    }

    public void setHttpsProxyHost(String httpsProxyHost) {
        HttpsProxyHost = httpsProxyHost;
    }

    public int getHttpsProxyPort() {
        return HttpsProxyPort;
    }

    public void setHttpsProxyPort(int httpsProxyPort) {
        HttpsProxyPort = httpsProxyPort;
    }
}
