package net.javierjimenez.Repositories;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import net.javierjimenez.Models.Carrito;

public interface CarritoRepositori extends PagingAndSortingRepository<Carrito, String> {

	public List<Carrito> findByUsername(String username);
	
    public long countByUsername(String username);
    
    public Carrito findByIdAndUsername(String id, String username);
    
    public Carrito findById(String id);

}
