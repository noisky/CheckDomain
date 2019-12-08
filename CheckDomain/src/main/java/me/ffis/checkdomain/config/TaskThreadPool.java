package me.ffis.checkdomain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定义异步任务线程池
 * Created by fanfan on 2019/12/08.
 */
@Configuration
public class TaskThreadPool {
    @Bean
    public Executor taskExecutor() {
        /*
         ThredPoolTaskExcutor的任务处理流程：
         当池子大小小于corePoolSize，就新建线程，并处理请求
         当池子大小等于corePoolSize，把请求放入缓冲队列中，池子里有空闲线程就去缓冲队列中取任务并处理
         当缓冲队列放不下任务时，就继续新建线程入池，并处理请求，当池子大小撑到了maximumPoolSize，就用RejectedExecutionHandler来做拒绝Task处理
         当池子的线程数大于corePoolSize时，多余的线程会等待keepAliveTime长时间，如果无请求可处理就自行销毁
         */
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数10：线程池创建时候初始化的线程数
        executor.setCorePoolSize(5);
        //最大线程数20：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setMaxPoolSize(15);
        //缓冲队列200：用来缓冲执行任务的队列
        executor.setQueueCapacity(50);
        //允许线程的空闲时间60秒：当超过了核心线程数之外的线程在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(60);
        //线程池名的前缀：设置好了之后可以方便定位处理任务所在的线程池
        executor.setThreadNamePrefix("taskExecutor-");
        /*
        对拒绝task的处理策略
        (1) 默认为ThreadPoolExecutor.AbortPolicy 处理程序遭到拒绝将抛出运行时RejectedExecutionException;
        (2) ThreadPoolExecutor.CallerRunsPolicy 当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务，此策略提供简单的反馈控制机制，能够减缓新任务的提交速度，如果执行程序已关闭，则会丢弃该任务
        (3) ThreadPoolExecutor.DiscardPolicy  不能执行的任务将被删除;
        (4) ThreadPoolExecutor.DiscardOldestPolicy  如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）
        这里采用了CallerRunsPolicy策略，
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务的等待时间，如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住。
        executor.setAwaitTerminationSeconds(600);
        return executor;
    }
}
