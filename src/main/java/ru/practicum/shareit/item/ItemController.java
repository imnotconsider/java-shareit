package ru.practicum.shareit.item;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Log4j2
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping()
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDto) {
        log.info("Item controller | createItem | request: userId({}), itemDto({})", userId, itemDto);
        ItemDto item = itemService.createItem(itemDto, userId);
        log.info("Booking controller | createBooking | answer: created by userId-{} {} ", userId, item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Min(value = 1, message = "id cannot be < 1") @RequestHeader("X-Sharer-User-Id") long ownerId,
                              @RequestBody ItemDto itemDto,
                              @Min(value = 1, message = "id cannot be < 1") @PathVariable("itemId") long itemId) {
        log.info("Item controller | updateItem | request: ownerId({}), itemId({}), itemDto({})", ownerId, itemId, itemDto);
        ItemDto item = itemService.updateItem(itemDto, ownerId, itemId);
        log.info("Item controller | updateItem | answer: itemId-{} succesfully updated", itemId);
        return item;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") long itemId) {
        log.info("Item controller | getItemById | request: itemId({})", itemId);
        ItemDto item = itemService.getItemById(itemId);
        log.info("Item controller | getItemById | answer: {}", item);
        return item;
    }

    @GetMapping
    public List<ItemDto> getItemsByUserId(@Min(value = 1, message = "id cannot be < 1") @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Item controller | getItemsByUserId | request: userId({})", userId);
        List<ItemDto> items = itemService.getItemsByUserId(userId);
        log.info("Item controller | getItemsByUserId | answer: {}", items);
        return items;
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsByText(@RequestParam("text") String searchText) {
        log.info("Item controller | getItemsByText | request: searchText({})", searchText);
        List<ItemDto> items = itemService.getItemsByText(searchText);
        log.info("Item controller | getItemsByText | answer: {}", items);
        return items;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable long itemId,
                                 @RequestBody CommentDto comment,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Item controller | addComment | request: itemId({}), userId({}, comment({}))", itemId, userId, comment);
        CommentDto commentDto = commentService.createComment(itemId, comment, userId);
        log.info("Item controller | addComment | answer: created {}", commentDto);
        return commentDto;
    }
}
