package com.fleur.cinemate.genre;

import com.fleur.cinemate.__shared.exception.BadRequestException;
import com.fleur.cinemate.genre.dto.CreateGenreDto;
import com.fleur.cinemate.genre.dto.GenreDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    public GenreDto createGenre(CreateGenreDto createGenreDto) {
        genreRepository.findByName(createGenreDto.name())
                .ifPresent(genre -> {
                    throw new BadRequestException("Genre already exists");
                });

        Genre genre = genreMapper.toEntity(createGenreDto);

        return genreMapper.toDto(genreRepository.save(genre));
    }

    @Transactional(readOnly = true)
    public Page<GenreDto> findAllGenres(Pageable pageable) {
        return genreRepository.findAll(pageable)
                .map(genreMapper::toDto);
    }

    @Transactional(readOnly = true)
    public GenreDto findGenreById(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));

        return genreMapper.toDto(genre);
    }

    @Transactional(readOnly = true)
    public Genre findByName(String name) {
        return  genreRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
    }

    @Transactional(readOnly = true)
    public Genre findGenreEntityById(Long genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
    }

    @Transactional
    public void deleteGenre(Long genreId) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));

        genreRepository.delete(genre);
    }

}
