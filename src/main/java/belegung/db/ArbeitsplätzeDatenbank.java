package belegung.db;

import org.springframework.data.jpa.repository.JpaRepository;

import belegung.model.Arbeitsplätze;

public interface ArbeitsplätzeDatenbank extends JpaRepository<Arbeitsplätze, Long> {
	
    
    
}
