package entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store_location")
public class StoreLocation extends BaseEntity{

    private String locationName;
    private Set<Sale> sales;

    public StoreLocation(){
        this.sales = new HashSet<>();
    }

    @Column(name = "location_name", nullable = false, unique = true)
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @OneToMany(mappedBy = "storeLocation", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    public Set<Sale> getSales() {
        return sales;
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }
}
