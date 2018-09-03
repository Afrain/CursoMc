package com.udemy.curso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.udemy.curso.domain.Cliente;
import com.udemy.curso.dto.ClienteDTO;
import com.udemy.curso.repositories.ClienteRepository;
import com.udemy.curso.services.exceptions.DataIntegrityException;
import com.udemy.curso.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));

	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateObjCliente(newObj, obj);
		return repo.save(newObj);
	}

	public void delete(Cliente obj) {
		find(obj.getId());
		try {
		repo.delete(obj);
		}catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possivel excluir um cliente com outras tabelas vinculadas a ele!");
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail()
				, null, null);
	}

	private void updateObjCliente(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
