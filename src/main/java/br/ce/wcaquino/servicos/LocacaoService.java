package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	private static final int INDICE_SEXTO_FILME = 5;
	private static final int INDICE_QUINTO_FILME = 4;
	private static final int INDICE_QUARTO_FILME = 3;
	private static final int INDICE_TERCEIRO_FILME = 2;

	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		if(usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
		
		for(Filme filme: filmes) {
			if(filme.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		
		Double valorTotal = 0d;
		
		for(int i = 0; i < filmes.size(); i++) {
			
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
			
			valorFilme = aplicaDescontosNoValorDoFilme(i, valorFilme);
			
			valorTotal += valorFilme;
		}
		
		locacao.setValor(valorTotal);
		
		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		
		if(ehDomingo(dataEntrega)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

	private boolean ehDomingo(Date dataEntrega) {
		return DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY);
	}

	private Double aplicaDescontosNoValorDoFilme(int i, Double valorFilme) {
		if(i == INDICE_TERCEIRO_FILME) {
			valorFilme = valorFilme * 0.75;
		}
		
		if(i == INDICE_QUARTO_FILME) {
			valorFilme = valorFilme * 0.50;
		}
		
		if(i == INDICE_QUINTO_FILME) {
			valorFilme = valorFilme * 0.25;
		}
		
		if(i == INDICE_SEXTO_FILME) {
			valorFilme = valorFilme * 0;
		}
		
		return valorFilme;
	}
}