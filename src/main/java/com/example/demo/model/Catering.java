package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Catering {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String area;
    private String type;
    private String phone;
    private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "catering", cascade = CascadeType.ALL)
    private List<CateringRecommend> cateringRecommends;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<CateringRecommend> getCateringRecommends() {
        return cateringRecommends;
    }

    public void setCateringRecommends(List<CateringRecommend> cateringRecommends) {
        this.cateringRecommends = cateringRecommends;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
