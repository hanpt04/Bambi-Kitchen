package gr1.fpt.bambikitchen.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@EnableScheduling
@Configuration
public class ScheduledConfig {

    /**
     * Cấu hình Task Scheduler cho các công việc lập lịch
     */
//    @Bean
//    public TaskScheduler taskScheduler() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(1);
//        scheduler.setThreadNamePrefix("scheduled-task-");
//        scheduler.setWaitForTasksToCompleteOnShutdown(true);
//        scheduler.setAwaitTerminationSeconds(30);
//        return scheduler;
//    }

//    /**
//     * Thread pool executor tự tạo với tên pool
//     * Muốn dùng thì @Async("mailPool") ở chỗ cần dùng
//     * @return ThreadPoolTaskExecutor
//     */
//    @Bean(name = "mailPool")
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(5);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(25);
//        executor.setThreadNamePrefix("async-");
//        executor.initialize();
//        return executor;
//    }
}
