package com.devflux.deenone.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "halal_ingredients")
public class HalalFoodEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String code; // e.g. E100, E120, E471
    private String name;
    private String category; // Coloring, Emulsifier, Preservative, etc.
    private String status; // HALAL, HARAM, MUSHBOOH
    private String description;
    private String source; // Plant, Animal, Synthetic

    public HalalFoodEntity(String code, String name, String category, String status, String description, String source) {
        this.code = code;
        this.name = name;
        this.category = category;
        this.status = status;
        this.description = description;
        this.source = source;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
}
