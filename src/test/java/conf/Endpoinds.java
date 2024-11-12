package conf;

public enum Endpoinds {
    REGISTER("/register"),
    USERS("/users{userId}");

    private final String path;
    Endpoinds(String path){
        this.path=path;
    }

    public String path(){
        return path;
    }
}
