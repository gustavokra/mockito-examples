# O que faz o Mockito?
O Mockito tira a dependência de elementos não relevantes em um determinado contexto, para que o teste possa ser realizado de maneira mais simples.

//Para aprofundar - Pesquisar sobre BDDMockito

# Configurando o Mockito
Adicionar ao POM o Mockito Core e Mokito JUnit Jupiter do MVN repository.
https://mvnrepository.com/artifact/org.mockito/mockito-core
https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter

# Começando com Mockito
Para usar o Mockito na sua classe de teste, você deve usar a seguinte anotação:
```
@ExtendWith(MockitoExtension.class)
```

Você deve marcar a variável a ser simulada com @Mock, e após isso programar seu uso.
Exemplo: quando eu chamar o primeiro item da lista, deverá retornar "B":
```
@ExtendWith(MockitoExtension.class)  
public class ListaTest {  
  
    @Mock  
    private List<String> letras;  
  
    @Test  
    void adicionarItem() {  
        Mockito.when(letras.getFirst()).thenReturn("B");  
  
        Assertions.assertEquals("B", letras.getFirst());  
    }  
}
```

Também existe outra alternativa, porém depreciada, de criação de mocks:
```
@BeforeAll  
void setUp() {  
    MockitoAnnotations.initMocks(this);  
}  
  
private ApiDosCorreios apiDosCorreios = Mockito.mock(ApiDosCorreios.class);
```


No exemplo a ser usado, vamos testar um método cadastrarPessoa de uma classe CadastrarPessoa, mockando um objeto usado dentro dele, que é a API dos correios. 
Para que a simulação usando Mockito funcione dentro da classe CadastrarPessoa, precisamos dizer no código que será injetado um mock dentro da classe, com a anotação 
```
@InjectMocks
```
O código fica ssim: 
```
@Mock  
private ApiDosCorreios apiDosCorreios;  
  
@InjectMocks  
private CadastrarPessoa cadastrarPessoa;  
  
@Test  
void validarDadosDeCadastro() {  
	Mockito.when(apiDosCorreios  
        .buscaDadosComBaseNoCep("2131233"))  
        .thenReturn(new DadosLocalizacao("MG",  
                "cidade",  
                "logradouro",  
                "compl",  
                "bairro")  
        );  
	Pessoa pessoa = cadastrarPessoa.cadastrarPessoa("Willyam", "77131311", LocalDate.now(), "2131233");  
	Assertions.assertEquals("Willyam", pessoa.getNome());
}
```

**Outro exemplo de teste com inject mocks: **

```
@ExtendWith(MockitoExtension.class)
public class LoginUserTest {

	@Mock
	private IUserRepository repository;
	  
	@InjectMocks
	private LoginUser loginUser;
  
	@Test
	void testSucessLoginUser() {
		var userDTO = TestUtil.createUserDTO();
		
		var userBO = UserMapper.toBO(userDTO);
		  
		when(repository.findFirstBy(anyList())).thenReturn(userBO);
		
		var token = loginUser.execute(userDTO);
		
		assertNotNull(token);

	}

  

	@Test
	
	void testFailLoginUser() {
		var userDTO = TestUtil.createUserDTO();
		
		when(repository.findFirstBy(anyList())).thenReturn(null);
		
		var token = loginUser.execute(userDTO);
		
		assertNull(token);
	}
  
}
```

# Spy - Espionando Objetos

Para espionar um objeto, basta anotar o mesmo com ```@Spy```

```
@Spy  
private EnviarMensagem enviarMesagem;
```


## Verificar a existência de interações
Para verificar se não existiu interações para o objeto espionado, usamos o ```Mockito.verifyNoInteractions(objeto);```

Para verificar se existiu interações, usamos o     ```Mockito.verify(objeto).metodoVerificar();``` 

```
@Test  
void verificarComportamentoDaClasse() {  
    Mockito.verifyNoInteractions(enviarMesagem);  
    Mensagem mensagem = new Mensagem("Hello World");  
    enviarMesagem.adicionarMensagem(mensagem);  
  
    Mockito.verify(enviarMesagem).adicionarMensagem(mensagem);  
	Mockito.verifyNoInteractions(enviarMesagem);  
}
```


## Verificar a quantidade de interações
Para verificar a quantidade de interações, usamos o método
```Mockito.verify(objeto, times(interações)).metodo;```

Exemplo com a classe "Conta":
```
public class Conta {  
  
    private int saldo;  
  
    public Conta(int saldo) {  
        this.saldo = saldo;  
    }  
  
    public void pagaBoleto(int valorAPagar) {  
        validaSaldo(valorAPagar);  
        debita(valorAPagar);  
        enviaCreditoParaEmissor(valorAPagar);  
    }  
  
    public void validaSaldo(int valorAPagar) {  
        if(valorAPagar > saldo) {  
            throw new IllegalStateException("Saldo insuficiente");  
        }  
    }  
  
    public void debita(int valorAPagar) {  
        this.saldo = this.saldo - valorAPagar;  
    }  
  
    public void enviaCreditoParaEmissor(int valorAPagar) {  
        //envia valor para emissor do boleto  
  
    }  
}
```

