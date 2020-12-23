package nl.elstarit.crypto.property;

import lombok.Data;

@Data
public class ConnectionPoolTimeoutProperties {
    private int connectTimeout;
    private int connectionRequestTimeout;
    private int socketTimeout;
}
