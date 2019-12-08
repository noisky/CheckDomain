package me.ffis.checkdomain.model;

/**
 * Created by fanfan on 2019/12/07.
 */
public enum LogFileName {

    MAIL_LOGS("mailLogs");

    private String logFileName;

    LogFileName(String fileName) {
        this.logFileName = fileName;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }
}
