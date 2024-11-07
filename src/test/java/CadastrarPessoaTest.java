import org.example.ApiDosCorreios;
import org.example.CadastrarPessoa;
import org.example.DadosLocalizacao;
import org.example.Pessoa;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class CadastrarPessoaTest {

    @Mock
    private ApiDosCorreios apiDosCorreios;

    @InjectMocks
    private CadastrarPessoa cadastrarPessoa;

    @Test
    void validarDadosDeCadastro() {
        DadosLocalizacao dadosLocalizacao = new DadosLocalizacao("MG","cidade", "logradouro", "compl", "bairro");

        Mockito.when(apiDosCorreios
                .buscaDadosComBaseNoCep(ArgumentMatchers.anyString()))
                .thenReturn(dadosLocalizacao);

//        Mockito.doThrow(IllegalArgumentException.class)
//                .when(apiDosCorreios).buscaDadosComBaseNoCep(ArgumentMatchers.anyString());

        Pessoa pessoa = cadastrarPessoa.cadastrarPessoa("Willyam", "77131311", LocalDate.now(), "2131233");
        Assertions.assertEquals("Willyam", pessoa.getNome());
    }

}
