package com.isedykh.profiles.mapper;

import com.isedykh.profiles.dao.entity.OrderEntity;
import com.isedykh.profiles.dao.entity.ClientEntity;
import com.isedykh.profiles.service.Client;
import com.isedykh.profiles.service.Order;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import org.mapstruct.Mapper;

import java.sql.Timestamp;
import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    Client clientEntityToClient(ClientEntity clientEntity);

    ClientEntity clientToClientEntity(Client client);

    List<Client> clientEntitiesToClients(List<ClientEntity> clientEntities);

    List<ClientEntity> clientsToClientEntities(List<Client> clients);

    @Mapping(target = "client", ignore = true)
    Order orderEntityToOrder(OrderEntity orderEntity);

    default java.sql.Timestamp map(java.time.LocalDate value) {
        return Timestamp.valueOf(value.atStartOfDay());
    }
}
