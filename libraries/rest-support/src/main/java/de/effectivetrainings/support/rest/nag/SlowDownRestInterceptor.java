package de.effectivetrainings.support.rest.nag;

import com.codahale.metrics.MetricRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Analog Netflix-Simian-Army.
 * <p>
 * Randomly slow down rest requests. This forces us to build for slow and failing connections.
 * You never know when it hits you.
 */
@Slf4j
public class SlowDownRestInterceptor implements ClientHttpRequestInterceptor {

    private MetricRegistry metricRegistry;

    public SlowDownRestInterceptor(MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //every fifth request
        if (Math.round(Math.random() * 5) == 5) {
            try {
                log.info("[Request Nag] Slowing down request by 4 seconds.");
                metricRegistry.meter("request.nag.slowDown").mark();
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        return execution.execute(request, body);
    }
}
