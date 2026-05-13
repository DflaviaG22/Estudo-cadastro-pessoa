package estudo.pessoa.cadastro.exception;

public class CepInvalidoException extends ValidacaoException {
    public CepInvalidoException(String mensagem) {
        super(mensagem);
    }

    public CepInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
