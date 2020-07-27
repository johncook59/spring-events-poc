package scratch.events;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class SpringEventsApplication {

    @Autowired
    StatsDClient statsDClient;

    public static void main(String[] args) {
        SpringApplication.run(SpringEventsApplication.class, args);
    }

    @PreDestroy
    void onShutdown() {
        try {
            Thread.sleep(2000); // Let the event publisher finish
            statsDClient.close();
        } catch (InterruptedException ignored) {
        }
    }

    @Bean
    StatsDClient statsDClient() {
        return new NonBlockingStatsDClientBuilder()
                .prefix("spring_events_poc")
                .hostname("localhost")
                .port(8125)
                .build();
    }
}
