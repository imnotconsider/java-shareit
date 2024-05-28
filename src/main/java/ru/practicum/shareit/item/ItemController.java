package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.Min;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Log4j2
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping()
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @RequestBody ItemDto itemDto) {
        ItemDto item = itemService.createItem(itemDto, userId);
        log.info("{} added by user-{}", item, userId);
        return item;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Min(value = 1, message = "id cannot be < 1") @RequestHeader("X-Sharer-User-Id") int ownerId,
                              @RequestBody ItemDto itemDto,
                              @Min(value = 1, message = "id cannot be < 1") @PathVariable("itemId") int itemId) {
        ItemDto item = itemService.updateItem(itemDto, ownerId, itemId);
        log.info("item-{} updated by user-{} to {}", itemId, ownerId, item);
        return item;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") int itemId) {
        ItemDto item = itemService.getItemById(itemId);
        log.info("item-{} requested: {}", itemId, item);
        return item;
    }

    @GetMapping
    public Collection<ItemDto> getItemsByUserId(@Min(value = 1, message = "id cannot be < 1") @RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("item list of user-{} requested.", userId);
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsByText(@RequestParam("text") String searchText) {
        log.info("item list requested with text: {}", searchText);
        return itemService.getItemsByText(searchText);
    }
}