```
@Test  
void validarQuantidadeDeChamadas() {  
    conta.validaSaldo(300);  
    conta.validaSaldo(500);  
    conta.validaSaldo(600);  
  
    Mockito.verify(conta, Mockito.times(3))  
            .validaSaldo(ArgumentMatchers.anyInt());  
}
```


## Verificar a ordem de chamadas de métodos
Para verificar a ordem de chamada de métodos, utilizamos a classe InOrder do Mockito, passando o objeto a ser espionado.

```
	InOrder  inOrder = Mockito.inOrder(objeto);
```

Exemplo com a classe "Conta":
```
public class Conta {  
  
    private int saldo;  
  
    public Conta(int saldo) {  
        this.saldo = saldo;  
    }  
  
    public void pagaBoleto(int valorAPagar) {  
        validaSaldo(valorAPagar);  
        debita(valorAPagar);  
        enviaCreditoParaEmissor(valorAPagar);  
    }  
  
    public void validaSaldo(int valorAPagar) {  
        if(valorAPagar > saldo) {  
            throw new IllegalStateException("Saldo insuficiente");  
        }  
    }  
  
    public void debita(int valorAPagar) {  
        this.saldo = this.saldo - valorAPagar;  
    }  
  
    public void enviaCreditoParaEmissor(int valorAPagar) {  
        //envia valor para emissor do boleto  
  
    }  
}
```

No InOrder, verificamos se os métodos forão chamados na ordem correta:
```
@ExtendWith(MockitoExtension.class)  
public class ContaTest {  
  
    @Spy  
    private Conta conta = new Conta(1_000);  
  
    @Test  
    void validarOrdemDeChamadas() {  
  
        conta.pagaBoleto(300);  
        InOrder  inOrder = Mockito.inOrder(conta);  
        inOrder.verify(conta).pagaBoleto(300);  
        inOrder.verify(conta).validaSaldo(300);  
        inOrder.verify(conta).debita(300);  
        inOrder.verify(conta).enviaCreditoParaEmissor(300);  
    }  
  
}
```

# Captor - Capturando argumentos
O Mockito possibilita capturar argumentos passados por parâmetro por um objeto mocado.
Por exemplo, aqui mockamos a "PlataformaDeEnvio" e injetamos o mock no "ServicoEnvioEmail";
Após isso, definimos um "Captor" do objeto "Email";
O email será criado quando o nosso objeto mockado "PlataformaDeEnvio" chamar o método "enviaEmail(Email email)" dentro de "PlataformaDeEnvio";
Como defimos o "Captor" para um objeto Email, podemos capturar o objeto "Email" usado no nosso mock "PlataformaDeEnvio".


```
package org.example;  
  
public class ServicoEnvioEmail {  
  
    private PlataformaDeEnvio plataforma;  
  
    public void enviaEmail(String enderecoEmail, String mensagem, boolean formatoHtml) {  
  
        Email email = null;  
  
        if(formatoHtml) {  
            email = new Email(enderecoEmail, mensagem, Formato.HTML);  
        }else{  
            email = new Email(enderecoEmail, mensagem, Formato.TEXTO);  
        }  
  
        plataforma.enviaEmail(email);  
    }  
}
```


```
@Mock  
private PlataformaDeEnvio plataformaDeEnvio;  
  
@InjectMocks  
private ServicoEnvioEmail servicoEnvioEmail;  
  
@Captor  
private ArgumentCaptor<Email> captor;  
  
@Test  
void validarDadosEnviadosParaAPlataforma() {  
    String email = "usuario@test.com.br";  
    String mensagem = "Olá mundo teste";  
    boolean isHtml = false;  
  
    servicoEnvioEmail.enviaEmail(email, mensagem, isHtml);  
  
    Mockito.verify(plataformaDeEnvio).enviaEmail(captor.capture());  
  
    Email emailCapturado = captor.getValue();  
  
    Assertions.assertEquals(email, emailCapturado.getEnderecoEmail());  
}
```

# Stubbing e ArgumentMatchers
## Stubbing
when, thenReturn, thenThrow, doNothing
Mockito.when(metodo).thenThrow
Mockito.doThrow(Exception.class).when(objeto).metodo();

## ArgumentMatchers
Exemplos:
ArgumentMatchers.any()
ArgumentMatchers.anyString()
ArgumentMatchers.anyInt()

# Mockando métodos estáticos
O mockito-core por si só não possui maneiras de testar métodos estáticos. Porém podemos testar com o **mockito-inline**.

```
<dependency>  
    <groupId>org.mockito</groupId>  
    <artifactId>mockito-inline</artifactId>  
    <version>5.2.0</version>  
    <scope>test</scope>  
</dependency>
```

Para mockar o objeto estático, basta fazermos o seguinte:
```
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
```
