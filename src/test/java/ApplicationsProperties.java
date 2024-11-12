import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;

public class ApplicationsProperties {
    private  static Properties instance = null;
    private static final String APPLICATION_PREFIX = "application";
    private static final String APPLICATION_SUFIX = "properties";
    private static final Logger logger = LogManager.getLogger(ApplicationsProperties.class);

    public static synchronized Properties getInstance(){
        if (instance== null){
            instance=loadPropertiesFile();

        }
        return instance;
    }

    private ApplicationsProperties(){
    }
    private static Properties loadPropertiesFile() {
        String enviroment = Optional.ofNullable(System.getenv("env"))
                .orElse("dev");

        String fileName = String.format("%s-%s.%s", APPLICATION_PREFIX, enviroment, APPLICATION_SUFIX);
        logger.info("Property file to read {}", fileName);

        Properties pro = new Properties();
        try {
            pro.load(getClassLoader().getResourceAsStream(fileName));

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to load the file {}", fileName);

        }
        return pro;

    }
}
