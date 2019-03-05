package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;


public class CalculadoraTest {
	
	private Calculadora calculadora; 
	
	@Before
	public void setup() {
		calculadora = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		//Cenário
		int a = 5;
		int b = 3;
		
		//Ação
		int resultado = calculadora.somar(a, b);
		
		//Verificação
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//Cenário
		int a = 8;
		int b = 5;
		
		//Ação
		int resultado = calculadora.subtrair(a, b);
		
		//Verificação
		assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//Cenário
		int a = 6;
		int b = 3;
		
		//Ação
		int resultado = calculadora.dividir(a, b);
		
		//Verificação
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//Cenário
		int a = 10;
		int b = 0;
		
		//Ação
		calculadora.dividir(a,b);
	}
}
