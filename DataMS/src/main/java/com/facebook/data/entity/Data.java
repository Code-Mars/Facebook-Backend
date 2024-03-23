package com.facebook.data.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "data")
public class Data {
    
    @Id
    private String id;
    private String location;
    private String fileName;
    private LocalDate lastUsedOn;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public LocalDate getLastUsedOn() {
        return lastUsedOn;
    }
    public void setLastUsedOn(LocalDate lastUsedOn) {
        this.lastUsedOn = lastUsedOn;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((lastUsedOn == null) ? 0 : lastUsedOn.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Data other = (Data) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        if (lastUsedOn == null) {
            if (other.lastUsedOn != null)
                return false;
        } else if (!lastUsedOn.equals(other.lastUsedOn))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Data [id=" + id + ", location=" + location + ", fileName=" + fileName + ", lastUsedOn=" + lastUsedOn
                + "]";
    }

    
      
}
