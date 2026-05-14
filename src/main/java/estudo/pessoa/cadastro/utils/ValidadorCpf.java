package estudo.pessoa.cadastro.utils;

public class ValidadorCpf {

    public static boolean ehValido(String cpf) {
        if (cpf == null) {
            return false;
        }

        String cpfNumerico = cpf.replaceAll("\\D", "");

        if (cpfNumerico.length() != 11 || cpfNumerico.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (cpfNumerico.charAt(i) - '0') * (10 - i);
            }

            int digito1 = 11 - (soma % 11);
            if (digito1 > 9) {
                digito1 = 0;
            }

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (cpfNumerico.charAt(i) - '0') * (11 - i);
            }

            int digito2 = 11 - (soma % 11);
            if (digito2 > 9) {
                digito2 = 0;
            }

            return digito1 == (cpfNumerico.charAt(9) - '0')
                    && digito2 == (cpfNumerico.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean validarCpf(String cpf) {
        return ehValido(cpf);
    }
}
