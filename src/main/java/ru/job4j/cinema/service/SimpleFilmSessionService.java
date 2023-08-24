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
import java.util.Optional;

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
    public Optional<DtoFilmSession> findById(int id) {
        var sessionOpt = sessionRepo.findById(id);
        return sessionOpt.map(this::toDto);
    }

    @Override
    public List<DtoFilmSession> findByDay(LocalDate date) {
        var sessions = sessionRepo.findByDay(date);
        return toDto(sessions);
    }

    @Override
    public List<DtoFilmSession> findByFilm(int id) {
        var sessions = sessionRepo.findByFilm(id);
        return toDto(sessions);
    }

    private List<DtoFilmSession> toDto(Collection<FilmSession> filmSessions) {
        var rsl = new ArrayList<DtoFilmSession>();
        for (var session : filmSessions) {
            rsl.add(toDto(session));
        }
        return rsl;
    }

    private DtoFilmSession toDto(FilmSession session) {
        var film = filmRepo.findById(session.getFilmId());
        var hall = hallRepo.findById(session.getHallId());
        if (film.isEmpty() || hall.isEmpty()) {
            var pattern = "No such object by id. FilmSession id = %s, Film id = %s, Hall id = %s.";
            var msg = String.format(pattern, session.getId(),
                                    session.getFilmId(), session.getHallId());
            throw new IllegalStateException(msg);
        }
        return new DtoFilmSession(session.getId(), film.get(), hall.get(),
                        session.getStartTime(), session.getEndTime(), session.getPrice());

    }
}
