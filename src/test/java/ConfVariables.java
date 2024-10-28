import java.util.Optional;

public class ConfVariables {
    public static String getHost(){
        return Optional.ofNullable(System.getenv("host"))
                .orElse((String) ApplicationsProperties.getInstance().get("host"));

    }

    public static String getPath(){
        return Optional.ofNullable(System.getenv("basePath"))
                .orElse((String) ApplicationsProperties.getInstance().get("basePath"));

    }
}
