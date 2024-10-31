package com.ics.interfaces.dto;

// Marker interface for DTOs that can be converted to entities
public interface IEntityDTO<T,K> {
    T toEntity(K additionalData);
}
