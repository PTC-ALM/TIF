/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.integrity;

/**
 *
 * @author pbowden
 */
public class IntegrityConnection
{

    private String host;
    private int port;
    private String user;
    private String password;

    public IntegrityConnection(String host, int port)
    {
        this(host, port, "", "");
    }

    public IntegrityConnection(String host, int port, String user)
    {
        this(host, port, user, "");
    }

    public IntegrityConnection(String host, int port, String user, String password)
    {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }

    public String getUser()
    {
        return user;
    }
    public String getPassword()
    {
        return password;
    }

    public String toString()
    {
        return user + "@" + host + ":" + port;
    }

}


