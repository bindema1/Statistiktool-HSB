package belegung.db;

import belegung.model.Belegung;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BelegungDatenbank extends JpaRepository<Belegung, Long> {
	
    
    
}
