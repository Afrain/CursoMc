package com.udemy.curso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.udemy.curso.domain.itemPedido;

@Repository
public interface ItemPedidoRepository extends JpaRepository<itemPedido, Integer> {

}
