package br.com.drs.bancosangue.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public abstract class GenericService<T, ID> {

    @Autowired
    private JpaRepository<T, ID> repository;

    public T salvar(T entidade) {
        return repository.save(entidade);
    }

    public List<T> listarTodos() {
        return repository.findAll();
    }

    public Optional<T> buscarPorId(ID id) {
        return repository.findById(id);
    }

    public T atualizar(T entidade) {
        return repository.save(entidade);
    }

    public void deletarPorId(ID id) {
        repository.deleteById(id);
    }
}