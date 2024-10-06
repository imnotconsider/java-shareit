package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemValidationException;
import ru.practicum.shareit.exception.UserNotEnoughRights;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository repository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
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
        item.setOwner(UserMapper.dtoToUser(userService.getUserById(userId)));
        repository.save(item);
        return ItemMapper.itemToDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, long ownerId, long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("item with id=%d not found", itemId)));

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

        repository.updateItem(itemId, itemDto.getName(), itemDto.getDescription(), Boolean.TRUE.equals(itemDto.getAvailable()));
        return ItemMapper.itemToDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = repository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(String.format("item with id=%d not found", itemId)));
        List<Comment> comments = commentRepository.findAllCommentsByItem(item);
        return ItemMapper.itemToDto(item, null, null, comments);
    }

    @Override
    public List<ItemDto> getItemsByText(String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return new ArrayList<>();
        }

        return repository.findAllByDescriptionIgnoreCaseOrNameContainingIgnoreCase(searchText, searchText).stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        User user = UserMapper.dtoToUser(userService.getUserById(userId));
        user.setId(userId);

        return repository.findAllByOwner(user).stream()
                .map(ItemMapper::itemToDto)
                .collect(Collectors.toList());
    }
}
