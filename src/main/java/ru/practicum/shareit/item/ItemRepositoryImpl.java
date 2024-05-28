package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotEnoughRights;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private final AtomicInteger id = new AtomicInteger(0);
    private final UserRepository userRepository;

    @Override
    public Item createItem(Item item, int userId) {
        if (userRepository.getUserById(userId) == null) {
            throw new UserNotFoundException(String.format("user with id=%d not found", userId));
        }

        item.setId(id.incrementAndGet());
        item.setOwnerId(userId);
        items.put(id.get(), item);
        return item;
    }

    @Override
    public Item getItemById(int itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new ItemNotFoundException(String.format("item with id=%d not found", itemId));
        }

        return item;
    }

    @Override
    public Item updateItem(ItemDto itemDto, int ownerId, int itemId) {
        Item item = getItemById(itemId);
        if (item.getOwnerId() != ownerId) {
            throw new UserNotEnoughRights("invalid owner id");
        }

        if (StringUtils.hasText(itemDto.getName())) {
            item.setName(itemDto.getName());
        }

        if (StringUtils.hasText(itemDto.getDescription())) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        items.put(itemId, item);
        return items.get(itemId);
    }

    @Override
    public List<ItemDto> getItemsByText(String text) {
        List<ItemDto> availableItems = new ArrayList<>();
        items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .forEach(item -> availableItems.add(ItemMapper.itemToDto(item)));

        return availableItems;
    }

    @Override
    public List<ItemDto> getItemsByUserId(int userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
    }
}
