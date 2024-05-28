package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    @Override
    public ItemDto createItem(ItemDto itemDto, int userId) {
        if (itemDto.getAvailable() == null) {
            throw new ItemValidationException("available cannot be null");
        }
        if (!StringUtils.hasText(itemDto.getName())) {
            throw new ItemValidationException("name cannot be empty");
        }
        if (!StringUtils.hasText(itemDto.getDescription())) {
            throw new ItemValidationException("description cannot be empty");
        }

        Item item = ItemMapper.dtoToItem(itemDto);
        return ItemMapper.itemToDto(repository.createItem(item, userId));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int ownerId, int itemId) {
        return ItemMapper.itemToDto(repository.updateItem(itemDto, ownerId, itemId));
    }

    @Override
    public ItemDto getItemById(int itemId) {
        return ItemMapper.itemToDto(repository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getItemsByText(String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return new ArrayList<>();
        }

        return repository.getItemsByText(searchText);
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(int userId) {
        return repository.getItemsByUserId(userId);
    }
}
