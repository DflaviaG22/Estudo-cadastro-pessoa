package estudo.pessoa.cadastro.exception;

public class RecursoNaoEncontradoException extends CadastroException {
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem, 404);
    }

    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, 404, causa);
    }
}
