package br.com.fiap.gs.domain.exception;

public class TerraformingException extends RuntimeException {

    private final String code;

    public TerraformingException(String code, String message) {
        super(message);
        this.code = code;
    }

    public TerraformingException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "[" + code + "] " + getMessage();
    }
}
