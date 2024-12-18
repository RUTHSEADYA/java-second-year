package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;



    @Entity
    public class Photographer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        private String description;
        private String position;
        private int yearsOfExperience;
        private String phone;
        private String imageUrl;

        @JsonIgnore
        @OneToMany(mappedBy = "photographer", cascade = CascadeType.ALL)
        private List<PhotoRecommend> photoRecommend;

        public Photographer() {}

        public Photographer(Long id, String name, String description, String position, int yearsOfExperience, String phone, String imageUrl, List<PhotoRecommend> photoRecommend) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.position = position;
            this.yearsOfExperience = yearsOfExperience;
            this.phone = phone;
            this.imageUrl = imageUrl;
            this.photoRecommend = photoRecommend;
        }
// רשימת ההמלצות

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getYearsOfExperience() {
            return yearsOfExperience;
        }

        public void setYearsOfExperience(int yearsOfExperience) {
            this.yearsOfExperience = yearsOfExperience;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public List<PhotoRecommend> getRecommendPhoto() {
            return photoRecommend;
        }

        public void setRecommendPhoto(List<PhotoRecommend> photoRecommend) {
            this.photoRecommend = photoRecommend;
        }
    }


