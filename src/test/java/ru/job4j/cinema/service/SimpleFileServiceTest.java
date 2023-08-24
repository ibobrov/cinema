package ru.job4j.cinema.service;

import org.junit.jupiter.api.Test;
import ru.job4j.cinema.repository.FileRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The base behavior is checked. Upon request, the service finds
 * the file and transfers data byte by byte.
 */
class SimpleFileServiceTest {
    private final FileRepository fileRepo = mock(FileRepository.class);
    private final SimpleFileService fileService = new SimpleFileService(fileRepo);

    @Test
    public void whenGetFileByIdWhenReturnCorrectedDto() throws IOException {
        var path = File.createTempFile("file", "txt").getPath();
        try (var writer = new FileWriter(path)) {
            writer.write("str");
        }
        var file = new ru.job4j.cinema.model.File(1, "test", path);
        when(fileRepo.findById(1)).thenReturn(Optional.of(file));
        var fileDto = fileService.getFileById(1).get();
        var strContent = new String(fileDto.getContent());
        assertThat(fileDto.getName()).isEqualTo("test");
        assertThat(strContent).isEqualTo("str");
    }

    @Test
    public void whenGetFileByIdWhenReturnEmpty() {
        var fileDto = fileService.getFileById(1);
        assertThat(fileDto).isEqualTo(empty());
    }
}