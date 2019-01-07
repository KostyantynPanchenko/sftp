package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sftp")
class SftpParams {

    private String host;
    private int port;
    private String user;
    private String password;
    private String remoteDirectoryDownload;
    private String localDirectoryDownload;
    private String remoteDirectoryDownloadFilter;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pasword) {
        this.password = pasword;
    }

    public String getRemoteDirectoryDownload() {
        return remoteDirectoryDownload;
    }

    public void setRemoteDirectoryDownload(String remoteDirectoryDownload) {
        this.remoteDirectoryDownload = remoteDirectoryDownload;
    }

    public String getLocalDirectoryDownload() {
        return localDirectoryDownload;
    }

    public void setLocalDirectoryDownload(String localDirectoryDownload) {
        this.localDirectoryDownload = localDirectoryDownload;
    }

    public String getRemoteDirectoryDownloadFilter() {
        return remoteDirectoryDownloadFilter;
    }

    public void setRemoteDirectoryDownloadFilter(String remoteDirectoryDownloadFilter) {
        this.remoteDirectoryDownloadFilter = remoteDirectoryDownloadFilter;
    }
}
