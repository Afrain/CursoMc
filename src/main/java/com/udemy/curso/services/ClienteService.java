package com.udemy.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.udemy.curso.domain.Cidade;
import com.udemy.curso.domain.Cliente;
import com.udemy.curso.domain.Endereco;
import com.udemy.curso.domain.enums.TipoCliente;
import com.udemy.curso.dto.ClienteDTO;
import com.udemy.curso.dto.ClienteNewDTO;
import com.udemy.curso.repositories.CidadeRepository;
import com.udemy.curso.repositories.ClienteRepository;
import com.udemy.curso.repositories.EnderecoRepository;
import com.udemy.curso.services.exceptions.DataIntegrityException;
import com.udemy.curso.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));

	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = clienteRepository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}

	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateObjCliente(newObj, obj);
		return clienteRepository.save(newObj);
	}

	public void delete(Cliente obj) {
		find(obj.getId());
		try {
			clienteRepository.delete(obj);
		}catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possivel excluir um cliente com outras tabelas vinculadas a ele!");
		}
	}
	
	public List<Cliente> findAll(){
		return clienteRepository.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail()
				, null, null);
	}
	
	public Cliente fromNewObjDTO(ClienteNewDTO objNewDTO) {
		Cliente cli = new Cliente(null, objNewDTO.getNome(), objNewDTO.getEmail(), objNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(objNewDTO.getTipo()));
		Cidade cid = cidadeRepository.findAll().get(objNewDTO.getCidadeId());
		Endereco end = new Endereco(null, objNewDTO.getLogradouro(), objNewDTO.getNumero(), objNewDTO.getComplemento(), 
				objNewDTO.getBairro(), objNewDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objNewDTO.getTelefone1());
		if(objNewDTO.getTelefone2() != null) {
			cli.getTelefones().add(objNewDTO.getTelefone2());
		}
		if(objNewDTO.getTelefone3() != null) {
			cli.getTelefones().add(objNewDTO.getTelefone3());
		}
		
		return cli;
	}

	private void updateObjCliente(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
