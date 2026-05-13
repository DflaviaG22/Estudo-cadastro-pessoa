package estudo.pessoa.cadastro.exception;

public class ValidadorCadastro {

    public static String validareCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new CepInvalidoException("CEP é obrigatório");
        }

        String cepNumerico = cep.replaceAll("\\D", "");

        if (cepNumerico.length() != 8) {
            throw new CepInvalidoException("CEP deve conter 8 dígitos. Formato esperado: XXXXX-XXX");
        }

        return cepNumerico;
    }

    public static String validareCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new CpfInvalidoException("CPF é obrigatório");
        }

        String cpfNumerico = cpf.replaceAll("\\D", "");

        if (cpfNumerico.length() != 11) {
            throw new CpfInvalidoException("CPF deve conter 11 dígitos. Formato esperado: XXX.XXX.XXX-XX");
        }

        return cpfNumerico;
    }

    public static void validarCepExistente(Object endereco) {
        if (endereco == null || (endereco instanceof java.util.Map && ((java.util.Map<?, ?>) endereco).containsKey("erro"))) {
            throw new CepInvalidoException("CEP informado não existe ou é inválido. Verifique e tente novamente.");
        }
    }
}
