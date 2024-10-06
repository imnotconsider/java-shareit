package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Modifying
    @Query("UPDATE Item i SET i.name = :name, i.description = :description, i.available = :available WHERE i.id = :itemId")
    void updateItem(@Param("itemId") long itemId, @Param("name") String name, @Param("description") String description, @Param("available") boolean available);

    List<Item> findAllByOwner(User owner);

    List<Item> findAllByDescriptionIgnoreCaseOrNameContainingIgnoreCase(String description, String name);
}
