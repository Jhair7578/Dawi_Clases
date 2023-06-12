package com.clinica.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.clinica.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

	//select tm from TipoMedicamento tm where tm.laboratorio.codigo=?1
	//select * from tb_cliente where ape_cli like 'M%';
	//@Query("select c from Cliente c where c.paterno like ?1%")
	//public List<Cliente> listarClientesPorApellido(String ape);
	
	public List<Cliente> findByPaternoStartingWith(String ape);
}
