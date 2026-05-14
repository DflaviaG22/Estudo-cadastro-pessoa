package estudo.pessoa.cadastro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class CadastroException extends ResponseStatusException {
    private final int statusCode;

    public CadastroException(String mensagem, int statusCode) {
        super(HttpStatus.valueOf(statusCode), mensagem);
        this.statusCode = statusCode;
    }

    public CadastroException(String mensagem, int statusCode, Throwable causa) {
        super(HttpStatus.valueOf(statusCode), mensagem, causa);
        this.statusCode = statusCode;
    }

    public int getStatusCodeValue() {
        return statusCode;
    }

    public String getMensagem() {
        return getReason();
    }
}
