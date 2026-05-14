package estudo.pessoa.cadastro.exception;

public class CpfJaCadastradoException extends CadastroException {
    public CpfJaCadastradoException(String mensagem) {
        super(mensagem, 409);
    }
}
