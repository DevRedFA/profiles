package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.dao.repository.ClientEntityRepository;
import com.isedykh.profiles.mapper.ClientMapper;
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
    public Client save(Client client) {
        ClientEntity entity = clientMapper.clientToClientEntity(client);
        ClientEntity save = clientEntityRepository.save(entity);
        return clientMapper.clientEntityToClient(save);
    }

    @Override
    public Client update(Client client) {
        ClientEntity save = clientEntityRepository.save(clientMapper.clientToClientEntity(client));
        return clientMapper.clientEntityToClient(save);
    }

    @Override
    public List<Client> findClientByName(String name) {
        List<ClientEntity> allByName = clientEntityRepository.findAllByName(name);
        return clientMapper.clientEntitiesToClients(allByName);
    }

    @Override
    public List<Client> findClientByPhone(long phone) {
        List<ClientEntity> allByPhone = clientEntityRepository.findAllByPhoneOrPhoneSecond(phone, phone);
        return clientMapper.clientEntitiesToClients(allByPhone);
    }

    @Override
    public Client findById(long id) {
        return clientMapper.clientEntityToClient(clientEntityRepository.getOne(id));
    }
}
