package no.nav.kontantstotte.proxy.config.toggle;

import no.finn.unleash.DefaultUnleash;
import no.finn.unleash.Unleash;
import no.finn.unleash.strategy.Strategy;
import no.finn.unleash.util.UnleashConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Configuration
public class FeatureToggleConfig {

    private static final String APP_NAME_PROPERTY_NAME = "${APP_NAME}";
    private static final String UNLEASH_API_URL_PROPERTY_NAME = "${UNLEASH_API_URL}";
    public static final String FASIT_ENVIRONMENT_NAME = "${FASIT_ENVIRONMENT_NAME}";

    public static final String KONTANTSTOTTE_BRUK_VEDLEGG = "kontantstotte.bruk.vedlegg";

    @Inject
    private Unleash unleash;

    @Profile("!dev")
    @Bean
    public Unleash unleash(
            @Value(APP_NAME_PROPERTY_NAME) String appName,
            @Value(UNLEASH_API_URL_PROPERTY_NAME) String unleashApiUrl,
            Strategy... strategies
    ) {
        UnleashConfig config = UnleashConfig.builder()
                .appName(appName)
                .unleashAPI(unleashApiUrl)
                .build();

        return new DefaultUnleash(config, strategies);
    }

    @Profile("!dev")
    @Bean
    public Strategy isNotProd(@Value(FASIT_ENVIRONMENT_NAME) String env){
        return new IsNotProdStrategy(env);
    }

    @Profile("!dev")
    @Bean
    public Strategy byEnvironment(@Value(FASIT_ENVIRONMENT_NAME) String env){
        return new ByEnvironmentStrategy(env);
    }

    @PostConstruct
    public void initUnleashProvider() {
        UnleashProvider.initialize(unleash);
    }
}
