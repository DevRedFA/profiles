package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.service.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    Client clientEntityToClient(ClientEntity clientEntity);

    ClientEntity clientToClientEntity(Client client);

    List<Client> clientEntitiesToClients(List<ClientEntity> clientEntities);

    List<ClientEntity> clientsToClientEntities(List<Client> clients);

    default java.sql.Timestamp map(java.time.LocalDate value) {
        return Timestamp.valueOf(value.atStartOfDay());
    }
}
