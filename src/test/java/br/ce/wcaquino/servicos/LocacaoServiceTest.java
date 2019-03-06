package br.ce.wcaquino.servicos;



import static br.ce.wcaquino.builder.UsuarioBuilder.umUsuario;
import static br.ce.wcaquino.matchers.MatchersProprios.caiNumaSegunda;
import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static br.ce.wcaquino.utils.DataUtils.obterDataComDiferencaDias;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.builder.UsuarioBuilder;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Before
	public void setup(){
		service = new LocacaoService();
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		
		assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
	}
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception{
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 4.0));
		
		//acao
		service.alugarFilme(usuario, filmes);
	}
	
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException{
		//cenario
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//acao
		try {
			service.alugarFilme(null, filmes);
			Assert.fail();
		} catch (LocadoraException e) {
			assertThat(e.getMessage(), is("Usuario vazio"));
		}
	}

	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException{
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio");
		
		//acao
		service.alugarFilme(usuario, null);
	}
	
	@Test
	public void devePagar75PorcentoNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário
		Usuario usuario = new Usuario();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), 
										   new Filme("Filme 2", 2, 4.0), 
										   new Filme("Filme 3", 2, 4.0));
		//Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(11.0));
	}
	
	@Test
	public void devePagar50PorcentoNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário
		Usuario usuario = new Usuario();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), 
										   new Filme("Filme 2", 2, 4.0), 
										   new Filme("Filme 3", 2, 4.0),
										   new Filme("Filme 4", 2, 4.0));
		//Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(13.0));
	}
	
	@Test
	public void devePagar25PorcentoNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário
		Usuario usuario = new Usuario();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), 
										   new Filme("Filme 2", 2, 4.0), 
										   new Filme("Filme 3", 2, 4.0),
										   new Filme("Filme 4", 2, 4.0),
										   new Filme("Filme 5", 2, 4.0));
		//Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void devePagar0PorcentoNoFilme6() throws FilmeSemEstoqueException, LocadoraException {
		//Cenário
		Usuario usuario = new Usuario();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0), 
										   new Filme("Filme 2", 2, 4.0), 
										   new Filme("Filme 3", 2, 4.0),
										   new Filme("Filme 4", 2, 4.0),
										   new Filme("Filme 5", 2, 4.0),
										   new Filme("Filme 6", 2, 4.0));
		//Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação
		assertThat(resultado.getValor(), is(14.0));
	}
	
	@Test
	public void deveDevolverFilmeNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {
		
		assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//Cenário
		Usuario usuario = new Usuario();
		List<Filme> filmes =  Arrays.asList(new Filme("Frozen 2: o inimigo agora é outro", 1, 5.0));
		
		//Ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//Verificação		
		assertThat(resultado.getDataRetorno(), caiNumaSegunda());
	}
}
