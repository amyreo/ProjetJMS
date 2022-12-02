package com.inti.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inti.model.Banque;

public interface IBanqueRepository extends JpaRepository<Banque, Long> {

}
