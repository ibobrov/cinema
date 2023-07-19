package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SimpleFilmSessionService implements FilmSessionService {
    private final SessionRepository sessionRepo;
    private final FilmRepository filmRepo;
    private final HallRepository hallRepo;

    public SimpleFilmSessionService(SessionRepository sessionRepo, FilmRepository filmRepo, HallRepository hallRepo) {
        this.sessionRepo = sessionRepo;
        this.filmRepo = filmRepo;
        this.hallRepo = hallRepo;
    }

    @Override
    public List<DtoFilmSession> getByDay(LocalDate date) {
        var sessions = sessionRepo.findByDay(date);
        return toDto(sessions);
    }

    @Override
    public List<DtoFilmSession> getByFilm(int id) {
        var sessions = sessionRepo.findByFilm(id);
        return toDto(sessions);
    }

    private List<DtoFilmSession> toDto(Collection<FilmSession> filmSessions) {
        List<DtoFilmSession> rsl = new ArrayList<>();
        for (var session : filmSessions) {
            var dto = new DtoFilmSession(
                    session.getId(),
                    filmRepo.findById(session.getFilmId()).get(),
                    hallRepo.findById(session.getHallId()).get(),
                    session.getStartTime(),
                    session.getEndTime(),
                    session.getPrice()
            );
            rsl.add(dto);
        }
        return rsl;
    }
}
