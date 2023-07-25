package net.dstone.common.config;
import java.io.FileNotFoundException;
import java.util.Properties;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Component;

import net.dstone.common.utils.LogUtil;

@Component("configProperty")
public class ConfigProperty {

	@SuppressWarnings("unused")
	private static final LogUtil logger = new LogUtil(ConfigProperty.class);

	private static Properties PROP;
	static {
		try {
			String profile = System.getProperty("SEVER_KIND", System.getProperty("spring.profiles.active", ""));
			Resource resource = new ClassPathResource("application"+profile+".yml");
			EncodedResource encodedResource = new EncodedResource(resource);
			PROP = load(encodedResource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**
     * Load properties from the YAML file.
     * @param resource Instance of {@link EncodedResource}
     * @return instance of properties
     */
    private static Properties load(EncodedResource resource) throws FileNotFoundException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (IllegalStateException ex) {
            /* Ignore resource not found. */
            Throwable cause = ex.getCause();
            if (cause instanceof FileNotFoundException) throw (FileNotFoundException) cause;
            throw ex;
        }
    }

	public static String getProperty(String key) {
		return PROP.getProperty(key);
	}
	
}

