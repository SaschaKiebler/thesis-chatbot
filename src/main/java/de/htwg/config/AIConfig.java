package de.htwg.config;

import org.eclipse.microprofile.config.ConfigProvider;

public class AIConfig {
    public static final String SYSTEM_PROMPT;

    static {
        // Here you load the value from configuration
        // For example, using MicroProfile Config
        SYSTEM_PROMPT = ConfigProvider.getConfig().getValue("ai.system.prompt", String.class);
    }
}
