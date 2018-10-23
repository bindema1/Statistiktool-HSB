package belegung.db;

import org.springframework.data.jpa.repository.JpaRepository;

import belegung.model.Stockwerk;

public interface StockwerkDatenbank extends JpaRepository<Stockwerk, Long> {
	
    
    
}
