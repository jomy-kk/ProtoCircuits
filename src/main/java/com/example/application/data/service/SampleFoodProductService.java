package com.example.application.data.service;

import com.example.application.data.entity.SampleFoodProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import javax.persistence.Lob;

@Service
public class SampleFoodProductService extends CrudService<SampleFoodProduct, Integer> {

    private SampleFoodProductRepository repository;

    public SampleFoodProductService(@Autowired SampleFoodProductRepository repository) {
        this.repository = repository;
    }

    @Override
    protected SampleFoodProductRepository getRepository() {
        return repository;
    }

}
