package org.example.parkinglot.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "car_photo")
public class CarPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "filename")
    private String filename;

    @Column(name = "file_type")
    private String fileType;

    // 1. Câmpul pentru conținutul fișierului (adăugat conform cerinței)
    @Column(name = "file_content")
    private byte[] fileContent;

    // 2. Asocierea OneToOne cu entitatea Car (adăugată conform cerinței)
    @OneToOne
    private Car car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}