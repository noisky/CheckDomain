package me.ffis.checkdomain.util;

import me.ffis.checkdomain.model.LogFileName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by fanfan on 2019/12/08.
 */
public class LoggerUtils {
    public final static <T> Logger Logger(Class<T> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    /**
     * 打印到指定的文件下
     *
     * @param fileName 日志文件名称
     * @return Logger
     */
    public final static Logger Logger(LogFileName fileName) {
        return LoggerFactory.getLogger(fileName.getLogFileName());
    }
}
