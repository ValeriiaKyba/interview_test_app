package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;


public class ConfigProvider {

    private static final String INNER_PROPERTY_PATTERN = "\\$\\{([a-zA-Z0-9]+(?:\\.[a-zA-Z0-9]+)*+)}";
    private static final ConfigProvider INSTANCE = new ConfigProvider();

    private Properties properties = new Properties();

    private ConfigProvider() {
        try {
            properties.putAll(loadPropertiesFromFile("configuration.properties"));
        } catch (final IllegalStateException e) {
            throw new IllegalStateException("Failed to load configuration file", e);
        }
    }

    private static final String BASE_URL = System.getProperty(
            "base.url",
            configuration().getProperty("base.url")
    );

    public static ConfigProvider configuration() {
        return INSTANCE;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public String getProperty(final String propertyName) {
        String property = System.getProperty(propertyName, properties.getProperty(propertyName));
        if (nonNull(property) && Pattern.matches(INNER_PROPERTY_PATTERN, property)) {
            final Matcher m = Pattern.compile(INNER_PROPERTY_PATTERN).matcher(property);
            while (m.find()) {
                final String innerPropertyName = m.group(1);
                String innerProperty = getProperty(innerPropertyName);
                property = property.replaceFirst(Pattern.quote(m.group(0)), Matcher.quoteReplacement(innerProperty));
            }
        }
        return property;
    }

    private Properties loadPropertiesFromFile(String pathToPropertyFile) {
        final InputStream inputStream = Optional
                .ofNullable(ConfigProvider.class.getClassLoader().getResourceAsStream(pathToPropertyFile))
                .orElseThrow(
                        () -> new IllegalStateException("Unable to load properties - file might not be specified: ".concat(pathToPropertyFile)));
        final Properties props = new Properties();
        try {
            props.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load properties from resource: ".concat(pathToPropertyFile));
        }
        return props;
    }

}
