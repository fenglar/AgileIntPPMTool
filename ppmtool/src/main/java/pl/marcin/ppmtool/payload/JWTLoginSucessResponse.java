package pl.marcin.ppmtool.payload;

public class JWTLoginSucessResponse {
    private boolean success;
    private String token;

    public JWTLoginSucessResponse(boolean succes, String token) {
        this.success = succes;
        this.token = token;
    }

    public boolean isSucces() {
        return success;
    }

    public void setSucces(boolean succes) {
        this.success = succes;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JWTLoginSucessReponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                '}';
    }
}
