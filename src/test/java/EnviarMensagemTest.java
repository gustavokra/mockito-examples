import org.example.EnviarMensagem;
import org.example.Mensagem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EnviarMensagemTest {

    @Spy
    private EnviarMensagem enviarMesagem;

    @Test
    void verificarComportamentoDaClasse() {
        Mockito.verifyNoInteractions(enviarMesagem);
        Mensagem mensagem = new Mensagem("Hello World");
        enviarMesagem.adicionarMensagem(mensagem);

        Mockito.verify(enviarMesagem).adicionarMensagem(mensagem);
        Assertions.assertFalse(enviarMesagem.getMensagens().isEmpty());
    }
}
