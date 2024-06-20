package io.undertree.starter.ysqljpa.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.BackOffPolicy;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * RetryInterceptor is a Spring @Component that allows for method retries based
 * on a retry policy and back-off policy.
 */
@Component
@EnableRetry
public class RetryInterceptor {

    private final RetryPolicy retryPolicy;
    private final BackOffPolicy backOffPolicy;

    public RetryInterceptor(RetryPolicy retryPolicy, BackOffPolicy backOffPolicy) {
        this.retryPolicy = retryPolicy;
        this.backOffPolicy = backOffPolicy;
    }

    /**
     * Creates and configures a RetryTemplate object.
     *
     * @return a RetryTemplate object with the configured retry policy and
     * back-off policy
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }

    @Bean
    public List<RetryListener> retryListeners() {
        Logger log = LoggerFactory.getLogger(getClass());

        return Collections.singletonList(new RetryListener() {
            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                log.warn("I retried?");
                RetryListener.super.onError(context, callback, throwable);
            }
        });
    }

    /**
     * Returns a RetryOperationsInterceptor for use in methods annotated with
     * <code>@Retry(interceptor="ysqlRetryInterceptor")</code>. The behavior of
     * this interceptor is affected by the configuration of both the retry and
     * back-off policies.
     *
     * @return a RetryOperationsInterceptor bean named "ysqlRetryInterceptor"
     */
    @Bean("ysqlRetryInterceptor")
    public RetryOperationsInterceptor ysqlRetryInterceptor() {
        return RetryInterceptorBuilder.stateless()
                .retryPolicy(retryPolicy)
                .backOffPolicy(backOffPolicy)
                .build();
    }
}