package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository {
    Item createItem(Item item, int userId);

    Item getItemById(int itemId);

    Item updateItem(ItemDto itemDto, int ownerId, int itemId);

    List<ItemDto> getItemsByText(String searchText);

    Collection<ItemDto> getItemsByUserId(int userId);
}
