package com.fleur.cinemate.search.dto;

public record CollectionFilter(
        String query,
        CollectionSortBy sortBy
) {
}
