package org.project.autoserviceapp.client;

public class getService {

    private Integer id;
    private String serviceName;
    private Integer storageId;
    private String serviceDeadlines;
    private Double servicePrice;
    private Integer workerId;
    private String image;

    // Конструктор для создания объекта услуги
    public getService(Integer id, String serviceName, Integer storageId, String serviceDeadlines, Double servicePrice, String image) {
        this.id = id;
        this.serviceName = serviceName;
        this.storageId = storageId;
        this.serviceDeadlines = serviceDeadlines;
        this.servicePrice = servicePrice;
        this.image = image;
    }

    // Геттеры
    public Integer getId(){
        return id;
    }

    public String getServiceName(){
        return serviceName;
    }

    public Integer getStorageId(){
        return storageId;
    }

    public String getServiceDeadlines(){
        return serviceDeadlines;
    }

    public Double getServicePrice(){
        return servicePrice;
    }

    public Integer getWorkerId(){
        return workerId;
    }

    public String getImage(){
        return image;
    }
}