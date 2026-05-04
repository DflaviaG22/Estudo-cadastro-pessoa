package estudo.pessoa.cadastro.utils;

public class FormatacaoCampo {

    public static String formatarCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String formatarTelefone(String telefone){
        telefone = telefone.replaceAll("[^0-9]","");
        return telefone.replaceAll ("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
    }
}
