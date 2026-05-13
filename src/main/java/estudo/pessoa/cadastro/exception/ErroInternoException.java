package estudo.pessoa.cadastro.exception;

public class ErroInternoException extends CadastroException {
    public ErroInternoException(String mensagem) {
        super(mensagem, 500);
    }

    public ErroInternoException(String mensagem, Throwable causa) {
        super(mensagem, 500, causa);
    }
}
