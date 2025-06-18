package com.microservice.movie_service.service;

import com.microservice.movie_service.exception.InvalidDataException;
import com.microservice.movie_service.exception.NotFoundException;
import com.microservice.movie_service.model.Movie;
import com.microservice.movie_service.repo.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // CRUD operation - Creat, Read, Update, Delete

    public Movie create(Movie movie) {

        if (movie == null) {
            throw new InvalidDataException("Invalid movie: null");
        }

        return movieRepository.save(movie);
    }

    public Movie read(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));
    }

    public void update(Long id, Movie update) {
        if (update == null || id == null) {
            throw new InvalidDataException("Invalid movie: null");
        }

        // Check if exists
        if (movieRepository.existsById(id)) {
            Movie movie = movieRepository.getReferenceById(id);
            movie.setName(update.getName());
            movie.setDirector(update.getDirector());
            movie.setActors(update.getActors());
            movieRepository.save(movie);
        } else {
            throw new NotFoundException("Movie not found with id: " + id);
        }
    }

    public void delete(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
        } else {
            throw new NotFoundException("Movie not found with id: " + id);
        }
    }
}