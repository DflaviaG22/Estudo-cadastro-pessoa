package estudo.pessoa.cadastro.exception;

public class ValidacaoException extends CadastroException {
    public ValidacaoException(String mensagem) {
        super(mensagem, 400);
    }

    public ValidacaoException(String mensagem, Throwable causa) {
        super(mensagem, 400, causa);
    }
}
