package ru.job4j.cinema.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.DtoFile;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.repository.Sql2oUserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class SimpleFileService implements FileService {
    private final Logger logger = LoggerFactory.getLogger(Sql2oUserRepository.class);
    private final FileRepository fileRepository;

    public SimpleFileService(FileRepository sql2oFileRepository) {
        this.fileRepository = sql2oFileRepository;
    }

    @Override
    public Optional<DtoFile> getFileById(int id) {
        var fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty()) {
            return Optional.empty();
        }
        var content = readFileAsBytes(fileOptional.get().getPath());
        return Optional.of(new DtoFile(fileOptional.get().getName(), content));
    }

    private byte[] readFileAsBytes(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            logger.trace(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
