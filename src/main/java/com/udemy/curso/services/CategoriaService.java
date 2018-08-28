 package com.udemy.curso.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.curso.domain.Categoria;
import com.udemy.curso.repositories.CategoriaRepository;
import com.udemy.curso.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria buscar(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: "+ id 
				+ ", Tipo: " + Categoria.class.getName()));
		
	}
	
	public Categoria insert(Categoria obj) {
		return repo.save(obj);
	}
	
}
