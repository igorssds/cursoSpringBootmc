package br.com.curso.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.curso.domain.Cidade;
import br.com.curso.domain.Cliente;
import br.com.curso.domain.Endereco;
import br.com.curso.domain.enums.TipoCliente;
import br.com.curso.dto.ClienteDTO;
import br.com.curso.dto.ClienteNewDTO;
import br.com.curso.repositories.ClienteRepository;
import br.com.curso.repositories.EnderecoRepository;
import br.com.curso.services.exceptions.DateIntegrityException;
import br.com.curso.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id) {
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado Id : " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		
		cliente = repo.save(cliente);
		
		enderecoRepository.saveAll(cliente.getEnderecos());
		
		return cliente;
	}
	
	public Cliente update(Cliente cliente) {
		Cliente newObj = find(cliente.getId());
		
		updateData(newObj, cliente);
		
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);	
		}catch (DataIntegrityViolationException e) {
			throw new DateIntegrityException("Não é possivel excluir porque há entidades relacionadas");
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
		}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome() , clienteDTO.getEmail(), null, null);
	}
	
	@Transactional
	public Cliente fromDTO(ClienteNewDTO clienteDTO) {
		Cliente cli = new Cliente(null, clienteDTO.getNome(), clienteDTO.getEmail(), clienteDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteDTO.getTipo()));
		Cidade cidade = new Cidade(clienteDTO.getCidadeId() , null , null);
		Endereco end = new Endereco(null, clienteDTO.getLogradouro(), clienteDTO.getNumero(), clienteDTO.getComplemento(), clienteDTO.getBairro(), clienteDTO.getCep(), cli, cidade);
	
		cli.getEnderecos().add(end);
		cli.getTelefones().add(clienteDTO.getTelefone1());
		
		if(clienteDTO.getTelefone2() != null) cli.getTelefones().add(clienteDTO.getTelefone2());
		
		if(clienteDTO.getTelefone3() != null ) cli.getTelefones().add(clienteDTO.getTelefone3());
		
		return cli;
		
	}
	
	private void updateData(Cliente newObj , Cliente obj) {
		
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}
			
			
}
