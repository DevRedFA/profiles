package com.isedykh.profiles.service;

import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.dao.repository.PersonEntityRepository;
import com.isedykh.profiles.mapper.ClientMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private PersonEntityRepository personEntityRepository;

    private ClientMapper clientMapper;

    @Override
    public List<Client> findAll() {
        return clientMapper.personEntitiesToPersons(personEntityRepository.findAll());
    }

    @Override
    public List<Client> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Client savePerson(Client client) {
        ClientEntity save = personEntityRepository.save(clientMapper.personToPersonEntity(client));
        return clientMapper.personEntityToPerson(save);
    }

    @Override
    public Client updatePerson(Client client) {
        ClientEntity save = personEntityRepository.save(clientMapper.personToPersonEntity(client));
        return clientMapper.personEntityToPerson(save);
    }

    @Override
    public void deletePerson(Client client) {
        personEntityRepository.delete(clientMapper.personToPersonEntity(client));
    }

    @Override
    public List<Client> findPersonByName(String name) {
        List<ClientEntity> allByName = personEntityRepository.findAllByName(name);
        return clientMapper.personEntitiesToPersons(allByName);
    }

    @Override
    public List<Client> findPersonByPhone(long phone) {
        List<ClientEntity> allByPhone = personEntityRepository.findAllByPhoneOrPhoneSecond(phone, phone);
        return clientMapper.personEntitiesToPersons(allByPhone);
    }

    @Override
    public Client findById(long id) {
        return clientMapper.personEntityToPerson(personEntityRepository.getOne(id));
    }
}
