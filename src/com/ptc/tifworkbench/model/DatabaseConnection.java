/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ptc.tifworkbench.model;

/**
 *
 * @author pbowden
 */
public class DatabaseConnection 
{
    private String dbName;
    private String dbServer;
    private String restorePoint;
    private String dbUser;
    private String dbPassword;
    
    public DatabaseConnection(String dbName, String dbServer, String restorePoint,
            String dbUser, String dbPassword)
    {
        this.dbName = dbName;
        this.dbServer = dbServer;
        this.restorePoint = restorePoint;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * @return the dbName
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * @param dbName the dbName to set
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * @return the dbServer
     */
    public String getDbServer() {
        return dbServer;
    }

    /**
     * @param dbServer the dbServer to set
     */
    public void setDbServer(String dbServer) {
        this.dbServer = dbServer;
    }

    /**
     * @return the restorePoint
     */
    public String getRestorePoint() {
        return restorePoint;
    }

    /**
     * @param restorePoint the restorePoint to set
     */
    public void setRestorePoint(String restorePoint) {
        this.restorePoint = restorePoint;
    }

    /**
     * @return the dbUser
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * @param dbUser the dbUser to set
     */
    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    /**
     * @return the dbPassword
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * @param dbPassword the dbPassword to set
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
