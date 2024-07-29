package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.likeservice.LikeService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films/{id}/like/{userId}")
public class LikeController {
    private final LikeService likeService;

    @PutMapping
    public void addLike(@PathVariable Integer id,
                        @PathVariable Integer userId) {
        log.info("Добавление лайка фильму с id {}", id);
        likeService.addLike(id, userId);
    }

    @DeleteMapping
    public void deleteLike(@PathVariable Integer id,
                           @PathVariable Integer userId) {
        log.info("Удаление лайка у фильма с id {}", id);
        likeService.deleteLike(id, userId);
    }

}
