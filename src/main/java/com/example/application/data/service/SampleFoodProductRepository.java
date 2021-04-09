package com.example.application.data.service;

import com.example.application.data.entity.SampleFoodProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import javax.persistence.Lob;

public interface SampleFoodProductRepository extends JpaRepository<SampleFoodProduct, Integer> {

}