package com.isedykh.profiles.service.impl;

import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.mapper.ClientMapper;
import com.isedykh.profiles.service.ClientService;
import com.isedykh.profiles.service.entity.Client;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private ClientEntityRepository clientEntityRepository;

    private ClientMapper clientMapper;

    @Override
    public List<Client> findAll() {
        return clientMapper.clientEntitiesToClients(clientEntityRepository.findAll());
    }

    @Override
    public Page<Client> findAll(Pageable pageable) {
        Page<ClientEntity> all = clientEntityRepository.findAll(pageable);
        List<Client> clients = clientMapper.clientEntitiesToClients(all.getContent());
        return new PageImpl<>(clients, all.getPageable(), all.getTotalElements());
    }

    @Override
    public void delete(Client client) {
        ClientEntity entity = clientMapper.clientToClientEntity(client);
        clientEntityRepository.delete(entity);
    }

    @Override
    public void delete(long id) {
        clientEntityRepository.deleteById(id);
    }

    @Override
    public Client save(Client client) {
        ClientEntity entity = clientMapper.clientToClientEntity(client);
        ClientEntity save = clientEntityRepository.save(entity);
        return clientMapper.clientEntityToClient(save);
    }

    @Override
    public List<Client> findClientByName(String name) {
        List<ClientEntity> allByName = clientEntityRepository.findAllByNameContainingIgnoreCase(name);
        return clientMapper.clientEntitiesToClients(allByName);
    }

    @Override
    public Client findById(long id) {
        ClientEntity one = clientEntityRepository.getOne(id);
        return clientMapper.clientEntityToClient(one);
    }
}
