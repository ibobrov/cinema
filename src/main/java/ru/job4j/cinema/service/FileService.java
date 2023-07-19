package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.DtoFile;

import java.util.Optional;

public interface FileService {

    Optional<DtoFile> getFileById(int id);
}
