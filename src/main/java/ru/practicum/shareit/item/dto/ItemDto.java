package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * TODO Sprint add-controllers.
 */

@Data
@Builder
@AllArgsConstructor
public class ItemDto {
    @Min(value = 1, message = "id cannot be < 1")
    private int id;
    private String name;
    private String description;
    private Boolean available;
}
