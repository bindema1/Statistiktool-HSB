package belegung.db;

import org.springframework.data.jpa.repository.JpaRepository;

import belegung.model.Kapazität;

public interface KapazitätDatenbank extends JpaRepository<Kapazität, Long> {
	
    
    
}
