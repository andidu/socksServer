package com.andidu.socks_server;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Transactional
@Repository
public class SocksDao {
    @PersistenceContext
    private EntityManager entityManager;

    public void addSocks(Socks sock) {
        //TODO: check corner cases
        List<SocksDB> socksDBS = getSocks(sock.getColor(), sock.getCottonPart());
        if (socksDBS == null || socksDBS.isEmpty()) {
            Query query = entityManager.createNativeQuery("INSERT into SocksDB (color, cotton_part, quantity) VALUES (?1, ?2, ?3)")
                    .setParameter(1, sock.getColor())
                    .setParameter(2, sock.getCottonPart())
                    .setParameter(3, sock.getQuantity());
            query.executeUpdate();
        } else {
            SocksDB socksDB = socksDBS.get(0);
            entityManager.createNativeQuery("UPDATE socksdb set quantity = ?1 where id = ?2")
                    .setParameter(1, sock.getQuantity() + socksDB.getQuantity()).setParameter(2, socksDB.getId()).executeUpdate();
        }
    }

    public void subtractSocks(Socks sock) {
        //TODO: check corner cases
        addSocks(sock);
    }

    public List<SocksDB> getSocks(String color, Integer cottonPart) {
        //TODO: check corner cases
        return entityManager.createQuery("from SocksDB where color = ?1 and cotton_part = ?2", SocksDB.class).
                setParameter(1, color).setParameter(2, cottonPart).getResultList();
    }

    public List<SocksDB> getSocks(String color, String operation, Integer cottonPart) {
        //TODO: check corner cases
        switch (operation) {
            case "moreThan":
                return entityManager.createQuery("from SocksDB where color = ?1 and cotton_part > ?2", SocksDB.class).
                        setParameter(1, color).setParameter(2, cottonPart).getResultList();
            case "lessThan":
                return entityManager.createQuery("from SocksDB where color = ?1 and cotton_part < ?2", SocksDB.class).
                        setParameter(1, color).setParameter(2, cottonPart).getResultList();
            case "equal":
                return getSocks(color, cottonPart);
        }
        return null;
    }
}
