package com.keshav.repository;

import com.keshav.model.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchListRepo extends JpaRepository<WatchList, Long> {

    WatchList findByUserId(Long userId);

}
