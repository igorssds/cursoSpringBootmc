package br.com.curso;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.curso.domain.Categoria;
import br.com.curso.domain.Cidade;
import br.com.curso.domain.Cliente;
import br.com.curso.domain.Endereco;
import br.com.curso.domain.Estado;
import br.com.curso.domain.Produto;
import br.com.curso.domain.enums.TipoCliente;
import br.com.curso.repositories.CategoriaRepository;
import br.com.curso.repositories.CidadeRepository;
import br.com.curso.repositories.ClienteRepository;
import br.com.curso.repositories.EnderecoRepository;
import br.com.curso.repositories.EstadoRepository;
import br.com.curso.repositories.ProdutoRepository;

@SpringBootApplication
public class CursoSpringBootApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ClienteRepository clienteRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(CursoSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritorio");
		
		Produto p1 = new Produto(null, "Computador",2000.00 );
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.0);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1,cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
		
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2 , c3));
		
		estadoRepository.saveAll(Arrays.asList(est1 , est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "123456985-78", TipoCliente.PESSOAFISICA);
		
		cli1.getTelefones().addAll(Arrays.asList("21 949384-2323", "11 93894-3234"));
		
		Endereco e1 = new Endereco(null, "Rua tenerife", "1460", "apt 500", "Jardim", "03302-929", cli1, c1);
		Endereco e2 = new Endereco(null, "Rua matos", "34", null, "América", "03328-212", cli1, c2);
		
		cli1.getEnderecos().addAll(Arrays.asList(e1,e2));
		
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
	}
}
