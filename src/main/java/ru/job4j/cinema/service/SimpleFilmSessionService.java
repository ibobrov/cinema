package ru.job4j.cinema.service;

import org.springframework.stereotype.Service;
import ru.job4j.cinema.common.RelationIdException;
import ru.job4j.cinema.dto.DtoFilmSession;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.HallRepository;
import ru.job4j.cinema.repository.SessionRepository;

import java.time.LocalDate;
import java.util.*;

import static java.util.Optional.empty;

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
        try {
            return sessionOpt.isPresent() ? Optional.of(toDto(sessionOpt.get())) : empty();
        } catch (RelationIdException e) {
            e.printStackTrace();
            return empty();
        }
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
        List<DtoFilmSession> rsl = new ArrayList<>();
        for (var session : filmSessions) {
            try {
                rsl.add(toDto(session));
            } catch (RelationIdException e) {
                e.printStackTrace();
            }
        }
        return rsl;
    }

    private DtoFilmSession toDto(FilmSession session) throws RelationIdException {
        var film = filmRepo.findById(session.getFilmId());
        var hall = hallRepo.findById(session.getHallId());
        if (film.isEmpty() || hall.isEmpty()) {
            var msg = String.format("No link found when converting FilmSession to DtoFilmSession. "
                                    + "FilmSession id = %s, Film id = %s, Hall id = %s.",
                                    session.getId(), session.getFilmId(), session.getHallId());
            throw new RelationIdException(msg);
        }
        return new DtoFilmSession(session.getId(), film.get(), hall.get(),
                        session.getStartTime(), session.getEndTime(), session.getPrice());

    }
}
