package com.inti.service;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inti.model.Compte;

public interface ICompteRepository extends JpaRepository<Compte, Long> {

}
