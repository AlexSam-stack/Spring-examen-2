package com.examen.codigo.repository;

import com.examen.codigo.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Long> {
    Optional<Cliente> findByDni(String dni);

    boolean existsByDni(String dni);

    boolean existsByCorreo(String correo);
}
