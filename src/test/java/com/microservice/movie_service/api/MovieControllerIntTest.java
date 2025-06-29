package com.microservice.movie_service.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.movie_service.model.Movie;
import com.microservice.movie_service.repo.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerIntTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MovieRepository movieRepository;
@BeforeEach
    void cleanUp(){
    movieRepository.deleteAllInBatch();
    }

    @Test
void givenMovie_whenCreateMovie_thenReturnSavedMovie() throws Exception{
    //Given
        Movie movie=new Movie();
    movie.setName("Genesis");
    movie.setDirector("Holy God");
    movie.setActors(List.of("Holy God","Adam","Eve"));
    //when create a movie
var response=
                mockMvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(movie)));
//then verify saved movie

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(notNullValue())))
                .andExpect(jsonPath("$.name",is(movie.getName())))
                .andExpect(jsonPath("$.director",is(movie.getDirector())))
                .andExpect(jsonPath("$.actors",is(movie.getActors())))
        ;

    }


    @Test
    void givenMovieId_whenFetchMovie_thenReturnMovie () throws Exception {
// Given
        Movie movie=new Movie();
        movie.setName("Genesis");
        movie.setDirector("Holy God");
        movie.setActors(List.of("Holy God","Adam","Eve"));

       Movie savedMovie= movieRepository.save(movie);
        // When
        var response  = mockMvc.perform(get( "/movies/" + savedMovie.getId()));
//Then verify saved movie
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id",is(savedMovie.getId().intValue())))
                .andExpect(jsonPath("$.name",is(movie.getName())))
                .andExpect(jsonPath("$.director",is(movie.getDirector())))
                .andExpect(jsonPath("$.actors",is(movie.getActors())))
        ;


    }

    @Test
void givenSavedMovie_WhenUpdateMovie_thenMovieUpdatedInDb()  throws Exception{
            //
        Movie movie=new Movie();
        movie.setName("Genesis");
        movie.setDirector("Holy God");
        movie.setActors(List.of("Holy God","Adam","Eve"));

        Movie savedMovie= movieRepository.save(movie);
        Long id=savedMovie.getId();

        //update movie
        movie.setActors(List.of("Holy God","Adam","Eve","Saturn"));
        var response=
                mockMvc.perform(put("/movies/"+id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(movie)));
//Then verify saved movie
        response.andDo(print())
                .andExpect(status().isOk());

        var fetchResponse  = mockMvc.perform(get( "/movies/" + id));
        fetchResponse.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(movie.getName())))
                .andExpect(jsonPath("$.director",is(movie.getDirector())))
                .andExpect(jsonPath("$.actors",is(movie.getActors())))
        ;
    }
    @Test
    void givenMovie_whenDeleteRequest_thenMovieRemovedFromDb() throws Exception {
// Given
        Movie movie = new Movie();
        movie.setName("Genesis");
        movie.setDirector("Holy God");
        movie.setActors(List.of("Holy God","Adam","Eve"));

        Movie savedMovie = movieRepository.save(movie);
        Long id = savedMovie.getId();
//Then
        mockMvc.perform(delete("/movies/" + id))
.andDo(print())
                .andExpect(status().isOk());
        assertFalse(movieRepository.findById(id).isPresent());

    }


}