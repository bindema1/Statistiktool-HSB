package belegung.db;

import org.springframework.data.jpa.repository.JpaRepository;

import belegung.model.Carrels;

public interface CarrelsDatenbank extends JpaRepository<Carrels, Long> {
	
    
    
}
