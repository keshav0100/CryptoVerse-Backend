package com.keshav.repository;

import com.keshav.model.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepo extends JpaRepository<Coin, String> {

}
