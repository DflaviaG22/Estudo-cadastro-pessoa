package estudo.pessoa.cadastro.exception;

public class CpfInvalidoException extends ValidacaoException {
    public CpfInvalidoException(String mensagem) {
        super(mensagem);
    }

    public CpfInvalidoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
