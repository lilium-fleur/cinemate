package com.fleur.cinemate.search.service;

import com.fleur.cinemate.search.repository.CollectionDocumentRepository;
import com.fleur.cinemate.search.repository.FilmDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ESDeleteService {

    private final FilmDocumentRepository filmDocumentRepository;
    private final CollectionDocumentRepository collectionDocumentRepository;

    public void deleteByEntityName(String name, Long documentId){
        switch (name.toLowerCase()){
            case "film":
                filmDocumentRepository.deleteById(documentId);
                break;
            case "collection":
                collectionDocumentRepository.deleteById(documentId);
                break;
            default:
                log.warn("elastic search index for entity with name {} not found", name);
                throw new RuntimeException("elastic search index for entity with name " + name + " not found");
        }
    }
}
