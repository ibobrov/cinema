package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.dto.DtoFile;
import ru.job4j.cinema.service.FileService;

import java.io.IOException;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The base behavior is checked. Upon request, a service finds
 * the file and transfer data byte by byte.
 */
class FileControllerTest {
    private final FileService fileService = mock(FileService.class);
    private final FileController fileController = new FileController(fileService);
    private final MultipartFile testFile = new MockMultipartFile("testFile.img", new byte[] {1, 2, 3});

    @Test
    public void whenGetFileByIdThenReturnOK() throws IOException {
        var fileDto = new DtoFile(testFile.getOriginalFilename(), testFile.getBytes());
        when(fileService.getFileById(1)).thenReturn(Optional.of(fileDto));
        var entity = fileController.getById(1);
        assertThat(entity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    public void whenGetFileByIdThenReturnNotFound() {
        when(fileService.getFileById(1)).thenReturn(empty());
        var entity = fileController.getById(1);
        assertThat(entity.getStatusCodeValue()).isEqualTo(404);
    }
}