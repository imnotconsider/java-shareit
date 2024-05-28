package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, int userId);

    ItemDto updateItem(ItemDto itemDto, int userId, int itemId);

    ItemDto getItemById(int itemId);

    List<ItemDto> getItemsByText(String searchText);

    Collection<ItemDto> getItemsByUserId(int userId);
}