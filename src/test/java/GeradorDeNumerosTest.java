import org.example.GeradorDeNumeros;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GeradorDeNumerosTest {

    @Test
    void testaGeracaoComTamanhoDefinido() {
        MockedStatic<GeradorDeNumeros> mockedStatic = Mockito.mockStatic(GeradorDeNumeros.class);
        mockedStatic.when(() -> GeradorDeNumeros.geraNumerosAleatorios(1)).thenReturn(List.of(1));

        var result = GeradorDeNumeros.geraNumerosAleatorios(1);

        Assertions.assertEquals(List.of(1), result);
    }
}
